(ns kria.pb.map-reduce
  (:require
   [kria.conversions
    :refer [byte-string<-utf8-string
            utf8-string<-byte-string]])
  (:import
   [com.basho.riak.protobuf
    RiakKvPB$RpbMapRedReq
    RiakKvPB$RpbMapRedResp]))

(set! *warn-on-reflection* true)

(defrecord RbpMapRedReq
    [request      ; required bytes
     content-type ; required bytes
     ])

(defn ^RiakKvPB$RpbMapRedReq RbpMapRedReq->pb
  [m]
  (let [b (RiakKvPB$RpbMapRedReq/newBuilder)]
    (let [x (:request m)]
      (.setRequest b x))
    (let [x (:content-type m)]
      (.setContentType b x))
    (.build b)))

(defn RbpMapRedReq->bytes
  [m]
  (.toByteArray (RbpMapRedReq->pb m)))

(defrecord RbpMapRedResp
    [phase    ; optional uint32
     response ; optional bytes
     done     ; optional bool
     ])

(defn ^RiakKvPB$RpbMapRedResp pb->RbpMapRedResp
  [^RiakKvPB$RpbMapRedResp pb]
  (->RbpMapRedResp
   (.getPhase pb)
   (.getResponse pb)
   (.getDone pb)))

(defn bytes->RbpMapRedResp
  [^bytes x]
  (pb->RbpMapRedResp (RiakKvPB$RpbMapRedResp/parseFrom x)))
