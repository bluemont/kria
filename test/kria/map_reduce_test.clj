(ns kria.map-reduce-test
  (:import [com.google.protobuf ByteString])
  (:require [clojure.test :refer :all]
            [kria.test-helpers :as h]
            [kria.client :as c]
            [kria.map-reduce :as mr]
            [kria.object :as o]
            [kria.conversions :refer [byte-string<-utf8-string
                                      utf8-string<-byte-string]]
            [clojure.data.json :as json]))

(deftest map-reduce-test
  (testing "simple bucket summing MapReduce job"
    (let [conn (h/connect)
          b (h/rand-bucket)
          job (json/write-str
                {:inputs (utf8-string<-byte-string b)
                 :query [{:map {:arg nil
                                :name "Riak.mapValuesJson"
                                :language "javascript"
                                :keep false}}
                         {:reduce {:arg nil
                                   :name "Riak.reduceSum"
                                   :language "javascript"
                                   :keep true}}]})
          result (promise)
          stream (atom [])
          stream-cb (fn [xs]
                      (if (and xs (not (zero? (.size ^ByteString xs))))
                        (swap! stream conj xs)
                        (deliver result @stream)))
          result-cb (fn [asc e a] (or a e))]
      (doseq [n (range 10)]
        (let [p (promise)]
          (o/put conn b
                 (byte-string<-utf8-string (format "K-%d" n))
                 {:value (byte-string<-utf8-string (format "%d" n))}
                 {}
                 (fn [_ _ a] (deliver p a)))
          @p))
      (mr/map-reduce conn (byte-string<-utf8-string job) result-cb stream-cb)
      (is (= (-> @result
                 first
                 utf8-string<-byte-string
                 json/read-str)
             [(apply + (range 10))])))))
