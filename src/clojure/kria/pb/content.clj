(ns kria.pb.content
  "Functions for Riak content (e.g. values stored with object keys)"
  (:require
    [kria.conversions :refer
     [byte-string?
      byte-string<-utf8-string
      utf8-string<-byte-string]]
    [kria.pb.link :refer [Link->pb pb->Link]]
    [kria.pb.pair :refer [Pair->pb pb->Pair]])
  (:import
    [com.basho.riak.protobuf RiakKvPB$RpbContent]))

(defrecord Content
  [value            ; required bytes
   content-type     ; optional bytes
   charset          ; optional bytes
   content-encoding ; optional bytes
   vtag             ; optional bytes
   links            ; repeated RpbLink
   last-mod         ; optional uint32
   last-mod-usecs   ; optional uint32
   usermeta         ; repeated RpbPair
   indexes          ; repeated RpbPair
   deleted          ; optional bool
   ])

(defn ^RiakKvPB$RpbContent Content->pb
  "Returns a Riak content protobuf from a Content record."
  [r]
  (let [b (RiakKvPB$RpbContent/newBuilder)]
    (let [x (:value r)]
      (.setValue b x))
    (if-let [x (:content-type r)]
      (.setContentType b (byte-string<-utf8-string x)))
    (if-let [x (:charset r)]
      (.setCharset b (byte-string<-utf8-string x)))
    (if-let [x (:content-encoding r)]
      (.setContentEncoding b (byte-string<-utf8-string x)))
    (if-let [x (:vtag r)]
      (.setVtag b (byte-string<-utf8-string x)))
    (if-let [x (:links r)]
      (.addAllLinks b (map Link->pb x)))
    (if-let [x (:last-mod r)]
      (.setLastMod b x))
    (if-let [x (:last-mod-usecs r)]
      (.setLastModUsecs b x))
    (if-let [x (:usermeta r)]
      (.addAllUsermeta b (map Pair->pb x)))
    (if-let [x (:indexes r)]
      (.addAllIndexes b (map Pair->pb x)))
    (if-let [x (:deleted r)]
      (.setDeleted b x))
    (.build b)))

(defn pb->Content
  [^RiakKvPB$RpbContent pb]
  (->Content
    (.getValue pb)
    (some-> (.getContentType pb) utf8-string<-byte-string)
    (some-> (.getCharset pb) utf8-string<-byte-string)
    (some-> (.getContentEncoding pb) utf8-string<-byte-string)
    (some-> (.getVtag pb) utf8-string<-byte-string)
    (mapv pb->Link (.getLinksList pb))
    (.getLastMod pb)
    (.getLastModUsecs pb)
    (mapv pb->Pair (.getUsermetaList pb))
    (mapv pb->Pair (.getIndexesList pb))
    (.getDeleted pb)))
