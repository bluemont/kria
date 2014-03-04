(ns kria.bucket-test
  (:require [clojure.test :refer :all]
            [kria.test-helpers :as h]
            [kria.client :as c]
            [kria.bucket :as b]))

(deftest set-get-test
  (testing "set, get"
    (let [conn (h/connect)
          b (h/rand-bucket)
          idx (h/rand-index)
          p1 (promise)
          p2 (promise)]
      (b/set conn b {:props {:search-index idx}} (h/cb-fn p1))
      (let [[asc e a] @p1]
        (is (nil? e))
        (is (true? a)))
      (b/get conn b (h/cb-fn p2))
      (let [[asc e a] @p2]
        (is (nil? e))
        (is (= idx (-> a :props :search-index)))))))
