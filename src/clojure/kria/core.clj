(ns kria.core
  (:require
   [kria.pb.messages :refer [get-message-reader get-message-writer]]
   [kria.conversions :refer [utf8-string<-byte-string]])
  (:import
   [java.nio ByteBuffer]
   [java.nio.channels AsynchronousSocketChannel CompletionHandler]
   [com.basho.riak.protobuf RiakPB$RpbErrorResp]
   [com.google.protobuf InvalidProtocolBufferException]))

(set! *warn-on-reflection* true)

(def bytes->ErrorResp
  (get-message-reader :error-resp))

(def ^:const message-codes
  {:error-resp 0
   :ping-req 1
   :ping-resp 2
   :get-client-id-req 3
   :get-client-id-resp 4
   :set-client-id-req 5
   :set-client-id-resp 6
   :get-server-info-req 7
   :get-server-info-resp 8
   :get-req 9
   :get-resp 10
   :put-req 11
   :put-resp 12
   :del-req 13
   :del-resp 14
   :list-buckets-req 15
   :list-buckets-resp 16
   :list-keys-req 17
   :list-keys-resp 18
   :get-bucket-req 19
   :get-bucket-resp 20
   :set-bucket-req 21
   :set-bucket-resp 22
   :map-red-req 23
   :map-red-resp 24
   :index-req 25
   :index-resp 26
   :search-query-req 27
   :search-query-resp 28
   :reset-bucket-req 29
   :reset-bucket-resp 30
   :get-bucket-type-req 31
   :set-bucket-type-req 32
   :counter-update-req 50
   :counter-update-resp 51
   :counter-get-req 52
   :counter-get-resp 53
   :yz-index-get-req 54
   :yz-index-get-resp 55
   :yz-index-put-req 56
   :yz-index-put-resp 12 ; *
   :yz-index-del-req 57
   :yz-index-del-resp 14 ; *
   :yz-schema-get-req 58
   :yz-schema-get-resp 59
   :yz-schema-put-req 60
   :yz-schema-put-resp 12 ; *
   :dt-fetch-req 80
   :dt-fetch-resp 81
   :dt-update-req 82
   :dt-update-resp 83})
; * https://github.com/basho/riak-java-client/issues/367

(defn ^Byte message-code-byte
  "Returns a Riak message code as a byte."
  [message-key]
  {:pre [(keyword? message-key)]}
  (byte (message-key message-codes)))

(defn simple-message
  "Returns a ByteBuffer message suitable for Riak."
  [message-key]
  {:pre [(keyword? message-key)]}
  (doto (ByteBuffer/allocate 5)
    (.putInt 1)
    (.put (message-code-byte message-key))
    (.rewind)))

(defn payload-message
  "Returns a ByteBuffer message suitable for Riak."
  [message-key ^bytes payload]
  (let [payload-length (count payload)
        body-length (inc payload-length)
        message-length (+ 4 body-length)]
    (doto (ByteBuffer/allocate message-length)
      (.putInt body-length)
      (.put (message-code-byte message-key))
      (.put payload)
      (.rewind))))

(defn length-and-code
  "Returns [length, code]. Mutates the supplied buffer, but the
  combined effects should cancel out."
  [^ByteBuffer buf]
  (.rewind buf)
  (vector (.getInt buf) (.get buf)))

(defn read-header-handler
  "Returns a CompletionHandler."
  [asc cb]
  {:pre [(fn? cb)]}
  (proxy [CompletionHandler] []
    (completed [n buf] (cb asc nil (length-and-code buf)))
    (failed [e buf] (cb asc e (length-and-code buf)))))

(defn read-header
  [^AsynchronousSocketChannel asc cb]
  {:pre [(fn? cb)]}
  (let [buf (ByteBuffer/allocate 5)]
    (.read asc buf buf (read-header-handler asc cb))))

(declare read-payload-handler)

(defn read-payload-handler
  "Returns a CompletionHandler."
  [^AsynchronousSocketChannel asc ^ByteBuffer buf len cb]
  {:pre [(integer? len) (fn? cb)]}
  (proxy [CompletionHandler] []
    (completed
      [n a]
      (if (.hasRemaining buf)
        (.read asc buf nil (read-payload-handler asc buf len cb))
        (cb asc nil (.array buf))))
    (failed
      [e a]
      (cb asc e nil))))

(defn read-payload
  "Reads a protobuf payload of length `len`. Note that this length
  is one less than the message length field."
  [^AsynchronousSocketChannel asc len cb]
  {:pre [(integer? len) (fn? cb)]}
  (let [buf (ByteBuffer/allocate len)
        handler (read-payload-handler asc buf len cb)]
    (.read asc buf nil handler)))

(defn parse-fn
  "Returns a parse function, which itself can serve as a callback.
  Will call the provided callback if an error occurs."
  [f cb]
  {:pre [(fn? f) (fn? cb)]}
  (fn [asc e a]
    (cond
      e (cb asc e nil)
      a (try
          (cb asc nil (f a))
          (catch InvalidProtocolBufferException ipbe
            (cb asc ipbe nil)))
      :else (cb asc {:attachment nil} nil))))

(defn error-parse-fn
  "Returns a parse function, which itself can serve as a callback.
  Will call the provided callback if an error occurs."
  [cb]
  (fn [asc e a]
    (try
      (let [resp (bytes->ErrorResp a)
            error {:message (-> resp :errmsg utf8-string<-byte-string) :code (:errcode resp)}]
        (cb asc error nil))
      (catch InvalidProtocolBufferException e
        (cb asc e nil)))))

(declare header-cb-fn)

(defn stream-cb-fn
  "Returns a stream callback function."
  [exp-key parser cb chunk-fn done-fn stream-cb]
  (fn [asc e a]
    (let [p (parser asc e a)]
      (stream-cb (chunk-fn p))
      (if (done-fn p)
        (stream-cb nil)
        (let [hcb (header-cb-fn
                   exp-key true parser cb chunk-fn done-fn stream-cb)]
          (read-header asc hcb))))))

(defn header-cb-fn
  "Returns a header callback function based on:
  * exp-key : an expected message key
  * multi-resp : are multiple responses expected (e.g. streaming)?
  * parser : a parser function
  * cb : a callback function

  Optional parameters include:
  * chunk-fn : a chunking function
  * done-fn : a predicate testing completion
  * stream-cb : a streaming callback function"
  [exp-key multi-resp? parser cb & [chunk-fn done-fn stream-cb]]
  {:pre [(keyword? exp-key) (fn? parser) (fn? cb)]}
  (let [exp (exp-key message-codes)
        err (:error-resp message-codes)]
    (fn [asc e [l c]]
      (if e
        (cb asc e nil)
        (cond
          (= c exp)
          (if multi-resp?
            (let [scb (stream-cb-fn
                       exp-key parser cb chunk-fn done-fn stream-cb)]
              (read-payload asc (dec l) scb))
            (read-payload asc (dec l) parser))

          (= c err)
          (read-payload asc (dec l) (error-parse-fn cb))

          :else
          (cb asc {:code {:actual c :expected exp}} nil))))))

(declare write-all-handler)

(defn write-all-handler
  "Returns a CompletionHandler that calls `read-header` on success
  or `cb` on failure."
  [^AsynchronousSocketChannel asc ^ByteBuffer message cb]
  {:pre [(fn? cb)]}
  (proxy [CompletionHandler] []
    (completed
      [n a]
      (if (.hasRemaining message)
        (.write asc message nil (write-all-handler asc message cb))
        (read-header asc cb)))
    (failed
      [e a]
      (cb asc e nil))))


(defn call
  "A template function to call API via the protobuf interface.

  Parameters:
  * asc : AsynchronousSocketChannel
  * cb : callback function
  * req-key : request message key
  * resp-key : response message key
  * req-map->bytes : fn to convert a request map to a request byte array
  * bytes->resp-map : fn to convert a response byte array to a response map
  * req-map : request map (constructed from function arguments)
  * multi-resp? : are multiple responses expected?
  * chunk-fn : a chunking function
  * done-fn : a predicate function that tests completion
  * stream-cb : a streaming callback function"
  [^AsynchronousSocketChannel asc cb req-key resp-key
   _ _ ;; req-map->bytes bytes->resp-map
   req-map
   & [multi-resp? chunk-fn done-fn stream-cb]]
  {:pre [(fn? cb) (keyword? req-key) (keyword? resp-key)
         ;; (fn? req-map->bytes) (fn? bytes->resp-map)
         (map? req-map)]}
  (let [req-map->bytes (get-message-writer req-key)
        bytes->resp-map (get-message-reader resp-key)
        payload (req-map->bytes req-map)
        message (payload-message req-key payload)
        parser (parse-fn bytes->resp-map cb)
        header-cb (header-cb-fn resp-key multi-resp? parser cb
                                chunk-fn done-fn stream-cb)
        handler (write-all-handler asc message header-cb)]
    (.write asc message nil handler)))
