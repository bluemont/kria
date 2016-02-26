(ns kria.pb.object.put
  (:require
   [flatland.protobuf.core :as pb])
  (:import
   [com.basho.riak.protobuf
    RiakKvPB$RpbPutReq
    RiakKvPB$RpbPutResp]))

(def PutReq (pb/protodef RiakKvPB$RpbPutReq))

(defn PutReq->bytes
  [m]
  (pb/protobuf-dump
   (pb/protobuf PutReq
                m)))

(def PutResp
  (pb/protodef
   RiakKvPB$RpbPutResp))

(defn bytes->PutResp
  [^bytes x]
  (pb/protobuf-load PutResp x))
