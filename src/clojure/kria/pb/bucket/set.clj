(ns kria.pb.bucket.set
  (:require
   [kria.conversions :refer [byte-string<-utf8-string]]
   [kria.pb.bucket.props :refer [BucketProps->pb]])
  (:import
   [com.basho.riak.protobuf RiakPB$RpbSetBucketReq]))

(set! *warn-on-reflection* true)

(defrecord SetBucketReq
           [bucket ; required bytes
            props  ; required RpbBucketProps
            type   ; optional bytes
            ])

(defn ^RiakPB$RpbSetBucketReq SetBucketReq->pb
  [m]
  (let [b (RiakPB$RpbSetBucketReq/newBuilder)]
    (let [x (:bucket m)]
      (.setBucket b x))
    (let [x (:props m)]
      (.setProps b (BucketProps->pb x)))
    (if-let [x (:type m)]
      (.setType b (byte-string<-utf8-string x)))
    (.build b)))

(defn SetBucketReq->bytes
  [m]
  (.toByteArray (SetBucketReq->pb m)))
