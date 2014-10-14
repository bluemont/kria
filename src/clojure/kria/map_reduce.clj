(ns kria.map-reduce
  (:require
   [kria.conversions :refer [byte-string?
                             byte-string<-utf8-string]]
   [kria.core :refer [call]]
   [kria.pb.map-reduce :refer [RbpMapRedReq->bytes bytes->RbpMapRedResp]]))

(set! *warn-on-reflection* true)

(defn map-reduce
  "Launch a map/reduce job (json request/response)."
  [asc request cb stream-cb]
  {:pre [(byte-string? request)]}

  (call asc cb :map-red-req :map-red-resp
        RbpMapRedReq->bytes bytes->RbpMapRedResp
        {:request request
         :content-type (byte-string<-utf8-string "application/json")}
        true :response :done stream-cb))
