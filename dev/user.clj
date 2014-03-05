(ns user
  (:require
    [clojure.pprint :refer (pprint)]
    [clojure.repl :refer :all]
    [clojure.string :as str]
    [clojure.test :as test]
    [clojure.tools.namespace.repl :refer (refresh)]
    [kria.bucket :as bucket]
    [kria.client :as client]
    [kria.conversions :as conv]
    [kria.core :as core]
    [kria.object :as object]
    [kria.search :as search]
    [kria.server :as server]))

(set! *warn-on-reflection* true)

(def result (atom nil))

(defn result-cb
  [asc e a]
  (if-not e
    (reset! result a)
    (if (instance? Throwable e)
      (.printStackTrace ^Throwable e)
      (println e))))
