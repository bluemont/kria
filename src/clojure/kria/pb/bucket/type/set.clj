(ns kria.pb.bucket.type.set
  (:require
   [flatland.protobuf.core :as pb])
  (:import
   [com.basho.riak.protobuf RiakPB$RpbSetBucketTypeReq]))

(set! *warn-on-reflection* true)

(def SetBucketTypeReq
  (pb/protodef RiakPB$RpbSetBucketTypeReq))

(defn SetBucketTypeReq->bytes
  [m]
  (pb/protobuf-dump
   (pb/protobuf
    SetBucketTypeReq
    m)))
