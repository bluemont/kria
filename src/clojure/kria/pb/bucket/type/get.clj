(ns kria.pb.bucket.type.get
  (:require
   [flatland.protobuf.core :as pb]
   [kria.pb.bucket.get :refer [bytes->GetBucketResp]])
  (:import
   [com.basho.riak.protobuf
    RiakPB$RpbGetBucketTypeReq]))

(set! *warn-on-reflection* true)

(def GetBucketTypeReq
  (pb/protodef RiakPB$RpbGetBucketTypeReq))

(defn GetBucketTypeReq->bytes
  [m]
  (pb/protobuf-dump
   (pb/protobuf
    GetBucketTypeReq
    m)))

(def bytes->GetBucketTypeResp
  bytes->GetBucketResp)
