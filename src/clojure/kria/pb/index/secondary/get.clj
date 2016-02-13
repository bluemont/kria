(ns kria.pb.index.secondary.get
  (:require
   [kria.conversions :refer [utf8-string<-byte-string
                             byte-string<-utf8-string]]
   [kria.pb.pair :refer [pb->Pair Pair->pb]])
  (:import
   [com.basho.riak.protobuf
    RiakKvPB$RpbIndexReq
    RiakKvPB$RpbIndexReq$IndexQueryType
    RiakKvPB$RpbIndexResp]))

(defrecord IndexReq
    [bucket          ; required bytes
     index           ; required bytes
     qtype           ; enum eq = 0, range = 1
     key             ; optional bytes see note at https://github.com/basho/riak_pb/blob/develop/src/riak_kv.proto#L173
     range-min       ; optional bytes
     range-max       ; optional bytes
     return-terms    ; optional bool
     stream          ; optional bool
     max-results     ; optional uint32
     continuation    ; optional bytes
     timeout         ; optional uint32
     type            ; optional bytes
     term-regex      ; optional bytes
     pagination-sort ; optional bool
     ])

(defn ^RiakKvPB$RpbIndexReq IndexReq->pb
  [m]
  (let [{:keys [bucket
                index
                qtype
                key
                range-min
                range-max
                return-terms
                stream
                max-results
                continuation
                timeout
                type
                term-regex
                pagination-sort]} m
        b (RiakKvPB$RpbIndexReq/newBuilder)]
    (when bucket
      (.setBucket b bucket))
    (when index
      (.setIndex b index))
    (when qtype
      (.setQtype b (case qtype
                     :eq (RiakKvPB$RpbIndexReq$IndexQueryType/eq)
                     :range (RiakKvPB$RpbIndexReq$IndexQueryType/range)
                     (RiakKvPB$RpbIndexReq$IndexQueryType/eq))))
    (when key
      (.setKey b key))
    (when range-min
      (.setRangeMin b range-min))
    (when range-max
      (.setRangeMax b range-max))
    (when return-terms
      (.setReturnTerms b return-terms))
    (when stream
      (.setStream b stream))
    (when max-results
      (.setMaxResults b max-results))
    (when continuation
      (.setContinuation b continuation))
    (when timeout
      (.setTimeout b timeout))
    (when type
      (.setType b type))
    (when term-regex
      (.setTermRegex b term-regex))
    (when pagination-sort
      (.setPaginationSort b pagination-sort))
    (.build b)))

(defn IndexReq->bytes
  [m]
  (.toByteArray (IndexReq->pb m)))

(defrecord IndexResp
    [keys         ; repeated bytes
     results      ; repeated pair
     continuation ; optional bytes
     done         ; optional bool
     ])

(defn pb->IndexResp
  [^RiakKvPB$RpbIndexResp pb]
  (->IndexResp
   (mapv utf8-string<-byte-string (.getKeysList pb))
   (mapv pb->Pair (.getResultsList pb))
   (.getContinuation pb)
   (.getDone pb)))

(defn bytes->IndexResp
  [^bytes x]
  (pb->IndexResp
   (RiakKvPB$RpbIndexResp/parseFrom x)))
