(ns kria.schema-test
  (:require
   [clojure.test :refer :all]
   [kria.test-helpers :as h]
   [kria.client :as c]
   [kria.schema :as schema]))

(defn test-put-get
  [schema-filename]
  (let [conn (h/connect)
        schema-name (h/rand-schema)
        schema-content (h/slurp-schema)
        p1 (promise)
        p2 (promise)]
    (schema/put conn schema-name schema-content (h/cb-fn p1))
    (let [[asc e a] @p1]
      (is (nil? e))
      (is (true? a)))
    (schema/get conn schema-name (h/cb-fn p2))
    (let [[asc e a] @p2]
      (is (= schema-content (-> a :schema :content))))
    (c/disconnect conn)))

(deftest put-basic-get-basic-test
  (testing "put basic, get basic"
    (test-put-get "test/resources/schema_basic.xml")))

(deftest put-minimal-get-minimal-test
  (testing "put minimal, get minimal"
    (test-put-get "test/resources/schema_minimal.xml")))
