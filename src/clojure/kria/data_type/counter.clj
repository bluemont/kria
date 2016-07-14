(ns kria.data-type.counter
  (:refer-clojure :exclude [get])
  (:require
   [kria.data-type :as dt]))

(set! *warn-on-reflection* true)

(defn- int64?
  [n]
  (or (instance? Integer n)
      (instance? Long n)
      (instance? Short n)
      (instance? Byte n)))

(defn increment
  "Increments a counter in a bucket."
  [asc b t k incr opts cb]
  {:pre [(int64? incr)]}
  (dt/set asc b t k {:counter-op {:increment incr}} opts cb))

(defn get
  "Get a counter from a bucket."
  [asc b t k opts cb]
  (dt/get asc b t k opts
          (fn [asc e a]
            (if e
              (cb asc e nil)
              (cb asc e (:counter-value (:value a)))))))
