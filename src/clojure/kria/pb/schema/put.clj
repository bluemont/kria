(ns kria.pb.schema.put
  (:require
    [kria.schema.schema :refer [Schema->pb]])
  (:import
    [com.basho.riak.protobuf
     RiakYokozunaPB$RpbYokozunaSchemaPutReq]))

(set! *warn-on-reflection* true)

(defrecord SchemaPutReq
  [schema ; required RpbYokozunaSchema
   ])

(defn ^RiakYokozunaPB$RpbYokozunaSchemaPutReq SchemaPutReq->pb
  [m]
  (let [b (RiakYokozunaPB$RpbYokozunaSchemaPutReq/newBuilder)]
    (let [x (:schema m)]
      (.setSchema b (Schema->pb x)))
    (.build b)))
