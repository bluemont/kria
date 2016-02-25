(ns kria.pb.map-reduce
  (:require
   [flatland.protobuf.core :as pb])
  (:import
   [com.basho.riak.protobuf
    RiakKvPB$RpbMapRedReq
    RiakKvPB$RpbMapRedResp]))

(set! *warn-on-reflection* true)



(def MapRedReq
  (pb/protodef RiakKvPB$RpbMapRedReq))

(defn MapRedReq->bytes
  [m]
  (pb/protobuf-dump
   (pb/protobuf MapRedReq
                m)))

(def MapRedResp
  (pb/protodef RiakKvPB$RpbMapRedResp))

(defn bytes->MapRedResp
  [^bytes x]
  (pb/protobuf-load MapRedResp x))
