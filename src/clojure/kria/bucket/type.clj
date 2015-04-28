(ns kria.bucket.type
  (:refer-clojure :exclude [get set])
  (:require
   [kria.conversions :refer [byte-string?]]
   [kria.core :refer [call]]
   [kria.pb.bucket.type.get :refer [GetBucketTypeReq->bytes
                                    bytes->GetBucketTypeResp]]
   [kria.pb.bucket.type.set :refer [SetBucketTypeReq->bytes]]))

(set! *warn-on-reflection* true)

(defn get
  "Get bucket type properties."
  [asc t cb]
  {:pre [(byte-string? t)]}
  (call asc cb :get-bucket-type-req :get-bucket-resp
        GetBucketTypeReq->bytes bytes->GetBucketTypeResp
        (merge {} {:type t})))

(defn set
  "Set bucket type properties."
  [asc t opts cb]
  {:pre [(byte-string? t)]}
  (call asc cb :set-bucket-type-req :set-bucket-resp
        SetBucketTypeReq->bytes (fn [_] true)
        (merge opts {:type t})))
