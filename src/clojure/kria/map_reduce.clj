(ns kria.map-reduce
  (:require
   [kria.conversions :refer [byte-string? byte-string<-utf8-string]]
   [kria.core :refer [call]]
   [kria.pb.map-reduce :refer [MapRedReq->bytes bytes->MapRedResp]]))

(set! *warn-on-reflection* true)

(defn map-reduce
  "Launch a map/reduce job (json request/response)."
  [asc request cb stream-cb]
  {:pre [(byte-string? request)]}
  (call asc cb :map-red-req :map-red-resp
        MapRedReq->bytes bytes->MapRedResp
        {:request request
         :content-type (byte-string<-utf8-string "application/json")}
        true :response :done stream-cb))
