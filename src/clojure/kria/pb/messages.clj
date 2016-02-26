(ns kria.pb.messages
  (:require
   [kria.pb.defs :refer [message-defs]]
   [kria.pb.util :as putil]))

(def message-writers
  (into {}
        (for [[k d] message-defs]
          [k (partial putil/Req->bytes d)])))

(defn get-message-writer
  "Returns a message writer for the given key, or a function
  that returns an empty byte array."
  [msg-k]
  (msg-k message-writers
         ;; for messages with no payload
         (constantly
          (byte-array 0))))

(def message-readers
  (into {}
        (for [[k d] message-defs]
          [k (partial putil/bytes->Resp d)])))

(defn get-message-reader
  "Returns a message reader for the given key, or a function
  that returns true"
  [msg-k]
  (msg-k message-readers
         (constantly true)))
