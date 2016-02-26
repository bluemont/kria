(ns kria.data-type
  (:refer-clojure :exclude [get set])
  (:require
   [kria.conversions :refer [byte-string?]]
   [kria.core :refer [call]]))

(set! *warn-on-reflection* true)

(defn set
  "Update a data-typed key in a bucket with an operation."
  [asc b t k op opts cb]
  {:pre [(byte-string? b) (byte-string? t) (byte-string? k)]}
  (call asc cb :dt-update-req :dt-update-resp
        (merge opts {:bucket b :key k :type t
                     :op op})))

(defn get
  "Get a data-typed key from a bucket."
  [asc b t k opts cb]
  {:pre [(byte-string? b) (byte-string? t) (byte-string? k)]}
  (call asc cb :dt-fetch-req :dt-fetch-resp
        (merge opts {:bucket b :key k :type t})))
