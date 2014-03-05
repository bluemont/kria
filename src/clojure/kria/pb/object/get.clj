(ns kria.pb.object.get
  (:require
    [kria.conversions :refer [byte-string<-utf8-string]]
    [kria.pb.content :refer [pb->Content Content->pb]])
  (:import
    [com.basho.riak.protobuf
     RiakKvPB$RpbGetReq
     RiakKvPB$RpbGetResp]))

(defrecord GetReq
  [bucket         ; required bytes
   key            ; required bytes
   r              ; optional uint32
   pr             ; optional uint32
   basic-quorum   ; optional bool
   not-found-ok   ; optional bool
   if-modified    ; optional bytes
   head           ; optional bool
   deleted-vclock ; optional bool
   timeout        ; optional uint32
   sloppy-quorum  ; optional bool
   n-val          ; optional uint32
   type           ; optional bytes
   ])

(defn ^RiakKvPB$RpbGetReq GetReq->pb
  [m]
  (let [b (RiakKvPB$RpbGetReq/newBuilder)]
    (let [x (:bucket m)]
      (.setBucket b x))
    (let [x (:key m)]
      (.setKey b x))
    (if-let [x (:r m)]
      (.setR b x))
    (if-let [x (:pr m)]
      (.setPr b x))
    (if-let [x (:basic-quorum m)]
      (.setBasicQuorum b x))
    (if-let [x (:not-found-ok m)]
      (.setNotFoundOk b x))
    (if-let [x (:if-modified m)]
      (.setIfModified b x))
    (if-let [x (:head m)]
      (.setHead b x))
    (if-let [x (:deleted-vclock m)]
      (.setDeletedVclock b x))
    (if-let [x (:timeout m)]
      (.setTimeout b x))
    (if-let [x (:sloppy-quorum m)]
      (.setSloppyQuorum b x))
    (if-let [x (:n-val m)]
      (.setNVal b x))
    (if-let [x (:type m)]
      (.setType b (byte-string<-utf8-string x)))
    (.build b)))

(defn GetReq->bytes
  [m]
  (.toByteArray (GetReq->pb m)))

(defrecord GetResp
  [content   ; repeated RpbContent
   vclock    ; optional bytes
   unchanged ; optional bool
   ])

(defn pb->GetResp
  [^RiakKvPB$RpbGetResp pb]
  (->GetResp
    (mapv pb->Content (.getContentList pb))
    (.getVclock pb)
    (.getUnchanged pb)))

(defn bytes->GetResp
  [^bytes x]
  (pb->GetResp
    (RiakKvPB$RpbGetResp/parseFrom x)))
