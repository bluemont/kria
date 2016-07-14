(defn conn-cb [asc e a] (println (if e e "connected")))
(def conn (client/connect "127.0.0.1" 8087 conn-cb))
(def content (slurp "test/resources/schema_basic.xml"))
(schema/put conn (conv/byte-string<-utf8-string "S-600") (conv/byte-string<-utf8-string content) result-cb)
(pprint @result)
