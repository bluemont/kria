(ns kria.pb.search.doc
  (:require
   [kria.pb.pair :refer [Pair->pb pb->Pair]])
  (:import
   [com.basho.riak.protobuf
    RiakSearchPB$RpbSearchDoc]))

(set! *warn-on-reflection* true)

(defrecord SearchDoc
           [fields ; repeated RpbPair
            ])

(defn ^RiakSearchPB$RpbSearchDoc SearchDoc->pb
  [m]
  (let [b (RiakSearchPB$RpbSearchDoc/newBuilder)]
    (let [x (:fields m)]
      (.addAllFields b (map Pair->pb x)))
    (.build b)))

(defn pb->SearchDoc
  [^RiakSearchPB$RpbSearchDoc pb]
  (->SearchDoc
   (mapv pb->Pair (.getFieldsList pb))))
