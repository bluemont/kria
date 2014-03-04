(ns kria.pb.mod-fun
  (:require
    [kria.conversions :refer [utf8-string<-byte-string
                              byte-string<-utf8-string]])
  (:import
    [com.basho.riak.protobuf
     RiakPB$RpbModFun]))

(set! *warn-on-reflection* true)

(defrecord ModFun
  [module   ; required bytes
   function ; required bytes
   ])

(defn pb->ModFun
  [^RiakPB$RpbModFun pb]
  (->ModFun
    (some-> (.getModule pb) utf8-string<-byte-string)
    (some-> (.getFunction pb) utf8-string<-byte-string)))

(defn ^RiakPB$RpbModFun ModFun->pb
  [m]
  (let [b (RiakPB$RpbModFun/newBuilder)]
    (let [x (:module m)]
      (.setModule b (byte-string<-utf8-string x)))
    (let [x (:function m)]
      (.setFunction b (byte-string<-utf8-string x)))
    (.build b)))
