(ns kria.pb.error
  (:require
   [flatland.protobuf.core :as pb])
  (:import
   [com.basho.riak.protobuf RiakPB$RpbErrorResp]))

(def ErrorResp
  (pb/protodef RiakPB$RpbErrorResp))

(defn bytes->ErrorResp
  [^bytes x]
  (pb/protobuf-load ErrorResp x))
