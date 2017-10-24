(defproject guildsman-mnist "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Apache License, Version 2.0"
            :url "http://www.apache.org/licenses/LICENSE-2.0"}
  :dependencies [[org.clojure/clojure "1.9.0-beta2"]
                 [com.billpiel/guildsman "0.0.1-DEV"]]
  :main ^:skip-aot guildsman-mnist.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
