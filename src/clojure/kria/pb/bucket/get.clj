(ns kria.pb.bucket.get
  (:require
    [kria.pb.bucket.props :refer [pb->BucketProps]])
  (:import
    [com.basho.riak.protobuf
     RiakPB$RpbGetBucketReq
     RiakPB$RpbGetBucketResp]))

(set! *warn-on-reflection* true)

(defrecord GetBucketReq
  [bucket ; required bytes
   type   ; optional bytes
   ])

(defn ^RiakPB$RpbGetBucketReq GetBucketReq->pb
  [m]
  (let [b (RiakPB$RpbGetBucketReq/newBuilder)]
    (let [x (:bucket m)]
      (.setBucket b x))
    (if-let [x (:type m)]
      (.setType b x))
    (.build b)))

(defn GetBucketReq->bytes
  [m]
  (.toByteArray (GetBucketReq->pb m)))

(defrecord GetBucketResp
  [props ; required RpbBucketProps
   ])

(defn pb->GetBucketResp
  [^RiakPB$RpbGetBucketResp pb]
  (->GetBucketResp
    (pb->BucketProps (.getProps pb))))

(defn bytes->GetBucketResp
  [^bytes x]
  (pb->GetBucketResp
    (RiakPB$RpbGetBucketResp/parseFrom x)))
