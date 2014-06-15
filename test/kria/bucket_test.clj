(ns kria.bucket-test
  (:require [clojure.test :refer :all]
            [kria.test-helpers :as h]
            [kria.conversions :refer [utf8-string<-byte-string]]
            [kria.client :as c]
            [kria.object :as o]
            [kria.bucket :as b]
            [kria.index :as i]))

(deftest get-defaults-test
  (testing "allow_mult=false by default"
    (let [conn (h/connect)
          b (h/rand-bucket)
          p (promise)]
      (b/get conn b (h/cb-fn p))
      (let [[asc e a] @p
            props (:props a)]
        (is (= (:n-val props) 3))
        (is (= (:allow-mult props) false))
        (is (= (:last-write-wins props) false))
        (is (= (:pr props) 0))
        (is (= (:r props) -3))
        (is (= (:w props) -3))
        (is (= (:pw props) 0))
        (is (= (:dw props) -3))
        (is (= (:rw props) -3))
        (is (= (:basic-quorum props) false))
        (is (= (:not-found-ok props) true))
        (is (= (:search props) false))))))

(deftest set-get-test
  (testing "set, get"
    (let [conn (h/connect)
          b (h/rand-bucket)
          idx (h/rand-index)
          p1 (promise)
          p2 (promise)
          p3 (promise)]
      (i/put conn idx {} (h/cb-fn p1))
      (let [[asc e a] @p1]
        (is (nil? e))
        (is (true? a)))
      (is (i/get-poll conn idx 250 20))
      (b/set conn b {:props {:search-index idx}} (h/cb-fn p2))
      (let [[asc e a] @p2]
        (is (nil? e))
        (is (true? a)))
      (b/get conn b (h/cb-fn p3))
      (let [[asc e a] @p3]
        (is (nil? e))
        (is (= idx (-> a :props :search-index))))
      (c/disconnect conn))))

(defn put-object
  [conn b]
  (let [k (h/rand-key)
        v (h/rand-value)
        p (promise)]
    (o/put conn b k v {} (h/cb-fn p))
    @p
    k))

(deftest put-3-keys-list-keys-test
  (testing "put 3 keys, list keys"
    (let [conn (h/connect)
          b (h/rand-bucket)
          ks [(put-object conn b)
              (put-object conn b)
              (put-object conn b)]
          result (promise)
          stream (atom [])
          stream-cb (fn [xs]
                      (if xs
                        (swap! stream concat xs)
                        (deliver result @stream)))
          result-cb (fn [asc e a] (or a e))]
      (b/list conn b {} result-cb stream-cb)
      (is (= (set (map utf8-string<-byte-string ks))
             (set (map utf8-string<-byte-string @result))))
      (c/disconnect conn))))
