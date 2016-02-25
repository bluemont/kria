(ns kria.pb.bucket.list-keys
  (:require
   [flatland.protobuf.core :as pb])
  (:import
   [com.basho.riak.protobuf
    RiakKvPB$RpbListKeysReq
    RiakKvPB$RpbListKeysResp]))

(set! *warn-on-reflection* true)

(def ListKeysReq
  (pb/protodef RiakKvPB$RpbListKeysReq))

(defn ListKeysReq->bytes
  [m]
  (pb/protobuf-dump
   (pb/protobuf ListKeysReq
                m)))

(def ListKeysResp
  (pb/protodef RiakKvPB$RpbListKeysResp))

(defn bytes->ListKeysResp
  [^bytes x]
  (pb/protobuf-load ListKeysResp x))
