(ns kria.index
  (:refer-clojure :exclude [get])
  (:require
    [kria.conversions :refer [byte-string?]]
    [kria.core :refer [call]]
    [kria.pb.index.delete :refer [IndexDeleteReq->bytes]]
    [kria.pb.index.get :refer [IndexGetReq->bytes bytes->IndexGetResp]]
    [kria.pb.index.put :refer [IndexPutReq->bytes]]))

(set! *warn-on-reflection* true)

(defn delete
  "Stores or updates an index."
  [asc name cb]
  {:pre [(string? name)]}
  (call asc cb :yz-index-delete-req :yz-index-delete-resp
        IndexDeleteReq->bytes (fn [_] true)
        {:name name}))

(defn get
  "Gets an index."
  [asc name cb]
  {:pre [(string? name)]}
  (call asc cb :yz-index-get-req :yz-index-get-resp
        IndexGetReq->bytes bytes->IndexGetResp
        {:name name}))

(defn put
  "Stores or updates an index."
  [asc name opts cb]
  {:pre [(string? name)]}
  (call asc cb :yz-index-put-req :yz-index-put-resp
        IndexPutReq->bytes (fn [_] true)
        (assoc-in opts [:index :name] name)))

(defn get-poll
  "Polls `get` up to `k` times. Starts by waiting `delay` milli-
  seconds and increases the delay by 20% on each iteration. If not
  found, returns false. If found, returns the total time used."
  [asc name delay k]
  (loop [i 0 d delay t 0]
    (if (>= i k)
      false
      (let [p (promise)
            cb (fn [asc e a] (deliver p [asc e a]))]
        (get asc name cb)
        (let [[asc e a] @p]
          (if (= name (-> a :index first :name))
            t
            (do
              (Thread/sleep d)
              (let [d' (long (* d 1.2))]
                (recur (inc i) d' (+ t d'))))))))))
