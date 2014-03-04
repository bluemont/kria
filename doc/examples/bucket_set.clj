(defn conn-cb [asc e a] (println (if e e "connected")))
(def conn (client/connect nil "127.0.0.1" 8087 conn-cb))
(def b (conv/byte-string<-utf8-string "b1"))
(def opts {:props
           {:search true
            :search-index "I123"}})
(bucket/set conn b opts result-cb)
(pprint @result)
