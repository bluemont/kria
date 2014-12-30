(ns kria.conversions
  (:import
    [java.nio ByteBuffer]
    [java.nio.charset Charset]
    [com.google.protobuf ByteString]))

(def ^:private ^:const byte-array-class
  (class (byte-array 0)))

(def ^:private utf-8
  (Charset/forName "UTF-8"))

(defn byte-array?
  [v]
  (instance? byte-array-class v))

(defn byte-buffer?
  [v]
  (instance? ByteBuffer v))

(defn byte-string?
  [v]
  (instance? ByteString v))

(defn ^{:tag 'bytes} byte-array<-byte-buffer
  [^ByteBuffer v]
  {:pre [(byte-buffer? v)]}
  (.array v))

(defn ^{:tag 'bytes} byte-array<-byte-string
  [^ByteString v]
  {:pre [(byte-string? v)]}
  (if v (.toByteArray v)))

(defn ^{:tag 'bytes} byte-array<-utf8-string
  [^String v]
  {:pre [(instance? String v)]}
  (if v (.getBytes v utf-8)))

(defn ^ByteBuffer byte-buffer<-byte-array
  [^bytes v]
  {:pre [(byte-array? v)]}
  (ByteBuffer/wrap v))

(defn ^ByteBuffer byte-buffer<-byte-string
  [^ByteString v]
  {:pre [(byte-string? v)]}
  (byte-buffer<-byte-array (byte-array<-byte-string v)))

(defn ^ByteString byte-string<-byte-array
  [^bytes v]
  {:pre [(byte-array? v)]}
  (if v (ByteString/copyFrom v)))

(defn ^ByteString byte-string<-byte-buffer
  [^ByteBuffer v]
  {:pre [(byte-buffer? v)]}
  (byte-string<-byte-array (byte-array<-byte-buffer v)))

(defn ^ByteString byte-string<-utf8-string
  [^String v]
  {:pre [(instance? String v)]}
  (if v (ByteString/copyFromUtf8 v)))

(defn ^String utf8-string<-byte-string
  [^ByteString v]
  {:pre [(byte-string? v)]}
  (if v (.toStringUtf8 v)))

(defn ^String utf8-string<-byte-array
  [^bytes v]
  {:pre [(byte-array? v)]}
  (utf8-string<-byte-string (byte-string<-byte-array v)))
