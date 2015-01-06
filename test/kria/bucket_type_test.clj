(ns kria.bucket-type-test
  (:require [clojure.test :refer :all]
            [kria.test-helpers :as h]
            [kria.conversions :refer [utf8-string<-byte-string
                                      byte-string<-utf8-string]]
            [kria.bucket.type :as bt]))

(deftest get-default-type-test
  (testing "deatatype=\"\" by default"
    (let [conn (h/connect)
          b (byte-string<-utf8-string "default")
          p (promise)]
      (bt/get conn b (h/cb-fn p))
      (let [[asc e a] @p
            props (:props a)]
        (is (= (:datatype props) ""))))))

(deftest get-invalid-type-test
  (testing "getting an invalid bucket type"
    (let [conn (h/connect)
          b (byte-string<-utf8-string "invalid_bucket_type")
          p (promise)]
      (bt/get conn b (h/cb-fn p))
      (let [[asc e a] @p]
        (is (= (:message e)
               "Invalid bucket type: <<\"invalid_bucket_type\">>"))))))

(deftest set-inactive-type-test
  (testing "setting properties on an inactive type"
    (let [conn (h/connect)
          b (byte-string<-utf8-string "inactive_bucket_type")
          p (promise)]
      (bt/set conn b {:props {:datatype "set"}}
              (h/cb-fn p))
      (let [[asc e a] @p]
        (is (= (:message e)
               "Invalid bucket properties: not_active"))))))
