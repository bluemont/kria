(ns kria.pb.server.info
  (:require
   [kria.conversions :refer [utf8-string<-byte-string]])
  (:import
   [com.basho.riak.protobuf
    RiakPB$RpbGetServerInfoResp]))

(set! *warn-on-reflection* true)

(defrecord InfoResp
           [node           ; optional bytes
            server-version ; optional bytes
            ])

(defn pb->InfoResp
  [^RiakPB$RpbGetServerInfoResp pb]
  (->InfoResp
   (some-> (.getNode pb) utf8-string<-byte-string)
   (some-> (.getServerVersion pb) utf8-string<-byte-string)))

(defn bytes->InfoResp
  [^bytes x]
  (pb->InfoResp
   (RiakPB$RpbGetServerInfoResp/parseFrom x)))
