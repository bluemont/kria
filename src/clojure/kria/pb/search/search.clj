(ns kria.pb.search.search
  (:require
   [kria.conversions :refer [utf8-string<-byte-string
                             byte-string<-utf8-string]]
   [kria.pb.search.doc :refer [pb->SearchDoc]])
  (:import
   [com.basho.riak.protobuf
    RiakSearchPB$RpbSearchQueryReq
    RiakSearchPB$RpbSearchQueryResp]))

(defrecord SearchReq
           [q       ; required bytes
            index   ; required bytes
            rows    ; optional uint32
            start   ; optional uint32
            sort    ; optional bytes
            filter  ; optional bytes
            df      ; optional bytes
            op      ; optional bytes
            fl      ; repeated bytes
            presort ; optional bytes
            ])

(defn ^RiakSearchPB$RpbSearchQueryReq SearchReq->pb
  [m]
  (let [b (RiakSearchPB$RpbSearchQueryReq/newBuilder)]
    (let [x (:q m)]
      (.setQ b x))
    (let [x (:index m)]
      (.setIndex b (byte-string<-utf8-string x)))
    (if-let [x (:rows m)]
      (.setRows b x))
    (if-let [x (:start m)]
      (.setStart b x))
    (if-let [x (:sort m)]
      (.setSort b x))
    (if-let [x (:filter m)]
      (.setFilter b x))
    (if-let [x (:df m)]
      (.setDf b x))
    (if-let [x (:op m)]
      (.setOp b x))
    (if-let [x (:fl m)]
      (.addAllFl b (map byte-string<-utf8-string x)))
    (if-let [x (:presort m)]
      (.setPresort b x))
    (.build b)))

(defn SearchReq->bytes
  [m]
  (.toByteArray (SearchReq->pb m)))

(defrecord SearchResp
           [docs      ; repeated RpbSearchDoc
            max-score ; optional float
            num-found ; optional uint32
            ])

(defn pb->SearchResp
  [^RiakSearchPB$RpbSearchQueryResp pb]
  (->SearchResp
   (mapv pb->SearchDoc (.getDocsList pb))
   (.getMaxScore pb)
   (.getNumFound pb)))

(defn bytes->SearchResp
  [^bytes x]
  (pb->SearchResp
   (RiakSearchPB$RpbSearchQueryResp/parseFrom x)))
