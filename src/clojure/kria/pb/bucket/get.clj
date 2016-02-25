(ns kria.pb.bucket.get
  (:require
   [flatland.protobuf.core :as pb])
  (:import
   [com.basho.riak.protobuf
    RiakPB$RpbGetBucketReq
    RiakPB$RpbGetBucketResp]))

(set! *warn-on-reflection* true)

(def GetBucketReq
  (pb/protodef RiakPB$RpbGetBucketReq))

(defn GetBucketReq->bytes
  [m]
  (pb/protobuf-dump
   (pb/protobuf
    GetBucketReq
    m)))

(def GetBucketResp
  (pb/protodef RiakPB$RpbGetBucketResp))

(defn bytes->GetBucketResp
  [^bytes x]
  (pb/protobuf-load GetBucketResp x))
