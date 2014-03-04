(ns kria.pb.object.delete
  (:import
    [com.basho.riak.protobuf RiakKvPB$RpbDelReq]))

(defrecord DeleteReq
  [bucket        ; required bytes
   key           ; required bytes
   rw            ; optional uint32
   vclock        ; optional bytes
   r             ; optional uint32
   w             ; optional uint32
   pr            ; optional uint32
   pw            ; optional uint32
   dw            ; optional uint32
   timeout       ; optional uint32
   sloppy-quorum ; optional bool
   n-val         ; optional uint32
   type          ; optional bytes
   ])

(defn ^RiakKvPB$RpbDelReq DeleteReq->pb
  [m]
  (let [b (RiakKvPB$RpbDelReq/newBuilder)]
    (let [x (:bucket m)]
      (.setBucket b x))
    (let [x (:key m)]
      (.setKey b x))
    (if-let [x (:rw m)]
      (.setRw b x))
    (if-let [x (:vclock m)]
      (.setVclock b x))
    (if-let [x (:r m)]
      (.setR b x))
    (if-let [x (:w m)]
      (.setW b x))
    (if-let [x (:pr m)]
      (.setPr b x))
    (if-let [x (:pw m)]
      (.setPw b x))
    (if-let [x (:dw m)]
      (.setDw b x))
    (if-let [x (:timeout m)]
      (.setTimeout b x))
    (if-let [x (:sloppy-quorum m)]
      (.setSloppyQuorum b x))
    (if-let [x (:n-val m)]
      (.setNVal b x))
    (if-let [x (:type m)]
      (.setType b x))
    (.build b)))

(defn DeleteReq->bytes
  [m]
  (.toByteArray (DeleteReq->pb m)))
