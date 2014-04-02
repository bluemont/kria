(ns kria.client-test
  (:require [clojure.test :refer :all]
            [kria.test-helpers :as h]
            [kria.client :as c]
            [kria.conversions :refer [byte-string<-utf8-string]])
  (:import [sun.nio.ch UnixAsynchronousSocketChannelImpl]))

(deftest connect-test
  (testing "connect"
    (let [p (promise)
          conn (c/connect nil "127.0.0.1" 8087 (h/cb-fn p))]
      (is (= (class conn) UnixAsynchronousSocketChannelImpl))
      (is (= @p [conn nil true])))))

(deftest disconnect-test
  (testing "disconnect"
    (let [p (promise)
          conn (c/connect nil "127.0.0.1" 8087 (h/cb-fn p))]
      @p
      (c/disconnect conn)
      (is (not (.isOpen conn))))))
