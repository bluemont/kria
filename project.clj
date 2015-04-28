(defproject kria "0.1.16-SNAPSHOT"
  :description "A Clojure client library for Riak 2.1.0. Uses NIO.2."
  :url "https://github.com/bluemont/kria"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :min-lein-version "2.0.0"
  :dependencies [[org.clojure/clojure "1.7.0-beta2"]
                 [com.google.protobuf/protobuf-java "2.6.1"]
                 [com.basho.riak.protobuf/riak-pb "2.0.0.16"]]
  :source-paths ["src/clojure"]
  :java-source-paths ["src/java"]
  :profiles {:dev
             {:source-paths ["dev"]
              :dependencies [[org.clojure/tools.namespace "0.2.10"]
                             [org.clojure/data.json "0.2.6"]]}})
