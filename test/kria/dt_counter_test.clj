(ns kria.dt-counter-test
  (:require [clojure.test :refer :all]
            [kria.test-helpers :as h]
            [kria.conversions :refer [byte-string<-utf8-string]]
            [kria.client :as c]
            [kria.data-type.counter :as dtc]))

(deftest counter-inc-test
  (testing "counter increment"
    (let [conn (h/connect)
          b (h/rand-bucket)
          t (byte-string<-utf8-string "kria-counter")
          k (h/rand-key)
          incr 2

          ip (promise)
          ip2 (promise)
          gp (promise)
          gp2 (promise)]
      (dtc/increment conn b t k incr {} (h/cb-fn ip))
      (let [[asc e a] @ip]
        (is (nil? e))
        (is (= true a)))
      (dtc/get conn b t k {} (h/cb-fn gp))
      (let [[asc e a] @gp]
        (is (nil? e))
        (is (>= a 2)))
      (dtc/increment conn b t k incr {} (h/cb-fn ip2))
      @ip2
      (dtc/get conn b t k {} (h/cb-fn gp2))
      (let [[_ _   a] @gp
            [asc e b] @gp2]
        (is (nil? e))
        (is (> b a))))))
