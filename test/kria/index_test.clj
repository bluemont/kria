(ns kria.index-test
  (:require
   [clojure.test :refer :all]
   [kria.test-helpers :as h]
   [kria.client :as c]
   [kria.index :as i]
   [kria.object :as o]
   [kria.pb.pair :refer [Pair->pb]]
   [kria.conversions :as conv]
   ))

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
      (is (h/index-ready? conn idx))
      (c/disconnect conn))))

(deftest secondary-index-test
  (testing "storing and searching for an object with secondary indices."
    (let [conn (h/connect)
          b (h/rand-bucket)

          k (h/rand-key)
          o-val (assoc (h/rand-value) :indexes [{:key "test_bin" :value "test-idx-val"}])

          put-p (promise)
          get-p (promise)]
      (testing "store a value with an index"
        (o/put conn b k o-val {} (h/cb-fn put-p))
        @put-p)

      (when (get (System/getenv) "KRIA_TEST_2I")
          (testing "get a value with an index"
            (i/get-2i
             conn
             b
             (conv/byte-string<-utf8-string "test_bin")
             (conv/byte-string<-utf8-string "test-idx-val")
             {}
             (h/cb-fn get-p))
            (let [[asc e a] @get-p
                  {:keys [keys]} a]
              (is (= (count keys)
                     1))
              (is (= (first keys) (conv/utf8-string<-byte-string k))))))

      (c/disconnect conn))))
