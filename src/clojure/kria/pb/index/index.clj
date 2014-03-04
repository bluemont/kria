(ns kria.pb.index.index
  (:require
    [kria.conversions :refer [utf8-string<-byte-string
                              byte-string<-utf8-string]])
  (:import
    [com.basho.riak.protobuf
     RiakYokozunaPB$RpbYokozunaIndex]))

(set! *warn-on-reflection* true)

(defrecord Index
  [name   ; required bytes
   schema ; optional bytes
   n-val  ; optional uint32
   ])

(defn ^RiakYokozunaPB$RpbYokozunaIndex Index->pb
  [m]
  (let [b (RiakYokozunaPB$RpbYokozunaIndex/newBuilder)]
    (let [x (:name m)]
      (.setName b (byte-string<-utf8-string x)))
    (if-let [x (:schema m)]
      (.setSchema b (byte-string<-utf8-string x)))
    (if-let [x (:n-val m)]
      (.setNVal b x))
    (.build b)))

(defn Index->bytes
  [m]
  (.toByteArray (Index->pb m)))

(defn pb->Index
  [^RiakYokozunaPB$RpbYokozunaIndex pb]
  (->Index
    (some-> (.getName pb) utf8-string<-byte-string)
    (some-> (.getSchema pb) utf8-string<-byte-string)
    (.getNVal pb)))

(defn bytes->Index
  [^bytes x]
  (pb->Index
    (RiakYokozunaPB$RpbYokozunaIndex/parseFrom x)))
