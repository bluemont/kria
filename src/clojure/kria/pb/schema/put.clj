(ns kria.pb.schema.put
  (:require
   [flatland.protobuf.core :as pb])
  (:import
   [com.basho.riak.protobuf
    RiakYokozunaPB$RpbYokozunaSchemaPutReq]))

(set! *warn-on-reflection* true)

(def SchemaPutReq
  (pb/protodef
   RiakYokozunaPB$RpbYokozunaSchemaPutReq))

(defn SchemaPutReq->bytes
  [m]
  (pb/protobuf-dump
   (pb/protobuf
    SchemaPutReq
    m)))
