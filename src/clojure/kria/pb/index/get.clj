(ns kria.pb.index.get
  (:require
   [kria.conversions :refer [utf8-string<-byte-string
                             byte-string<-utf8-string]]
   [kria.pb.index.index :refer [pb->Index]])
  (:import
   [com.basho.riak.protobuf
    RiakYokozunaPB$RpbYokozunaIndexGetReq
    RiakYokozunaPB$RpbYokozunaIndexGetResp]))

(set! *warn-on-reflection* true)

(defrecord IndexGetReq
           [name ; optional bytes
            ])

(defn ^RiakYokozunaPB$RpbYokozunaIndexGetReq IndexGetReq->pb
  [m]
  (let [b (RiakYokozunaPB$RpbYokozunaIndexGetReq/newBuilder)]
    (if-let [x (:name m)]
      (.setName b (byte-string<-utf8-string x)))
    (.build b)))

(defn IndexGetReq->bytes
  [m]
  (.toByteArray (IndexGetReq->pb m)))

(defrecord IndexGetResp
           [index ; repeated RpbYokozunaIndex
            ])

(defn pb->IndexGetResp
  [^RiakYokozunaPB$RpbYokozunaIndexGetResp pb]
  (->IndexGetResp
   (mapv pb->Index (.getIndexList pb))))

(defn bytes->IndexGetResp
  [^bytes x]
  (pb->IndexGetResp
   (RiakYokozunaPB$RpbYokozunaIndexGetResp/parseFrom x)))
