(ns kria.index-test
  (:require
    [clojure.test :refer :all]
    [kria.test-helpers :as h]
    [kria.conversions :refer :all]
    [kria.client :as c]
    [kria.index :as i]))

(deftest put-1-get-1-test
  (testing "put 1, get 1"
    (let [conn (h/connect)
          idx (h/rand-index)
          p1 (promise)
          p2 (promise)]
      (i/put conn idx {} (h/cb-fn p1))
      (let [[asc e a] @p1]
        (is (nil? e))
        (is (true? a)))
      (i/get conn idx (h/cb-fn p2))
      @p2
      (is (i/get-poll conn idx 5 500)))))
      (c/disconnect conn))))
