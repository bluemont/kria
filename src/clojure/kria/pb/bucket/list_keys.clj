(ns kria.pb.bucket.list-keys
  (:require
   [kria.conversions :refer [utf8-string<-byte-string
                             byte-string<-utf8-string]])
  (:import
   [com.basho.riak.protobuf
    RiakKvPB$RpbListKeysReq
    RiakKvPB$RpbListKeysResp]))

(set! *warn-on-reflection* true)

(defrecord ListKeysReq
           [bucket  ; required bytes
            timeout ; optional uint32
            type])  ; optional bytes

(defn ^RiakKvPB$RpbListKeysReq ListKeysReq->pb
  [m]
  (let [b (RiakKvPB$RpbListKeysReq/newBuilder)]
    (let [x (:bucket m)]
      (.setBucket b x))
    (if-let [x (:timeout m)]
      (.setTimeout b x))
    (if-let [x (:type m)]
      (.setType b (byte-string<-utf8-string x)))
    (.build b)))

(defn ListKeysReq->bytes
  [m]
  (.toByteArray (ListKeysReq->pb m)))

(defrecord ListKeysResp
           [keys   ; repeated bytes
            done]) ; optional bool

(defn pb->ListKeysResp
  [^RiakKvPB$RpbListKeysResp pb]
  (->ListKeysResp
   (vec (.getKeysList pb))
   (.getDone pb)))

(defn bytes->ListKeysResp
  [^bytes x]
  (pb->ListKeysResp
   (RiakKvPB$RpbListKeysResp/parseFrom x)))
