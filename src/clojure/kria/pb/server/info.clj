(ns kria.pb.server.info
  (:require
   [flatland.protobuf.core :as pb])
  (:import
   [com.basho.riak.protobuf
    RiakPB$RpbGetServerInfoResp]))

(set! *warn-on-reflection* true)

(def InfoResp
  (pb/protodef
   RiakPB$RpbGetServerInfoResp))

(defn bytes->InfoResp
  [^bytes x]
  (pb/protobuf-load InfoResp x))
