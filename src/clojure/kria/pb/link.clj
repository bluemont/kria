(ns kria.pb.link
  (:import
   [com.basho.riak.protobuf
    RiakKvPB$RpbLink]))

(defrecord Link
           [bucket ; optional bytes
            key    ; optional bytes
            tag    ; optional bytes
            ])

(defn pb->Link
  [^RiakKvPB$RpbLink pb]
  (->Link
   (.getBucket pb)
   (.getKey pb)
   (.getTag pb)))

(defn ^RiakKvPB$RpbLink Link->pb
  [m]
  (let [b (RiakKvPB$RpbLink/newBuilder)]
    (if-let [x (:bucket m)]
      (.setBucket b x))
    (if-let [x (:key m)]
      (.setKey b x))
    (if-let [x (:tag m)]
      (.setTag b x))
    (.build b)))
