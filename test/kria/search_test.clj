(ns kria.search-test
  (:require
   [clojure.test :refer :all]
   [kria.bucket :as b]
   [kria.conversions :refer [byte-string?
                             byte-string<-utf8-string
                             utf8-string<-byte-string]]
   [kria.index :as i]
   [kria.object :as o]
   [kria.polling :as p]
   [kria.schema :as schema]
   [kria.search :as s]
   [kria.test-helpers :as h]))

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

(deftest store-6-search-test
  (let [conn (h/connect)
        b (h/rand-bucket)
        idx (h/rand-index)
        schema-name (h/rand-schema)
        schema-content (h/slurp-schema)]
    (h/setup-schema conn schema-name)
    (is (h/schema-ready? conn schema-name schema-content))
    (h/setup-index conn idx schema-name)
    (is (h/index-ready? conn idx))
    (h/setup-bucket conn b idx)
    (is (h/bucket-ready? conn b idx))
    (let [phrases ["zone of danger"
                   "ozone layer"
                   "acid rain"
                   "danger zone"
                   "ocean acidification"
                   "preparation"]
          bs_ks (put-objects conn b "phrase" phrases)
          ks (mapv utf8-string<-byte-string bs_ks)]

      (testing "*:* (find 6)"
        (let [q (byte-string<-utf8-string "*:*")
              search (fn []
                       (let [p (promise)]
                         (s/search conn idx q {} (h/cb-fn p))
                         (let [[asc e a] @p]
                           a)))]
          (is (p/poll 6 #(:num-found (search)) h/max-i h/i-delay))
          (let [docs (:docs (search))
                ms (for [fields (map :fields docs)]
                     (->> fields
                          (map (fn [x] {(:key x) (:value x)}))
                          (apply merge)))
                yz_rb (set (map #(get % "_yz_rb") ms))
                yz_rk (set (map #(get % "_yz_rk") ms))]
            (is (= #{(utf8-string<-byte-string b)} yz_rb))
            (is (= (set ks) yz_rk)))))

      (testing "phrase:zone (find 2)"
        (let [q (byte-string<-utf8-string "phrase:zone")
              p (promise)
              _ (s/search conn idx q {} (h/cb-fn p))
              [asc e a] @p]
          (is (= 2 (:num-found a)))
          (let [docs (:docs a)
                ms (for [fields (map :fields docs)]
                     (->> fields
                          (map (fn [x] {(:key x) (:value x)}))
                          (apply merge)))
                yz_rb (set (map #(get % "_yz_rb") ms))
                yz_rk (set (map #(get % "_yz_rk") ms))]
            (is (= #{(utf8-string<-byte-string b)} yz_rb))
            (is (= (set (map ks [0 3])) yz_rk)))))

      (testing "phrase:* (find 6)"
        (let [q (byte-string<-utf8-string "phrase:*")
              p (promise)
              _ (s/search conn idx q {} (h/cb-fn p))
              [asc e a] @p]
          (is (= 6 (:num-found a)))))

      (testing "phrase:\"*\" (find 0)"
        (let [q (byte-string<-utf8-string "phrase:\"*\"")
              p (promise)
              _ (s/search conn idx q {} (h/cb-fn p))
              [asc e a] @p]
          (is (= 0 (:num-found a)))))

      (testing "prefix matching"
        (let [q (byte-string<-utf8-string "phrase:acid*")
              p (promise)
              _ (s/search conn idx q {} (h/cb-fn p))
              [asc e a] @p]
          (is (= 2 (:num-found a)))))

      (testing "suffix matching"
        (let [q (byte-string<-utf8-string "phrase:*zone")
              p (promise)
              _ (s/search conn idx q {} (h/cb-fn p))
              [asc e a] @p]
          (is (= 3 (:num-found a))))))))
