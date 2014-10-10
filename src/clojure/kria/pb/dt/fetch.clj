(ns kria.pb.dt.fetch
  (:import
   [com.basho.riak.protobuf
    RiakDtPB$DtFetchReq
    RiakDtPB$DtFetchResp]))

(set! *warn-on-reflection* true)

(defrecord DtFetchReq
    [bucket          ; required bytes
     key             ; required bytes
     type            ; required bytes
     r               ; optional uint32
     pr              ; optional uint32
     basic-quorum    ; optional bool
     notfound-ok     ; optional bool
     timeout         ; optional uint32
     sloppy-quorum   ; optional bool
     n-val           ; optional uint32
     include-context ; optional bool
     ])

(defn ^RiakDtPB$DtFetchReq DtFetchReq->pb
  [m]
  (let [b (RiakDtPB$DtFetchReq/newBuilder)]
    (let [x (:bucket m)]
      (.setBucket b x))
    (let [x (:key m)]
      (.setKey b x))
    (let [x (:type m)]
      (.setType b x))
    (if-let [x (:r m)]
      (.setR b x))
    (if-let [x (:pr m)]
      (.setPr b x))
    (if-let [x (:basic-quorum m)]
      (.setBasicQuorum b x))
    (if-let [x (:notfound-ok m)]
      (.setNotfoundOk b x))
    (if-let [x (:timeout m)]
      (.setTimeout b x))
    (if-let [x (:sloppy-quorum m)]
      (.setSloppyQuorum b x))
    (if-let [x (:n-val m)]
      (.setNVal b x))
    (if-let [x (:include-context m)]
      (.setIncludeContext b x))
    (.build b)))

(defn DtFetchReq->bytes
  [m]
  (.toByteArray (DtFetchReq->pb m)))

(defrecord DtFetchResp
    [context ; optional bytes
     type    ; required DataType
     value   ; optional DtValue
     ])

(defn pb->DtFetchResp
  [^RiakDtPB$DtFetchResp pb]
  (->DtFetchResp
   (.getContext pb)
   (.getType pb)
   (.getValue pb)))

(defn bytes->DtFetchResp
  [^bytes x]
  (pb->DtFetchResp (RiakDtPB$DtFetchResp/parseFrom x)))
