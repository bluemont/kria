(ns kria.bucket.type
  (:refer-clojure :exclude [get set])
  (:require
   [kria.conversions :refer [byte-string?]]
   [kria.core :refer [call]]))

(set! *warn-on-reflection* true)

(defn get
  "Get bucket type properties."
  [asc t cb]
  {:pre [(byte-string? t)]}
  (call asc cb :get-bucket-type-req :get-bucket-resp
        (merge {} {:type t})))

(defn set
  "Set bucket type properties."
  [asc t opts cb]
  {:pre [(byte-string? t)]}
  (call asc cb :set-bucket-type-req :set-bucket-resp
        (merge opts {:type t})))
