(ns kria.pb.bucket.set
  (:require
   [flatland.protobuf.core :as pb])
  (:import
   [com.basho.riak.protobuf RiakPB$RpbSetBucketReq]))

(set! *warn-on-reflection* true)

(def SetBucketReq
  (pb/protodef RiakPB$RpbSetBucketReq))

(defn SetBucketReq->bytes
  [m]
  (pb/protobuf-dump
   (pb/protobuf
    SetBucketReq
    m)))
