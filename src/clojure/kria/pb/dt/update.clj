(ns kria.pb.dt.update
  (:import
   [com.basho.riak.protobuf
    RiakDtPB$DtUpdateReq
    RiakDtPB$DtUpdateResp
    RiakDtPB$DtOp]))

(set! *warn-on-reflection* true)

(defrecord DtUpdateReq
           [bucket          ; required bytes
            key             ; optional bytes
            type            ; required bytes
            context         ; optional bytes
            op              ; required DtOp
            w               ; optional uint32
            dw              ; optional uint32
            pw              ; optional uint32
            return-body     ; optional bool
            timeout         ; optional uint32
            sloppy-quorum   ; optional bool
            n-val           ; optional uint32
            include-context ; optional bool
            ])

(defn ^RiakDtPB$DtUpdateReq DtUpdateReq->pb
  [m]
  (let [b (RiakDtPB$DtUpdateReq/newBuilder)]
    (let [x (:bucket m)]
      (.setBucket b x))
    (if-let [x (:key m)]
      (.setKey b x))
    (let [x (:type m)]
      (.setType b x))
    (if-let [x (:context m)]
      (.setContext b x))
    (let [x ^RiakDtPB$DtOp (:op m)]
      (.setOp b x))
    (if-let [x (:w m)]
      (.setW b x))
    (if-let [x (:dw m)]
      (.setDw b x))
    (if-let [x (:pw m)]
      (.setPw b x))
    (if-let [x (:return-body m)]
      (.setReturnBody b x))
    (if-let [x (:timeout m)]
      (.setTimeout b x))
    (if-let [x (:sloppy-quorum m)]
      (.setSloppyQuorum b x))
    (if-let [x (:n-val m)]
      (.setNVal b x))
    (if-let [x (:include-context m)]
      (.setIncludeContext b x))
    (.build b)))

(defn DtUpdateReq->bytes
  [m]
  (.toByteArray (DtUpdateReq->pb m)))

(defrecord DtUpdateResp
           [key           ; optional bytes
            context       ; optional bytes
            counter_value ; optional sint64
            set_value     ; repeated bytes
            map_value     ; repeated MapEntry
            ])

(defn pb->DtUpdateResp
  [^RiakDtPB$DtUpdateResp pb]
  (->DtUpdateResp
   (.getKey pb)
   (.getContext pb)
   (.getCounterValue pb)
   (.getSetValueList pb)
   (.getMapValueList pb)))

(defn bytes->DtUpdateResp
  [^bytes x]
  (pb->DtUpdateResp (RiakDtPB$DtUpdateResp/parseFrom x)))
