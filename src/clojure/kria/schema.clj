(ns kria.schema
  (:refer-clojure :exclude [get])
  (:require
    [kria.conversions :refer [byte-string?]]
    [kria.core :refer [call]]
    [kria.pb.schema.get :refer [SchemaGetReq->bytes bytes->SchemaGetResp]]
    [kria.pb.schema.put :refer [SchemaPutReq->bytes]]))

(set! *warn-on-reflection* true)

(defn get
  "Gets a schema."
  [asc name cb]
  {:pre [(string? name)]}
  (call asc cb :yz-schema-get-req :yz-schema-get-resp
        SchemaGetReq->bytes bytes->SchemaGetResp
        {:name name}))

(defn put
  "Stores or updates a schema."
  [asc name content cb]
  {:pre [(string? name) (string? content)]}
  (call asc cb :yz-schema-put-req :yz-schema-put-resp
        SchemaPutReq->bytes (fn [_] true)
        {:schema {:name name :content content}}))
