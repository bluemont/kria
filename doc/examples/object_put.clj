(defn conn-cb [asc e a] (println (if e e "connected")))
(def conn (client/connect nil "127.0.0.1" 8087 conn-cb))
(def b (conv/byte-string<-utf8-string "b1"))
(def k (conv/byte-string<-utf8-string "k01"))
(def v {:value (conv/byte-string<-utf8-string "v01")})
(object/put conn b k v {} result-cb)
(pprint @result)

; By default, `(store b k v {} cb)` does not return the body:
(:contents @result)
; []

; To return the body, use:
(object/put conn b k v {:return-body true} result-cb)
(:contents @result)
; [...]
