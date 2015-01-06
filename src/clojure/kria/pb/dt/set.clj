(ns kria.pb.dt.set
  (:require [kria.pb.dt.op])
  (:import
   [com.basho.riak.protobuf
    RiakDtPB$SetOp]))

(set! *warn-on-reflection* true)

(defrecord SetOp
    [adds    ; repeated bytes
     removes ; repeated bytes
     ])

(defn ^RiakDtPB$SetOp SetOp->pb
  [m]
  (let [b (RiakDtPB$SetOp/newBuilder)]
    (if-let [x (:adds m)]
      (.addAllAdds b x))
    (if-let [x (:removes m)]
      (.addAllRemoves b x))
    (.build b)))
