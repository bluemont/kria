(ns kria.server
  (:require
   [kria.core :refer [call]]
   [kria.pb.server.info :refer [bytes->InfoResp]]))

(defn ping
  "Pings the server."
  [asc cb]
  (call asc cb :ping-req :ping-resp
        (fn [_] (byte-array 0)) (fn [_] true)
        {}))

(defn info
  "Get server information."
  [asc cb]
  (call asc cb :get-server-info-req :get-server-info-resp
        (fn [_] (byte-array 0)) bytes->InfoResp
        {}))
