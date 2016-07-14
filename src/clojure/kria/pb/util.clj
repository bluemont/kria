(ns kria.pb.util
  (:require [flatland.protobuf.core :as pb])
  (:import [flatland.protobuf
            PersistentProtocolBufferMap
            PersistentProtocolBufferMap$Def]))

(defn ^bytes Req->bytes
  [^PersistentProtocolBufferMap$Def pb-def m]
  (pb/protobuf-dump
   (pb/protobuf pb-def m)))

(defn ^PersistentProtocolBufferMap bytes->Resp
  [^PersistentProtocolBufferMap$Def pb-def ^bytes x]
  (pb/protobuf-load pb-def x))
