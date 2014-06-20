(ns kria.search
  (:require
    [kria.conversions :refer [byte-string?]]
    [kria.core :refer [call]]
    [kria.pb.search.search :refer [SearchReq->bytes bytes->SearchResp]]))

(set! *warn-on-reflection* true)

(defn search
  "Searches index `idx` with query `q`. When the callback runs, it
  will receive [asc e a]. The last argument, the attachment, will
  take the following form:

   {:docs
    [{:fields
      [{:key \"score\", :value \"1.00000000000000000000e+00\"}
       {:key \"_yz_rb\", :value \"B-504\"}
       {:key \"_yz_rt\", :value \"default\"}
       {:key \"_yz_rk\", :value \"Key-1\"}
       {:key \"_yz_id\", :value \"default_B-504_Key-1_23\"}
       {:key \"word_ss\", :value \"ocean\"}]}
     {:fields
      [{:key \"score\", :value \"1.00000000000000000000e+00\"}
       {:key \"_yz_rb\", :value \"B-504\"}
       {:key \"_yz_rt\", :value \"default\"}
       {:key \"_yz_rk\", :value \"Key-2\"}
       {:key \"_yz_id\", :value \"default_B-504_Key-2_2\"}
       {:key \"word_ss\", :value \"stream\"}]}
     {:fields
      [{:key \"score\", :value \"1.00000000000000000000e+00\"}
       {:key \"_yz_rb\", :value \"B-504\"}
       {:key \"_yz_rt\", :value \"default\"}
       {:key \"_yz_rk\", :value \"Key-3\"}
       {:key \"_yz_id\", :value \"default_B-504_Key-3_2\"}
       {:key \"word_ss\", :value \"river\"}]}],
    :max-score 1.0,
    :num-found 3}]"
  [asc idx q opts cb]
  {:pre [(string? idx) (byte-string? q)]}
  (call asc cb :search-query-req :search-query-resp
        SearchReq->bytes bytes->SearchResp
        (merge opts {:q q :index idx})))
