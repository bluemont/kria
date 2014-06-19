(defn conn-cb [asc e a] (println (if e e "connected")))
(def conn (client/connect "127.0.0.1" 8087 conn-cb))
(def b (conv/byte-string<-utf8-string "B-56"))

(def k (conv/byte-string<-utf8-string "K01"))
(def v {:value (conv/byte-string<-utf8-string "V01")})
(object/put conn b k v {} result-cb)

(def k (conv/byte-string<-utf8-string "K02"))
(def v {:value (conv/byte-string<-utf8-string "V02")})
(object/put conn b k v {} result-cb)

(def k (conv/byte-string<-utf8-string "K03"))
(def v {:value (conv/byte-string<-utf8-string "V03")})
(object/put conn b k v {} result-cb)

; By default, `(store b k v {} cb)` does not return the body:
(:contents @result)
; []

; To return the body, use:
(object/put conn b k v {:return-body true} result-cb)
(:contents @result)
; [...]


(def k (conv/byte-string<-utf8-string "k02"))
(def v {:value (conv/byte-string<-utf8-string "v02")})
(object/put conn b k v {} result-cb)
(pprint @result)
