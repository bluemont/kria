(ns kria.data-type.set
  (:require
   [kria.pb.dt.op :refer [DtOp->pb]]
   [kria.pb.dt.set :refer [SetOp->pb]]
   [kria.data-type :as dt]))

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
