(ns kria.index
  (:refer-clojure :exclude [get])
  (:require
   [kria.conversions :refer [byte-string?]]
   [kria.core :refer [call]]
   [kria.pb.index.yz.delete :refer [IndexDeleteReq->bytes]]
   [kria.pb.index.yz.get :refer [IndexGetReq->bytes bytes->IndexGetResp]]
   [kria.pb.index.yz.put :refer [IndexPutReq->bytes]]
   [kria.pb.index.secondary.get :refer [IndexReq->bytes bytes->IndexResp]]))

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
  {:pre [(byte-string? name)]}
  (call asc cb :yz-index-get-req :yz-index-get-resp
        IndexGetReq->bytes bytes->IndexGetResp
        {:name name}))

(defn put
  "Stores or updates an index."
  [asc name opts cb]
  {:pre [(byte-string? name)]}
  (call asc cb :yz-index-put-req :yz-index-put-resp
        IndexPutReq->bytes (fn [_] true)
        (assoc-in opts [:index :name] name)))


(defn get-2i
  "Gets a secondary index by equality (first arity) or range (second arity)"
  ([asc b k v opts cb]
   {:pre [(byte-string? b) (byte-string? k) (byte-string? v) (map? opts)]}
   (call asc cb :index-req :index-resp
         IndexReq->bytes bytes->IndexResp
         (merge opts
                {:bucket b
                 :index k
                 :key v
                 :qtype :eq})))
  ([asc b k v1 v2 opts cb]
   {:pre [(byte-string? b) (byte-string? k) (byte-string? v1) (byte-string? v2) (map? opts)]}
   (call asc cb :index-req :index-resp
         IndexReq->bytes bytes->IndexResp
         (merge opts
                {:bucket b
                 :index k
                 :range-min v1
                 :range-max v2
                 :qtype :range}))))
