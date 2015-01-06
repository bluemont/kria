(ns kria.data-type.set
  (:refer-clojure :exclude [get])
  (:require
   [kria.pb.dt.op :refer [DtOp->pb]]
   [kria.pb.dt.set :refer [SetOp->pb]]
   [kria.data-type :as dt])
  (:import
   [com.basho.riak.protobuf
    RiakDtPB$DtValue]))

(set! *warn-on-reflection* true)

(defn ->DtSetOp
  [m]
  (DtOp->pb
   {:set-op (SetOp->pb m)}))

(defn update
  "Update a set in a bucket."
  [asc b t k adds removes opts cb]
  (dt/set asc b t k
          (->DtSetOp {:adds adds
                      :removes removes})
          opts cb))

(defn get
  "Get a set from a bucket."
  [asc b t k opts cb]

  (dt/get asc b t k opts
          (fn [asc e a]
            (if e
              (cb asc e nil)
              (cb asc e (.getSetValueList ^RiakDtPB$DtValue (:value a)))))))
