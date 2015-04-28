(ns kria.pb.index.put
  (:require
   [kria.pb.index.index :refer [Index->pb]])
  (:import
   [com.basho.riak.protobuf
    RiakYokozunaPB$RpbYokozunaIndexPutReq]))

(set! *warn-on-reflection* true)

(defrecord IndexPutReq
           [index ; required RpbYokozunaIndex
            ])

(defn ^RiakYokozunaPB$RpbYokozunaIndexPutReq IndexPutReq->pb
  [m]
  (let [b (RiakYokozunaPB$RpbYokozunaIndexPutReq/newBuilder)]
    (let [x (:index m)]
      (.setIndex b (Index->pb x)))
    (.build b)))

(defn IndexPutReq->bytes
  [m]
  (.toByteArray (IndexPutReq->pb m)))
