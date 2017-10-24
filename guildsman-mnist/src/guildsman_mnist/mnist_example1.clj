(ns guildsman-mnist.mnist-example1
  (:require [com.billpiel.guildsman.core :as gm]
            [com.billpiel.guildsman.ops.basic :as gb]
            [com.billpiel.guildsman.ops.composite :as gc]
            [guildsman-mnist.mnist-data :as data]))

(defn train
  ([] (train 100 100))
  ([train-set-size train-epochs]
   (let [feed {:data (take train-set-size @data/train-data)
               :labels (take train-set-size @data/train-labels)}
         {:keys [logits classes]}
         (gm/id$->> (gb/placeholder :data
                                    gm/dt-float
                                    [-1 784])
                    (gc/dense {:id :logits
                               :units 10})
                    (gb/arg-max :classes $ 1))
         {:keys [labels opt]}      
         (gm/id$->> (gb/placeholder :labels
                                    gm/dt-int
                                    [-1])
                    (gc/one-hot $ 10)
                    (gc/mean-squared-error logits)
                    (gc/reduce-mean :loss)
                    (gc/grad-desc-opt :opt 0.1))
         acc (gc/accuracy :acc
                          (gb/cast-tf {:SrcT gm/dt-long
                                       :DstT gm/dt-int}
                                      classes)
                          labels)]
     (gm/with-close-let [{:keys [graph] :as sess} (gm/build-all->session [opt acc])]
       (gm/run-global-vars-init sess)
       (println "Initial accuracy:"
                (gm/fetch sess acc feed))
       (gm/run-all sess
                   (repeat train-epochs
                           opt)
                   feed)
       (println "Accuracy after training:"
                (gm/fetch sess acc feed))))))

#_ (train)
