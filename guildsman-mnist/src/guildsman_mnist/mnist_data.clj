(ns guildsman-mnist.mnist-data
  (:require [clojure.java.io :as io])
  (:import [java.io DataInputStream File FileInputStream BufferedInputStream]))

(def TEST-CASE-COUNT 10000)
(def TRAIN-CASE-COUNT 60000)
(def SIZE 28)

(def mnist-path "mnist")

(defn ^DataInputStream mk-dis [name]
  (-> (str mnist-path name)
      io/resource
      io/input-stream
      (DataInputStream.)))

(defn normalize-img-byte [x]
  (float (/ x 255.0)))

(defn read-mnist-data
  [res-name n size]
  (with-open [^DataInputStream dis (mk-dis res-name)]
    (if (not= (.readInt dis) 2051)
      (throw (Error. "Wrong magic number")))
    (if (not= (.readInt dis) n)
      (throw (Error. "Unexpected image count")))  
    (if (not= (.readInt dis) size)
      (throw (Error. "Unexpected row count")))  
    (if (not= (.readInt dis) size)
      (throw (Error. "Unexpected column count")))  
    (vec (for [i (range n)]
           (vec (for [y (range size)
                      x (range size)]
                  (normalize-img-byte (.readUnsignedByte dis))))))))

(defn read-mnist-labels
  [res-name n]
  (with-open [^DataInputStream data-input-stream (mk-dis res-name)]
    (if (not= (.readInt data-input-stream) 2049)
      (throw (Error. "Wrong magic number")))
    (if (not= (.readInt data-input-stream) n)
      (throw (Error. "Unexpected image count")))
    (vec (for [i (range n)]
           (int (.readUnsignedByte data-input-stream))))))

(def train-data (future (read-mnist-data "/train-60k-images-idx3-ubyte"
                                         TRAIN-CASE-COUNT
                                         SIZE)))

(def train-labels (future (read-mnist-labels "/train-60k-labels-idx1-ubyte"
                                           TRAIN-CASE-COUNT)))

(def test-data (future (read-mnist-data "/test-10k-images-idx3-ubyte"
                                         TEST-CASE-COUNT
                                         SIZE)))

(def test-labels (future (read-mnist-labels "/test-10k-labels-idx1-ubyte"
                                      TEST-CASE-COUNT)))

