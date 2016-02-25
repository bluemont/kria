(ns kria.pb.index.yz.put
  (:require
   [flatland.protobuf.core :as pb])
  (:import
   [com.basho.riak.protobuf
    RiakYokozunaPB$RpbYokozunaIndexPutReq]))

(set! *warn-on-reflection* true)

(def IndexPutReq
  (pb/protodef RiakYokozunaPB$RpbYokozunaIndexPutReq))

(defn IndexPutReq->bytes
  [m]
  (pb/protobuf-dump
   (pb/protobuf IndexPutReq
                m)))
