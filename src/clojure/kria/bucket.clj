(ns kria.bucket
  (:refer-clojure :exclude [get set list])
  (:require
   [kria.conversions :refer [byte-string?]]
   [kria.core :refer [call]]
   [kria.pb.bucket.get :refer [GetBucketReq->bytes bytes->GetBucketResp]]
   [kria.pb.bucket.set :refer [SetBucketReq->bytes]]
   [kria.pb.bucket.list-keys :refer [ListKeysReq->bytes bytes->ListKeysResp]]))

(set! *warn-on-reflection* true)

(defn get
  "Get bucket properties."
  [asc b cb]
  {:pre [(byte-string? b)]}
  (call asc cb :get-bucket-req :get-bucket-resp
        GetBucketReq->bytes bytes->GetBucketResp
        (merge {} {:bucket b})))

(defn set
  "Set bucket properties."
  [asc b opts cb]
  {:pre [(byte-string? b)]}
  (call asc cb :set-bucket-req :set-bucket-resp
        SetBucketReq->bytes (fn [_] true)
        (merge opts {:bucket b})))

(defn list
  "List keys in bucket. Results are passed to stream-cb as the keys
  are streamed back."
  [asc b opts cb stream-cb]
  {:pre [(byte-string? b)]}
  (call asc cb :list-keys-req :list-keys-resp
        ListKeysReq->bytes bytes->ListKeysResp
        (merge opts {:bucket b})
        true :keys :done stream-cb))
