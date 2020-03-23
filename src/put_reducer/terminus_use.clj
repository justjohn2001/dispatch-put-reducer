(ns put-reducer.terminus-use
  (:require [put-reducer.core :as core]
            [put-reducer.terminus :as terminus]))

#_(defmethod terminus/log-terminus "vanish"
    [_]
    (fn
      ([] nil)
      ([result] nil)
      ([result [dest item]] nil)))

(core/defreducermethod terminus/log-terminus "vanish"
  ([] nil)
  ([result] nil)
  ([result item] nil))

(comment
  (transduce (map identity)
             (core/dispatch-put-reducer-fn {})
             [[:terminus/done :a]
              [:terminus/done :b]
              [:terminus/vanish :c]
              [:terminus/error :d]])
  )

