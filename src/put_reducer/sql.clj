(ns put-reducer.sql
  (:require [clojure.tools.logging :as log]
            [put-reducer.core :as core]))

(defmulti sql-put
  (fn [dest _]
    (name dest)))

(defmethod core/service-put "sql"
  [dest config]
  (sql-put dest config))






(let [sql-put-hotel (fn sql-put-hotel
                      ([] [])
                      ([result]
                       (log/infof "running `INSERT INTO hotel VALUES (...)` on %d items - %s"
                                  (count result) result))
                      ([result [_ row]] (conj result row)))]

  (defmethod sql-put "hotel"
    [_ config]
    sql-put-hotel))






(let [sql-put-photo (fn sql-put-photo
                      ([] [])
                      ([result]
                       (log/infof "running `INSERT INTO photos VALUES (...)` on %d items - %s"
                                  (count result) result))
                      ([result [_ row]]
                       (conj result row)))]
  (defmethod sql-put "photo"
    [_ config]
    sql-put-photo))

(comment
  (def hotel-data [{:id 1234
                    :name "Hotel Devine"
                    :photos [{:caption "lobby"
                              :height 50
                              :width 50}
                             {:caption "bathroom"
                              :height 15
                              :width 15}
                             {:caption "flower"
                              :height 100
                              :width 100}]}
                   {:id 1235
                    :name "Bates Motel"}])

  (defn process-hotel
    [{:keys [id name photos] :as hotel}]
    (cond-> [[:sql/hotel [id name]]]
      photos (concat (map (fn [{:keys [caption height width]}]
                            [:sql/photo [id caption height width]])
                          photos))))

  (transduce (mapcat process-hotel)
             (core/dispatch-put-reducer-fn {})
             hotel-data)
  )
