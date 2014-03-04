(ns kria.pb.index.delete
  (:require
    [kria.conversions :refer [utf8-string<-byte-string
                              byte-string<-utf8-string]])
  (:import
    [com.basho.riak.protobuf
     RiakYokozunaPB$RpbYokozunaIndexDeleteReq]))

(set! *warn-on-reflection* true)

(defrecord IndexDeleteReq
  [name ; required bytes
   ])

(defn ^RiakYokozunaPB$RpbYokozunaIndexDeleteReq IndexDeleteReq->pb
  [m]
  (let [b (RiakYokozunaPB$RpbYokozunaIndexDeleteReq/newBuilder)]
    (let [x (:name m)]
      (.setName b (byte-string<-utf8-string x)))
    (.build b)))

(defn IndexDeleteReq->bytes
  [m]
  (.toByteArray (IndexDeleteReq->pb m)))
