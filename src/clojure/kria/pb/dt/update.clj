(ns kria.pb.dt.update
  (:require
   [flatland.protobuf.core :as pb])
  (:import
   [com.basho.riak.protobuf
    RiakDtPB$DtUpdateReq
    RiakDtPB$DtUpdateResp
    RiakDtPB$DtOp]))

(set! *warn-on-reflection* true)

(def DtUpdateReq
  (pb/protodef RiakDtPB$DtUpdateReq))

(defn DtUpdateReq->bytes
  [m]
  (pb/protobuf-dump
   (pb/protobuf DtUpdateReq m)))

(def DtUpdateResp
  (pb/protodef RiakDtPB$DtUpdateResp))

(defn bytes->DtUpdateResp
  [^bytes x]
  (pb/protobuf-load DtUpdateResp x))
