(ns kria.search-test
  (:require
    [clojure.test :refer :all]
    [kria.bucket :as b]
    [kria.conversions :refer [byte-string?
                              byte-string<-utf8-string
                              utf8-string<-byte-string]]
    [kria.test-helpers :as h]
    [kria.index :as i]
    [kria.object :as o]
    [kria.search :as s]))

(defn setup-index
  [conn idx]
  (let [p (promise)]
    (i/put conn idx {} (h/cb-fn p))
    @p))

(defn setup-bucket
  [conn b idx]
  (let [p (promise)
        opts {:props {:search true :search-index idx}}]
    (b/set conn b opts (h/cb-fn p))
    @p))

(defn json
  [jk jv]
  (str "{\"" jk "\" : \"" jv "\"}"))

(defn put-object
  [conn b jk jv]
  {:pre [(byte-string? b) (string? jk) (string? jv)]}
  (let [k (h/rand-key)
        v {:value (byte-string<-utf8-string (json jk jv))
           :content-type "application/json"}
        p (promise)]
    (o/put conn b k v {} (h/cb-fn p))
    @p
    k))

(defn put-objects
  "Returns the object keys"
  [conn v jk jvs]
  (doall (map #(put-object conn v jk %) jvs)))

(deftest store-4-search-test
  (testing "store 3, search"
    (let [conn (h/connect)
          b (h/rand-bucket)
          idx (h/rand-index)
          _ (setup-index conn idx)
          _ (i/get-poll conn idx 100 20 1.5 500) ; TODO: test return value
          _ (setup-bucket conn b idx)
          words ["zone" "ozone" "anger" "danger"]
          ks (put-objects conn b "word" words)]
      (Thread/sleep 2000)
      (let [q (byte-string<-utf8-string "word:zone")
            p (promise)
            _ (s/search conn idx q {} (h/cb-fn p))
            [asc e a] @p
            docs (:docs a)
            doc (first docs)
            fields (:fields doc)
            ms (map #(hash-map (:key %) (:value %)) fields)
            m (apply merge ms)]
        (is (= 1 (:num-found a)))
        (is (= (utf8-string<-byte-string b)
               (get m "_yz_rb")))
        (is (= (utf8-string<-byte-string (first ks))
               (get m "_yz_rk"))))
      (let [q (byte-string<-utf8-string "word:*zone")
            p (promise)
            _ (s/search conn idx q {} (h/cb-fn p))
            [asc e a] @p]
        (is (= 2 (:num-found a)))))))
