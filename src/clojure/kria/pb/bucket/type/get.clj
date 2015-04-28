(ns kria.pb.bucket.type.get
  (:require
   [kria.conversions :refer [byte-string<-utf8-string]]
   [kria.pb.bucket.props :refer [pb->BucketProps]]
   [kria.pb.bucket.get :refer [bytes->GetBucketResp]])
  (:import
   [com.basho.riak.protobuf
    RiakPB$RpbGetBucketTypeReq
    RiakPB$RpbGetBucketResp]))

(set! *warn-on-reflection* true)

(defrecord GetBucketReq
           [type]) ; required bytes

(defn ^RiakPB$RpbGetBucketTypeReq GetBucketTypeReq->pb
  [m]
  (let [bt (RiakPB$RpbGetBucketTypeReq/newBuilder)]
    (let [x (:type m)]
      (.setType bt x))
    (.build bt)))

(defn GetBucketTypeReq->bytes
  [m]
  (.toByteArray (GetBucketTypeReq->pb m)))

(def bytes->GetBucketTypeResp
  bytes->GetBucketResp)
