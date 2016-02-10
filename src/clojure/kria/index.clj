(ns kria.index
  (:refer-clojure :exclude [get])
  (:require
   [kria.conversions :refer [byte-string?]]
   [kria.core :refer [call]]
   [kria.pb.index.yz.delete :refer [IndexDeleteReq->bytes]]
   [kria.pb.index.yz.get :refer [IndexGetReq->bytes bytes->IndexGetResp]]
   [kria.pb.index.yz.put :refer [IndexPutReq->bytes]]))

(set! *warn-on-reflection* true)

(defn delete
  "Stores or updates an index."
  [asc name cb]
  {:pre [(string? name)]}
  (call asc cb :yz-index-delete-req :yz-index-delete-resp
        IndexDeleteReq->bytes (fn [_] true)
        {:name name}))

(defn get
  "Gets an index."
  [asc name cb]
  {:pre [(string? name)]}
  (call asc cb :yz-index-get-req :yz-index-get-resp
        IndexGetReq->bytes bytes->IndexGetResp
        {:name name}))

(defn put
  "Stores or updates an index."
  [asc name opts cb]
  {:pre [(string? name)]}
  (call asc cb :yz-index-put-req :yz-index-put-resp
        IndexPutReq->bytes (fn [_] true)
        (assoc-in opts [:index :name] name)))
