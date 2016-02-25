(ns kria.pb.index.secondary.get
  (:require
   [flatland.protobuf.core :as pb])
  (:import
   [com.basho.riak.protobuf
    RiakKvPB$RpbIndexReq
    RiakKvPB$RpbIndexResp]))

(def IndexReq (pb/protodef RiakKvPB$RpbIndexReq))

(defn IndexReq->bytes
  [m]
  (pb/protobuf-dump (pb/protobuf IndexReq
                                 m)))

(def IndexResp
  (pb/protodef
   RiakKvPB$RpbIndexResp))

(defn bytes->IndexResp
  [^bytes x]
  (pb/protobuf-load IndexResp x))
