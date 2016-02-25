(ns kria.pb.dt.fetch
  (:require
   [flatland.protobuf.core :as pb])
  (:import
   [com.basho.riak.protobuf
    RiakDtPB$DtFetchReq
    RiakDtPB$DtFetchResp]))

(set! *warn-on-reflection* true)

(def DtFetchReq
  (pb/protodef RiakDtPB$DtFetchReq))

(defn DtFetchReq->bytes
  [m]
  (pb/protobuf-dump
   (pb/protobuf DtFetchReq
                m)))

(def DtFetchResp
  (pb/protodef RiakDtPB$DtFetchResp))

(defn bytes->DtFetchResp
  [^bytes x]
  (pb/protobuf-load DtFetchResp x))
