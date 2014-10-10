(ns kria.pb.bucket.type.set
  (:require
    [kria.conversions :refer [byte-string<-utf8-string]]
    [kria.pb.bucket.props :refer [BucketProps->pb]])
  (:import
    [com.basho.riak.protobuf RiakPB$RpbSetBucketTypeReq]))

(set! *warn-on-reflection* true)

(defrecord SetBucketTypeReq
  [type  ; required bytes
   props ; required RpbBucketProps
   ])

(defn ^RiakPB$RpbSetBucketTypeReq SetBucketTypeReq->pb
  [m]
  (let [b (RiakPB$RpbSetBucketTypeReq/newBuilder)]
    (let [x (:type m)]
      (.setType b x))
    (let [x (:props m)]
      (.setProps b (BucketProps->pb x)))
    (.build b)))

(defn SetBucketTypeReq->bytes
  [m]
  (.toByteArray (SetBucketTypeReq->pb m)))
