(ns kria.object-test
  (:require [clojure.test :refer :all]
            [kria.test-helpers :as h]
            [kria.client :as c]
            [kria.bucket :as b]
            [kria.object :as o]))

(deftest put-1-get-1-test
  (testing "put 1, get 1"
    (let [conn (h/connect)
          b (h/rand-bucket)
          k (h/rand-key)
          v (h/rand-value)
          p (promise)]
      (o/put conn b k v {} (h/cb-fn p))
      (let [[asc e a] @p]
        (is (nil? e))
        (is (zero? (count (:content a))))
        (is (zero? (.size (:vclock a))))
        (is (zero? (.size (:key a)))))
      (let [p (promise)]
        (o/get conn b k {} (h/cb-fn p))
        (let [[asc e a] @p]
          (is (= (set (map :value [v]))
                 (set (map :value (:content a)))))
          (is (nil? e))))
      (c/disconnect conn))))

(deftest put-2-siblings-get-1-test
  (testing "put 2, get 1 (siblings)"
    (let [conn (h/connect)
          conn2 (h/connect)
          b (h/rand-bucket)
          k (h/rand-key)
          v1 (h/rand-value)
          v2 (h/rand-value)
          p1 (promise)
          p2 (promise)
          p3 (promise)
          p4 (promise)]
      (b/set conn b {:props {:allow-mult true}} (h/cb-fn p1))
      @p1
      (o/put conn b k v1 {} (h/cb-fn p2))
      (o/put conn2 b k v2 {} (h/cb-fn p3))
      [@p2 @p3]
      (c/disconnect conn2)
      (o/get conn b k {} (h/cb-fn p4))
      (let [[asc e a] @p4]
        (is (= (set (map :value [v1 v2]))
               (set (map :value (:content a)))))
        (is (nil? e)))
      (c/disconnect conn))))

(deftest put-2-get-1-test
  (testing "put 2, get 1 (no siblings)"
    (let [conn (h/connect)
          b (h/rand-bucket)
          k (h/rand-key)
          v1 (h/rand-value)
          v2 (h/rand-value)
          p1 (promise)
          p2 (promise)
          p3 (promise)
          p4 (promise)]
      (o/put conn b k v1 {} (h/cb-fn p1))
      @p1
      (o/get conn b k {} (h/cb-fn p2))
      (let [[asc e a] @p2
            vclock (:vclock a)]
        (o/put conn b k v2 {:vclock vclock} (h/cb-fn p3))
        @p3
        (o/get conn b k {} (h/cb-fn p4))
        (let [[asc e a] @p4]
          (is (= (set (map :value [v2]))
                 (set (map :value (:content a)))))
          (is (nil? e))))
      (c/disconnect conn))))

(deftest put-1-delete-1-get-1-test
  (testing "put 1, delete 1, get 1"
    (let [conn (h/connect)
          b (h/rand-bucket)
          k (h/rand-key)
          v (h/rand-value)
          p1 (promise)
          p2 (promise)
          p3 (promise)]
      (o/put conn b k v {} (h/cb-fn p1))
      (let [[asc e a] @p1]
        (is (nil? e))
        (is (zero? (count (:content a))))
        (is (zero? (.size (:vclock a))))
        (is (zero? (.size (:key a)))))
      (o/delete conn b k {} (h/cb-fn p2))
      (let [[asc e a] @p2]
        (is (nil? e))
        (is (true? a)))
      (o/get conn b k {} (h/cb-fn p3))
      (let [[asc e a] @p3]
        (is (nil? e))
        (is (empty? (:content a))))
      (c/disconnect conn))))
