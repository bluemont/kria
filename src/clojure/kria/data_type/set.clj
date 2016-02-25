(ns kria.data-type.set
  (:refer-clojure :exclude [get update])
  (:require
   [kria.data-type :as dt]))

(set! *warn-on-reflection* true)

(defn update
  "Update a set in a bucket."
  [asc b t k adds removes opts cb]
  (dt/set asc b t k
          {:set-op {:adds adds :removes removes}}
          opts cb))

(defn get
  "Get a set from a bucket."
  [asc b t k opts cb]
  (dt/get asc b t k opts
          (fn [asc e a]
            (if e
              (cb asc e nil)
              (cb asc e (:set-value (:value a)))))))
