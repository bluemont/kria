(ns kria.schema-test
  (:require
    [clojure.pprint :refer (pprint)]
    [clojure.test :refer :all]
    [kria.test-helpers :as h]
    [kria.client :as c]
    [kria.schema :as s]))

(deftest put-1-get-1-test
  (testing "put 1, get 1"
    (let [conn (h/connect)
          s-name (h/rand-schema)
          s-content (slurp "test/resources/schema_default.xml")
          p1 (promise)
          p2 (promise)]
      (s/put conn s-name s-content (h/cb-fn p1))
      (let [[asc e a] @p1]
        (is (nil? e))
        (is (true? a)))
      (s/get conn s-name (h/cb-fn p2))
      (let [[asc e a] @p2]
        (is (= (-> a :schema :content) s-content)))
      (c/disconnect conn))))
