(ns kria.search
  (:require
    [kria.conversions :refer [byte-string?]]
    [kria.core :refer [call]]
    [kria.pb.search.search :refer [SearchReq->bytes bytes->SearchResp]]))

(set! *warn-on-reflection* true)

(defn search
  "Searches index with a query."
  [asc q idx opts cb]
  {:pre [(byte-string? q) (string? idx)]}
  (call asc cb :search-query-req :search-query-resp
        SearchReq->bytes bytes->SearchResp
        (merge opts {:q q :index idx})))
