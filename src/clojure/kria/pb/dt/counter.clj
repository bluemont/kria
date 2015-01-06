(ns kria.pb.dt.counter
  (:require [kria.pb.dt.op])
  (:import
   [com.basho.riak.protobuf
    RiakDtPB$CounterOp]))

(set! *warn-on-reflection* true)

(defrecord CounterOp
    [increment]) ; optional sint64

(defn ^RiakDtPB$CounterOp CounterOp->pb
  [m]
  (let [b (RiakDtPB$CounterOp/newBuilder)]
    (if-let [x (:increment m)]
      (.setIncrement b x))
    (.build b)))
