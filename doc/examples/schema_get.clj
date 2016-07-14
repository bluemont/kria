(defn conn-cb [asc e a] (println (if e e "connected")))
(def conn (client/connect "127.0.0.1" 8087 conn-cb))
(schema/get conn (conv/byte-string<-utf8-string "S-600") result-cb)
(keys @result) ; (:schema)
(keys (:schema @result)) ; (:name :content)
(-> @result :schema :name conv/utf8-string<-byte-string) ; "S-600"
(print (-> @result :schema :content conv/utf8-string<-byte-string))
