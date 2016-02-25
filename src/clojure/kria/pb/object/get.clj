(ns kria.pb.object.get
  (:require
   [flatland.protobuf.core :as pb]
   [kria.conversions :refer [byte-string<-utf8-string]]
   [kria.pb.content :refer [pb->Content Content->pb]])
  (:import
   [com.basho.riak.protobuf
    RiakKvPB$RpbGetReq
    RiakKvPB$RpbGetResp]))

(def GetReq (pb/protodef RiakKvPB$RpbGetReq))

(defn GetReq->bytes
  [m]
  (pb/protobuf-dump (pb/protobuf GetReq
                                 m)))

(def GetResp (pb/protodef RiakKvPB$RpbGetResp))

(defn bytes->GetResp
  [^bytes x]
  (pb/protobuf-load GetResp x))
