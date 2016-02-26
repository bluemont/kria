(ns kria.server
  (:require
   [kria.core :refer [call]]))

(defn ping
  "Pings the server."
  [asc cb]
  (call asc cb :ping-req :ping-resp
        {}))

(defn info
  "Get server information."
  [asc cb]
  (call asc cb :get-server-info-req :get-server-info-resp
        {}))
