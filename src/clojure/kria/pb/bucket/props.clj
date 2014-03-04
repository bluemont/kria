(ns kria.pb.bucket.props
  (:require
    [kria.conversions :refer [utf8-string<-byte-string
                              byte-string<-utf8-string]]
    [kria.pb.commit-hook :refer [CommitHook->pb pb->CommitHook]]
    [kria.pb.mod-fun :refer [ModFun->pb pb->ModFun]])
  (:import
    [com.basho.riak.protobuf RiakPB$RpbBucketProps]))

(set! *warn-on-reflection* true)

(defrecord BucketProps
  [
   n-val           ; optional uint32
   allow-mult      ; optional bool
   last-write-wins ; optional bool
   precommit       ; repeated RpbCommitHook
   has-precommit   ; optional bool
   postcommit      ; repeated RpbCommitHook
   has-postcommit  ; optional bool
   chash-keyfun    ; optional RpbModFun
   linkfun         ; optional RpbModFun
   old-vclock      ; optional uint32
   young-vclock    ; optional uint32
   big-vclock      ; optional uint32
   small-vclock    ; optional uint32
   pr              ; optional uint32
   r               ; optional uint32
   w               ; optional uint32
   pw              ; optional uint32
   dw              ; optional uint32
   rw              ; optional uint32
   basic-quorum    ; optional bool
   not-found-ok    ; optional bool
   backend         ; optional bytes
   search          ; optional bool
   repl            ; optional RpbReplMode
   search-index    ; optional bytes
   datatype        ; optional bytes
   ])

(defn ^RiakPB$RpbBucketProps BucketProps->pb
  [m]
  (let [b (RiakPB$RpbBucketProps/newBuilder)]
    (if-let [x (:n-val m)]
      (.setNVal b x))
    (if-let [x (:allow-mult m)]
      (.setAllowMult b x))
    (if-let [x (:last-write-wins m)]
      (.setLastWriteWins b x))
    (if-let [x (:precommit m)]
      (.addAllPrecommit b (map CommitHook->pb x)))
    (if-let [x (:has-precommit m)]
      (.setHasPrecommit b x))
    (if-let [x (:postcommit m)]
      (.addAllPostcommit b (map CommitHook->pb x)))
    (if-let [x (:has-postcommit m)]
      (.setHasPostcommit b x))
    (if-let [x (:chash-keyfun m)]
      (.setChashKeyfun b (ModFun->pb x)))
    (if-let [x (:linkfun m)]
      (.setLinkfun b (ModFun->pb x)))
    (if-let [x (:old-vclock m)]
      (.setOldVclock b x))
    (if-let [x (:young-vclock m)]
      (.setYoungVclock b x))
    (if-let [x (:big-vclock m)]
      (.setBigVclock b x))
    (if-let [x (:small-vclock m)]
      (.setSmallVclock b x))
    (if-let [x (:pr m)]
      (.setPr b x))
    (if-let [x (:r m)]
      (.setR b x))
    (if-let [x (:w m)]
      (.setW b x))
    (if-let [x (:pw m)]
      (.setPw b x))
    (if-let [x (:dw m)]
      (.setDw b x))
    (if-let [x (:rw m)]
      (.setRw b x))
    (if-let [x (:basic-quorum m)]
      (.setBasicQuorum b x))
    (if-let [x (:not-found-ok m)]
      (.setNotfoundOk b x))
    (if-let [x (:backend m)]
      (.setBackend b (byte-string<-utf8-string x)))
    (if-let [x (:search m)]
      (.setSearch b x))
    (if-let [x (:repl m)]
      (.setRepl b x))
    (if-let [x (:search-index m)]
      (.setSearchIndex b (byte-string<-utf8-string x)))
    (if-let [x (:datatype m)]
      (.setDatatype b (byte-string<-utf8-string x)))
    (.build b)))

(defn pb->BucketProps
  [^RiakPB$RpbBucketProps pb]
  (->BucketProps
    (.getNVal pb)
    (.getAllowMult pb)
    (.getLastWriteWins pb)
    (mapv pb->CommitHook (.getPrecommitList pb))
    (.getHasPrecommit pb)
    (mapv pb->CommitHook (.getPostcommitList pb))
    (.getHasPostcommit pb)
    (pb->ModFun (.getChashKeyfun pb))
    (pb->ModFun (.getLinkfun pb))
    (.getOldVclock pb)
    (.getYoungVclock pb)
    (.getBigVclock pb)
    (.getSmallVclock pb)
    (.getPr pb)
    (.getR pb)
    (.getW pb)
    (.getPw pb)
    (.getDw pb)
    (.getRw pb)
    (.getBasicQuorum pb)
    (.getNotfoundOk pb)
    (some-> (.getBackend pb) utf8-string<-byte-string)
    (.getSearch pb)
    (.getRepl pb)
    (some-> (.getSearchIndex pb) utf8-string<-byte-string)
    (some-> (.getDatatype pb) utf8-string<-byte-string)))
