(ns kria.data-type.counter
  (:refer-clojure :exclude [get])
  (:require
   [kria.pb.dt.op :refer [DtOp->pb]]
   [kria.pb.dt.counter :refer [CounterOp->pb]]
   [kria.data-type :as dt])
  (:import
   [com.basho.riak.protobuf
    RiakDtPB$DtValue]))

(set! *warn-on-reflection* true)

(defn- int64?
  [n]
  (or (instance? Integer n)
      (instance? Long n)
      (instance? Short n)
      (instance? Byte n)))

(defn ->DtCounterOp
  [incr]
  (DtOp->pb
   {:counter-op (CounterOp->pb {:increment incr})}))

(defn increment
  "Increments a counter in a bucket."
  [asc b t k incr opts cb]
  {:pre [(int64? incr)]}
  (dt/set asc b t k (->DtCounterOp incr) opts cb))

(defn get
  "Get a counter from a bucket."
  [asc b t k opts cb]
  (dt/get asc b t k opts
          (fn [asc e a]
            (if e
              (cb asc e nil)
              (cb asc e (.getCounterValue ^RiakDtPB$DtValue (:value a)))))))
