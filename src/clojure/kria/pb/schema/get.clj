(ns kria.pb.schema.get
  (:require
   [flatland.protobuf.core :as pb])
  (:import
   [com.basho.riak.protobuf
    RiakYokozunaPB$RpbYokozunaSchemaGetReq
    RiakYokozunaPB$RpbYokozunaSchemaGetResp]))

(set! *warn-on-reflection* true)

(def SchemaGetReq
  (pb/protodef RiakYokozunaPB$RpbYokozunaSchemaGetReq))

(defn SchemaGetReq->bytes
  [m]
  (pb/protobuf-dump
   (pb/protobuf
    SchemaGetReq
    m)))


(def SchemaGetResp
  (pb/protodef RiakYokozunaPB$RpbYokozunaSchemaGetResp))

(defn bytes->SchemaGetResp
  [^bytes x]
  (pb/protobuf-load SchemaGetResp x))
