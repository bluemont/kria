(ns kria.dt-set-test
  (:require [clojure.test :refer :all]
            [kria.test-helpers :as h]
            [kria.conversions :refer [byte-string<-utf8-string
                                      utf8-string<-byte-string]]
            [kria.client :as c]
            [kria.data-type.set :as dts]))

(deftest set-add-only-test
  (testing "set add one"
    (let [conn (h/connect)
          b (h/rand-bucket)
          t (byte-string<-utf8-string "kria-set")
          k (h/rand-key)
          v1 (:value (h/rand-value))

          up1 (promise)
          gp1 (promise)]

      (dts/update conn b t k [v1] nil {} (h/cb-fn up1))
      (let [[asc e a] @up1]
        (is (nil? e))
        (is (= true a)))
      (dts/get conn b t k {} (h/cb-fn gp1))
      (let [[asc e a] @gp1
            vals (map utf8-string<-byte-string a)
            exp [(utf8-string<-byte-string v1)]]
        (is (nil? e))
        (is (= vals exp)))))

  (testing "set add one twice"
    (let [conn (h/connect)
          b (h/rand-bucket)
          t (byte-string<-utf8-string "kria-set")
          k (h/rand-key)
          v1 (:value (h/rand-value))

          up1 (promise)
          up2 (promise)
          gp1 (promise)]

      (dts/update conn b t k [v1] nil {} (h/cb-fn up1))
      (let [[asc e a] @up1]
        (is (nil? e))
        (is (= true a)))
      (dts/update conn b t k [v1] nil {} (h/cb-fn up2))
      (let [[asc e a] @up2]
        (is (nil? e))
        (is (= true a)))
      (dts/get conn b t k {} (h/cb-fn gp1))
      (let [[asc e a] @gp1
            vals (map utf8-string<-byte-string a)
            exp [(utf8-string<-byte-string v1)]]
        (is (nil? e))
        (is (= vals exp)))))

  (testing "set add two"
    (let [conn (h/connect)
          b (h/rand-bucket)
          t (byte-string<-utf8-string "kria-set")
          k (h/rand-key)
          v1 (:value (h/rand-value))
          v2 (:value (h/rand-value))

          up1 (promise)
          up2 (promise)
          gp1 (promise)
          gp2 (promise)]

      (dts/update conn b t k [v1] nil {} (h/cb-fn up1))
      (let [[asc e a] @up1]
        (is (nil? e))
        (is (= true a)))
      (dts/get conn b t k {} (h/cb-fn gp1))
      (let [[asc e a] @gp1
            vals (map utf8-string<-byte-string a)
            exp [(utf8-string<-byte-string v1)]]
        (is (nil? e))
        (is (= vals exp)))

      (dts/update conn b t k [v2] nil {} (h/cb-fn up2))
      (let [[asc e a] @up2]
        (is (nil? e))
        (is (= true a)))
      (dts/get conn b t k {} (h/cb-fn gp2))
      (let [[asc e a] @gp2
            vals (set (map utf8-string<-byte-string a))
            exp #{(utf8-string<-byte-string v1)
                  (utf8-string<-byte-string v2)}]
        (is (nil? e))
        (is (= vals exp))))))

(deftest set-add-and-remove
  (testing "set add and remove"
    (let [conn (h/connect)
          b (h/rand-bucket)
          t (byte-string<-utf8-string "kria-set")
          k (h/rand-key)
          v1 (:value (h/rand-value))
          v2 (:value (h/rand-value))

          up1 (promise)
          up2 (promise)
          gp1 (promise)
          gp2 (promise)]

      (dts/update conn b t k [v1 v2] nil {} (h/cb-fn up1))
      (let [[asc e a] @up1]
        (is (nil? e))
        (is (= true a)))
      (dts/get conn b t k {} (h/cb-fn gp1))
      (let [[asc e a] @gp1
            vals (set (map utf8-string<-byte-string a))
            exp #{(utf8-string<-byte-string v1)
                  (utf8-string<-byte-string v2)}]
        (is (nil? e))
        (is (= vals exp)))

      (dts/update conn b t k nil [v1] {} (h/cb-fn up2))
      (let [[asc e a] @up2]
        (is (nil? e))
        (is (= true a)))
      (dts/get conn b t k {} (h/cb-fn gp2))
      (let [[asc e a] @gp2
            vals (set (map utf8-string<-byte-string a))
            exp #{(utf8-string<-byte-string v2)}]
        (is (nil? e))
        (is (= vals exp))))))
