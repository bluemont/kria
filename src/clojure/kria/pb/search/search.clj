(ns kria.pb.search.search
  (:require
   [flatland.protobuf.core :as pb])
  (:import
   [com.basho.riak.protobuf
    RiakSearchPB$RpbSearchQueryReq
    RiakSearchPB$RpbSearchQueryResp]))

(def SearchReq
  (pb/protodef RiakSearchPB$RpbSearchQueryReq))

(defn SearchReq->bytes
  [m]
  (pb/protobuf-dump (pb/protobuf SearchReq
                                 m)))

(def SearchResp
  (pb/protodef RiakSearchPB$RpbSearchQueryResp))

(defn bytes->SearchResp
  [^bytes x]
  (pb/protobuf-load SearchResp x))
