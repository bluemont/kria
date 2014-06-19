(defn conn-cb [asc e a] (println (if e e "connected")))
(def conn (client/connect "127.0.0.1" 8087 conn-cb))
(def b (conv/byte-string<-utf8-string "b1"))
(def k (conv/byte-string<-utf8-string "k02"))
(object/delete conn b k {} result-cb)
@result ; returns true even if no object is deleted
