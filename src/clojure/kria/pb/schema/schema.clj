(ns kria.pb.schema.schema
  (:require
    [kria.conversions :refer [utf8-string<-byte-string
                              byte-string<-utf8-string]])
  (:import
    [com.basho.riak.protobuf
     RiakYokozunaPB$RpbYokozunaSchema]))

(set! *warn-on-reflection* true)

(defrecord Schema
  [name    ; required bytes
   content ; optional bytes
   ])

(defn ^RiakYokozunaPB$RpbYokozunaSchema Schema->pb
  [m]
  (let [b (RiakYokozunaPB$RpbYokozunaSchema/newBuilder)]
    (let [x (:name m)]
      (.setName b (byte-string<-utf8-string x)))
    (if-let [x (:content m)]
      (.setContent b (byte-string<-utf8-string x)))
    (.build b)))

(defn pb->Schema
  [^RiakYokozunaPB$RpbYokozunaSchema pb]
  (->Schema
    (some-> (.getName pb) utf8-string<-byte-string)
    (some-> (.getContent pb) utf8-string<-byte-string)))
