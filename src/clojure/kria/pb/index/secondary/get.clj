(ns kria.pb.index.secondary.get
  (:require
   [flatland.protobuf.core :as pb])
  (:import
   [com.basho.riak.protobuf
    RiakKvPB$RpbIndexReq
    RiakKvPB$RpbIndexResp]
   [flatland.protobuf PersistentProtocolBufferMap]))

(def IndexReq (pb/protodef RiakKvPB$RpbIndexReq))



(defn ^PersistentProtocolBufferMap IndexReq->pb
  [m]
  (pb/protobuf IndexReq
               m))

(defn IndexReq->bytes
  [m]
  (pb/protobuf-dump (IndexReq->pb m)))

(def IndexResp
  (pb/protodef
   RiakKvPB$RpbIndexResp))

(defn bytes->IndexResp
  [^bytes x]
  (pb/protobuf-load IndexResp x))
