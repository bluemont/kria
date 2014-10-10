(ns kria.data-type.counter
  (:require
   [kria.pb.dt.op :refer [DtOp->pb]]
   [kria.pb.dt.counter :refer [CounterOp->pb]]
   [kria.data-type :as dt]))

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
