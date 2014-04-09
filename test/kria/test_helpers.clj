(ns kria.test-helpers
  (:require
    [kria.client :as client]
    [kria.conversions :refer [byte-string<-utf8-string]]))

(defn rand-bucket
  []
  (->> (rand-int 100000000)
       (format "B-%08d")
       (byte-string<-utf8-string)))

(defn rand-key
  []
  (->> (rand-int 100000000)
       (format "K-%08d")
       (byte-string<-utf8-string)))

(defn rand-value
  []
  {:value (->> (rand-int 10000)
               (format "V-%04d")
               (byte-string<-utf8-string))})

(defn rand-string-value
  [n]
  (let [s "1234567890ABCDEFGHIJKLMNOPQRSTUVWXYZ"]
    {:value (-> (repeatedly n #(str (rand-nth s)))
                (clojure.string/join)
                (byte-string<-utf8-string))}))

(defn rand-index
  []
  (->> (rand-int 100000000)
       (format "I-%08d")))

(defn cb-fn
  [p]
  (fn [asc e a] (deliver p [asc e a])))

(defn connect
  []
  (let [p (promise)
        conn (client/connect nil "127.0.0.1" 8087 (cb-fn p))]
    @p
    conn))
