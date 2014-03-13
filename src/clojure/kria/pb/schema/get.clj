(ns kria.pb.schema.get
  (:require
    [kria.conversions :refer [utf8-string<-byte-string
                              byte-string<-utf8-string]]
    [kria.pb.schema.schema :refer [pb->Schema]])
  (:import
    [com.basho.riak.protobuf
     RiakYokozunaPB$RpbYokozunaSchemaGetReq
     RiakYokozunaPB$RpbYokozunaSchemaGetResp]))

(set! *warn-on-reflection* true)

(defrecord SchemaGetReq
  [name ; required bytes
   ])

(defn ^RiakYokozunaPB$RpbYokozunaSchemaGetReq SchemaGetReq->pb
  [m]
  (let [b (RiakYokozunaPB$RpbYokozunaSchemaGetReq/newBuilder)]
    (let [x (:name m)]
      (.setName b (byte-string<-utf8-string x)))
    (.build b)))

(defrecord SchemaGetResp
  [schema ; required RpbYokozunaSchema
   ])

(defn pb->SchemaGetResp
  [^RiakYokozunaPB$RpbYokozunaSchemaGetResp pb]
  (->SchemaGetResp
    (pb->Schema (.getSchema pb))))
