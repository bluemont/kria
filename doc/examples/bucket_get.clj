(defn conn-cb [asc e a] (println (if e e "connected")))
(def conn (client/connect "127.0.0.1" 8087 conn-cb))
(def b (conv/byte-string<-utf8-string "b1"))
(bucket/get conn b result-cb)
(pprint @result)

(some-> @result
    :props
    :search-index
    conv/utf8-string<-byte-string
    )
