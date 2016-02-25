(ns kria.pb.index.yz.get
  (:require
   [flatland.protobuf.core :as pb])
  (:import
   [com.basho.riak.protobuf
    RiakYokozunaPB$RpbYokozunaIndexGetReq
    RiakYokozunaPB$RpbYokozunaIndexGetResp]))

(set! *warn-on-reflection* true)

(def IndexGetReq
  (pb/protodef RiakYokozunaPB$RpbYokozunaIndexGetReq))

(defn IndexGetReq->bytes
  [m]
  (pb/protobuf-dump
   (pb/protobuf IndexGetReq
                m)))

(def IndexGetResp
  (pb/protodef RiakYokozunaPB$RpbYokozunaIndexGetResp))

(defn bytes->IndexGetResp
  [^bytes x]
  (pb/protobuf-load IndexGetResp x))
