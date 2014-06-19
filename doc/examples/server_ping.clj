(defn conn-cb [asc e a] (println (if e e "connected")))
(def conn (client/connect "127.0.0.1" 8087 conn-cb))
(defn ping-cb [asc e a] (println (if e e "pong")))
(server/ping conn ping-cb)
