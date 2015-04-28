(ns kria.pb.error
  (:require
   [kria.conversions
    :refer [byte-string<-utf8-string
            utf8-string<-byte-string]])
  (:import
   [com.basho.riak.protobuf RiakPB$RpbErrorResp]))

(defrecord ErrorResp
           [message ; required bytes
            code    ; required uint32
            ])

(defn pb->ErrorResp
  [^RiakPB$RpbErrorResp pb]
  (->ErrorResp
   (some-> (.getErrmsg pb) utf8-string<-byte-string)
   (.getErrcode pb)))

(defn bytes->ErrorResp
  [^bytes x]
  (pb->ErrorResp
   (RiakPB$RpbErrorResp/parseFrom x)))
