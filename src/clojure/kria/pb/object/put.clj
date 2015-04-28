(ns kria.pb.object.put
  (:require
   [kria.conversions :refer [byte-string<-utf8-string]]
   [kria.pb.content :refer [pb->Content Content->pb]])
  (:import
   [com.basho.riak.protobuf
    RiakKvPB$RpbPutReq
    RiakKvPB$RpbPutResp]))

(defrecord PutReq
           [bucket          ; required bytes
            key             ; optional bytes
            vclock          ; optional bytes
            content         ; required RpbContent
            w               ; optional uint32
            dw              ; optional uint32
            return-body     ; optional bool
            pw              ; optional uint32
            if-not-modified ; optional bool
            if-none-match   ; optional bool
            return-head     ; optional bool
            timeout         ; optional uint32
            as-is           ; optional bool
            sloppy-quorum   ; optional bool
            n-val           ; optional uint32
            type            ; optional bytes
            ])

(defn ^RiakKvPB$RpbPutReq PutReq->pb
  [m]
  (let [b (RiakKvPB$RpbPutReq/newBuilder)]
    (let [x (:bucket m)]
      (.setBucket b x))
    (if-let [x (:key m)]
      (.setKey b x))
    (if-let [x (:vclock m)]
      (.setVclock b x))
    (let [x (:content m)]
      (.setContent b (Content->pb x)))
    (if-let [x (:w m)]
      (.setW b x))
    (if-let [x (:dw m)]
      (.setDw b x))
    (if-let [x (:return-body m)]
      (.setReturnBody b x))
    (if-let [x (:pw m)]
      (.setPw b x))
    (if-let [x (:if-not-modified m)]
      (.setIfNotModified b x))
    (if-let [x (:if-none-match m)]
      (.setIfNoneMatch b x))
    (if-let [x (:return-head m)]
      (.setReturnHead b x))
    (if-let [x (:as-is m)]
      (.setAsis b x))
    (if-let [x (:timeout m)]
      (.setTimeout b x))
    (if-let [x (:sloppy-quorum m)]
      (.setSloppyQuorum b x))
    (if-let [x (:n-val m)]
      (.setNVal b x))
    (if-let [x (:type m)]
      (.setType b (byte-string<-utf8-string x)))
    (.build b)))

(defn PutReq->bytes
  [m]
  (.toByteArray (PutReq->pb m)))

(defrecord PutResp
           [contents ; repeated RpbContent
            vclock   ; optional bytes
            key      ; optional bytes
            ])

(defn pb->PutResp
  [^RiakKvPB$RpbPutResp pb]
  (->PutResp
   (mapv pb->Content (.getContentList pb))
   (.getVclock pb)
   (.getKey pb)))

(defn bytes->PutResp
  [^bytes x]
  (pb->PutResp (RiakKvPB$RpbPutResp/parseFrom x)))
