(ns kria.pb.dt.op
  (:import
   [com.basho.riak.protobuf
    RiakDtPB$DtOp
    RiakDtPB$CounterOp
    RiakDtPB$SetOp
    RiakDtPB$MapOp]))

(set! *warn-on-reflection* true)

(defrecord DtOp
    [counter-op ; optional CounterOp
     set-op     ; optional SetOp
     map-op     ; optional MapOp
     ])

(defn ^RiakDtPB$DtOp DtOp->pb
  [m]
  (let [b (RiakDtPB$DtOp/newBuilder)]
    (if-let [x ^RiakDtPB$CounterOp (:counter-op m)]
      (.setCounterOp b x))
    (if-let [x ^RiakDtPB$SetOp (:set-op m)]
      (.setSetOp b x))
    (if-let [x ^RiakDtPB$MapOp (:map-op m)]
      (.setMapOp b x))
    (.build b)))
