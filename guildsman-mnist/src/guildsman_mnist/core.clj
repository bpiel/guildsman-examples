(ns guildsman-mnist.core
  (:require [guildsman-mnist.mnist-example1 :as ex1])
  (:gen-class))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (ex1/train))
