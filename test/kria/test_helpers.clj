(ns kria.test-helpers
  (:require
   [kria.bucket :as bucket]
   [kria.client :as client]
   [kria.conversions :refer [byte-string<-utf8-string
                             utf8-string<-byte-string]]
   [kria.index :as index]
   [kria.polling :as p]
   [kria.schema :as schema]))

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

(defn rand-schema
  []
  (->> (rand-int 100000000)
       (format "S-%08d")
       byte-string<-utf8-string))

(defn cb-fn
  [p]
  (fn [asc e a] (deliver p [asc e a])))

(defn connect
  []
  (let [p (promise)
        conn (client/connect "127.0.0.1" 8087 (cb-fn p))]
    @p
    conn))

(defn get-schema
  [conn schema-name]
  (let [p (promise)]
    (schema/get conn schema-name (cb-fn p))
    (let [[asc e a] @p]
      a)))

(defn get-index
  [conn idx]
  (let [p (promise)]
    (index/get conn (byte-string<-utf8-string idx) (cb-fn p))
    (let [[asc e a] @p]
      a)))

(defn get-bucket
  [conn b]
  (let [p (promise)]
    (bucket/get conn b (cb-fn p))
    (let [[asc e a] @p]
      a)))

(defn slurp-schema
  []
  (byte-string<-utf8-string
   (slurp "test/resources/schema_basic.xml")))

(defn setup-schema
  [conn schema-name]
  (let [p (promise)]
    (schema/put conn schema-name (slurp-schema) (cb-fn p))
    (let [[asc e a] @p]
      a)))

(defn setup-index
  [conn idx schema-name]
  (let [p (promise)]
    (index/put conn
               (byte-string<-utf8-string idx)
               {:index {:schema schema-name}}
               (cb-fn p))
    (let [[asc e a] @p]
      a)))

(defn setup-bucket
  [conn b idx]
  (let [p (promise)
        opts {:props {:search true
                      :search-index (byte-string<-utf8-string idx)}}]
    (bucket/set conn b opts (cb-fn p))
    @p))

(def max-i
  "Maximum number of iterations."
  20)

(def i-delay
  "Millisecond delay between iterations."
  500)

(defn schema-ready?
  [conn schema-name schema-content]
  (p/poll schema-content
          #(-> (get-schema conn schema-name)
               :schema
               :content)
          max-i i-delay))

(defn index-ready?
  [conn idx]
  (p/poll #{(byte-string<-utf8-string idx)}
          #(->> (get-index conn idx)
                :index
                (map :name)
                set)
          max-i i-delay))

(defn bucket-ready?
  [conn b idx]
  (p/poll idx
          #(->> (get-bucket conn b)
                :props
                :search-index
                utf8-string<-byte-string)
          max-i i-delay))
