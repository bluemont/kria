(defn cb-fn [p] (fn [asc e a] (deliver p [asc e a])))
(defn conn-cb [asc e a] (println (if e e "connected")))
(def conn (client/connect nil "127.0.0.1" 8087 conn-cb))
(def b (conv/byte-string<-utf8-string "B-503"))
(def idx "I-503")

(def p (promise))
(index/put conn idx {} (cb-fn p))
@p

(def p (promise))
(bucket/set conn b {:props {:search true :search-index idx}} (cb-fn p))
@p

(defn json
  [jk jv]
  (str "{\"" jk "\" : \"" jv "\"}"))

(def k (conv/byte-string<-utf8-string "Key-1"))
(def v {:value (conv/byte-string<-utf8-string (json "word" "ocean"))
        :content-type "application/json"})
(object/put conn b k v {} result-cb)

(def k (conv/byte-string<-utf8-string "Key-2"))
(def v {:value (conv/byte-string<-utf8-string (json "word" "stream"))
        :content-type "application/json"})
(object/put conn b k v {} result-cb)

(def k (conv/byte-string<-utf8-string "Key-3"))
(def v {:value (conv/byte-string<-utf8-string (json "word" "river"))
        :content-type "application/json"})
(object/put conn b k v {} result-cb)

(def q (conv/byte-string<-utf8-string "word:*"))
; (search/search conn q idx {} (cb-fn p))
(def p (promise))
(search/search conn q idx {:fl ["*"]} (cb-fn p))
(println @p)

