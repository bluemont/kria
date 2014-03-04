(ns kria.bucket
  (:refer-clojure :exclude [get set])
  (:require
    [kria.conversions :refer [byte-string?]]
    [kria.core :refer [call]]
    [kria.pb.bucket.get :refer [GetBucketReq->bytes bytes->GetBucketResp]]
    [kria.pb.bucket.set :refer [SetBucketReq->bytes]]
    [kria.core :refer [handler header-cb-fn parse-fn payload-message]]))

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
