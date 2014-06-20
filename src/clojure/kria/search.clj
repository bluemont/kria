(ns kria.search
  (:require
    [kria.conversions :refer [byte-string?]]
    [kria.core :refer [call]]
    [kria.pb.search.search :refer [SearchReq->bytes bytes->SearchResp]]))

(set! *warn-on-reflection* true)

(defn search
  "Searches index `idx` with query `q`."
  [asc idx q opts cb]
  {:pre [(string? idx) (byte-string? q)]}
  (call asc cb :search-query-req :search-query-resp
        SearchReq->bytes bytes->SearchResp
        (merge opts {:q q :index idx})))
