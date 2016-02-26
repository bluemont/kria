(ns kria.schema
  (:refer-clojure :exclude [get])
  (:require
   [kria.conversions :refer [byte-string?]]
   [kria.core :refer [call]]))

(set! *warn-on-reflection* true)

(defn get
  "Gets a schema."
  [asc name cb]
  {:pre [(byte-string? name)]}
  (call asc cb :yz-schema-get-req :yz-schema-get-resp
        {:name name}))

(defn put
  "Stores or updates a schema."
  [asc name content cb]
  {:pre [(byte-string? name) (byte-string? content)]}
  (call asc cb :yz-schema-put-req :yz-schema-put-resp
        {:schema {:name name :content content}}))
