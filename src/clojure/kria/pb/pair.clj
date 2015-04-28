(ns kria.pb.pair
  (:require
   [kria.conversions
    :refer [byte-string<-utf8-string
            utf8-string<-byte-string]])
  (:import
   [com.basho.riak.protobuf
    RiakPB$RpbPair]))

(set! *warn-on-reflection* true)

(defrecord Pair
           [key   ; required bytes
            value ; optional bytes
            ])

(defn ^RiakPB$RpbPair Pair->pb
  [r]
  (let [b (RiakPB$RpbPair/newBuilder)]
    (let [x (:key r)]
      (.setKey b (byte-string<-utf8-string x)))
    (if-let [x (:value r)]
      (.setValue b (byte-string<-utf8-string x)))
    (.build b)))

(defn pb->Pair
  [^RiakPB$RpbPair pb]
  (->Pair
   (some-> (.getKey pb) utf8-string<-byte-string)
   (some-> (.getValue pb) utf8-string<-byte-string)))
