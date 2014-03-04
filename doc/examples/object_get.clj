(defn conn-cb [asc e a] (println (if e e "connected")))
(def conn (client/connect nil "127.0.0.1" 8087 conn-cb))
(def b (conv/byte-string<-utf8-string "b1"))
(def k (conv/byte-string<-utf8-string "k01"))
(object/get conn b k {} result-cb)
(pprint @result)

; Possible results:

(-> @result
    :content)
; []

(-> @result
    :content
    first ; don't use in real code*
    :value
    conv/utf8-string<-byte-string)
; "v01"
; * Note: don't grab the first sibling in real code.
;   You need to find a way to resolve the conflict!
