(ns kria.conversions-test
  (:require [clojure.test :refer :all]
            [kria.conversions :refer :all])
  (:import
    [java.nio ByteBuffer]
    [com.google.protobuf ByteString]))

(deftest byte-array?-test
  (testing "byte-array?"
    (are [v e] (= (byte-array? v) e)
         (byte-array 10) true
         [0 1 2 3 4] false)))

(deftest byte-buffer?-test
  (testing "byte-buffer?"
    (are [v e] (= (byte-buffer? v) e)
         (ByteBuffer/allocate 10) true
         (byte-array 10) false)))

(deftest byte-string?-test
  (testing "byte-string?"
    (are [v e] (= (byte-string? v) e)
         (ByteString/copyFrom (byte-array 10)) true
         (ByteBuffer/allocate 10) false)))

(deftest byte-array<-byte-buffer-test
  (testing "byte-array<-byte-buffer"
    (is (-> (ByteBuffer/allocate 10)
            (byte-array<-byte-buffer)
            (byte-array?)))))

(deftest byte-array<-byte-string-test
  (testing "byte-array<-byte-string"
    (is (-> (ByteString/copyFrom (byte-array 10))
            (byte-array<-byte-string)
            (byte-array?)))))

(deftest byte-array<-utf8-string-test
  (testing "byte-array<-utf8-string"
    (is (-> "This is a UTF-8 string."
            (byte-array<-utf8-string)
            (byte-array?)))))

(deftest byte-buffer<-byte-array-test
  (testing "byte-buffer<-byte-array"
    (is (-> (byte-array 10)
            (byte-buffer<-byte-array)
            (byte-buffer?)))))

(deftest byte-buffer<-byte-string-test
  (testing "byte-buffer<-byte-string"
    (is (-> (ByteString/copyFrom (byte-array 10))
            (byte-buffer<-byte-string)
            (byte-buffer?)))))

(deftest byte-string<-byte-array-test
  (testing "byte-string<-byte-array"
    (is (-> (byte-array 10)
            (byte-string<-byte-array)
            (byte-string?)))))

(deftest byte-string<-byte-buffer-test
  (testing "byte-buffer<-byte-string"
    (is (-> (ByteBuffer/allocate 10)
            (byte-string<-byte-buffer)
            (byte-string?)))))

(deftest byte-string<-utf8-string-test
  (testing "byte-string<-utf8-string"
    (is (-> "This is a UTF-8 string."
            (byte-string<-utf8-string)
            (byte-string?)))))

(deftest utf8-string<-byte-string-test
  (testing "utf8-string<-byte-string"
    (is (-> (ByteString/copyFrom (byte-array 10))
            (utf8-string<-byte-string)
            (string?)))))

(deftest utf8-string<-byte-array-test
  (testing "utf8-string<-byte-array"
    (is (-> (byte-array 10)
            (utf8-string<-byte-array)
            (string?)))))
