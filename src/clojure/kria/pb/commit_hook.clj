(ns kria.pb.commit-hook
  (:require
    [kria.conversions :refer [utf8-string<-byte-string
                              byte-string<-utf8-string]]
    [kria.pb.mod-fun :refer [ModFun->pb pb->ModFun]])
  (:import
    [com.basho.riak.protobuf
     RiakPB$RpbCommitHook]))

(set! *warn-on-reflection* true)

(defrecord CommitHook
  [modfun ; optional RpbModFun
   name   ; optional bytes
   ])

(defn ^RiakPB$RpbCommitHook CommitHook->pb
  [m]
  (let [b (RiakPB$RpbCommitHook/newBuilder)]
    (if-let [x (:modfun m)]
      (.setModfun b (ModFun->pb x)))
    (if-let [x (:name m)]
      (.setName b (byte-string<-utf8-string x)))
    (.build b)))

(defn pb->CommitHook
  [^RiakPB$RpbCommitHook pb]
  (->CommitHook
    (pb->ModFun (.getModfun pb))
    (some-> (.getName pb) utf8-string<-byte-string)))
