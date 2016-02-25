(ns kria.pb.index.yz.delete
  (:require
   [flatland.protobuf.core :as pb])
  (:import
   [com.basho.riak.protobuf
    RiakYokozunaPB$RpbYokozunaIndexDeleteReq]))

(set! *warn-on-reflection* true)

(def IndexDeleteReq
  (pb/protodef RiakYokozunaPB$RpbYokozunaIndexDeleteReq))

(defn IndexDeleteReq->bytes
  [m]
  (pb/protobuf-dump
   (pb/protobuf IndexDeleteReq
                m)))
