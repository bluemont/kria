(ns kria.object
  (:refer-clojure :exclude [get])
  (:require
   [kria.conversions :refer [byte-string?]]
   [kria.core :refer [call]]
   [kria.pb.object.delete :refer [DeleteReq->bytes]]
   [kria.pb.object.get :refer [GetReq->bytes bytes->GetResp]]
   [kria.pb.object.put :refer [PutReq->bytes bytes->PutResp]]))

(set! *warn-on-reflection* true)

(defn delete
  "Deletes a key in a bucket."
  [asc b k opts cb]
  {:pre [(byte-string? b) (byte-string? k)]}
  (call asc cb :del-req :del-resp
        DeleteReq->bytes (fn [_] true)
        (merge opts {:bucket b :key k})))

(defn get
  "Reads an object from a key in a bucket."
  [asc b k opts cb]
  {:pre [(byte-string? b) (byte-string? k)]}
  (call asc cb :get-req :get-resp
        GetReq->bytes bytes->GetResp
        (merge opts {:bucket b :key k})))

(defn put
  "Stores or updates a value for a key in a bucket."
  [asc b k v opts cb]
  {:pre [(byte-string? b) (byte-string? k) (map? v)]}
  (call asc cb :put-req :put-resp
        PutReq->bytes bytes->PutResp
        (merge opts {:bucket b :key k :content v})))
