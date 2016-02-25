(ns kria.pb.object.delete
  (:require
   [flatland.protobuf.core :as pb])
  (:import
   [com.basho.riak.protobuf RiakKvPB$RpbDelReq]))

(def DeleteReq (pb/protodef RiakKvPB$RpbDelReq))

(defn DeleteReq->bytes
  [m]
  (pb/protobuf-dump
   (pb/protobuf DeleteReq
                m)))
