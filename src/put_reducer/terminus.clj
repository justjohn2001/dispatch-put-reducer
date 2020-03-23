(ns put-reducer.terminus
  (:require [clojure.tools.logging :as log]
            [put-reducer.core :as core]))

(defmulti log-terminus name)

(defmethod core/service-put "terminus"
  [k _]
  (log-terminus k))

#_(let [f (fn terminus-default
            ([] {})
            ([result] (doseq [[dest cnt] result]
                        (log/infof "%d entries reached terminus %s" cnt (name dest))))
            ([result [dest item]] (update result dest (fnil inc 0))))]
    (defmethod log-terminus :default
      [_]
      f))

(core/defreducermethod log-terminus :default
  ([] {})
  ([result] (doseq [[dest cnt] result]
              (log/infof "%d entries reached terminus %s" cnt (name dest))))
  ([result [dest item]] (update result dest (fnil inc 0))))

(comment
  (defn f1 []
    (fn [] 1))
  (= (f1) (f1))

  (let [f (fn [] 1)]
    (defn f2 []
      f))
  (= (f2) (f2))

  (defn f3 []
    (partial + 1))

  (= (f3) (f3))

  (transduce (map identity)
             (core/dispatch-put-reducer-fn {})
             [[:terminus/done :a]
              [:terminus/done :b]
              [:terminus/error :d]])
  )


