(ns put-reducer.core)

(defmulti service-put
  "Returns the reducing function for the key passed in"
  (fn [dest config] (namespace dest)))

(defn dispatch-put-reducer-fn
  "Reducing function that dispatches [<dest-key> <record>] to the appropriate
  put function and keeps track of them so they can be called to clean up upon
  termination. <dest-key> is expected to be a keyword in the form `:<service>/<dest>`.
  The service-put functions are intended to be side-effects only. Any
  return values will be discarded."
  [config]
  (fn
    ([] {})
    ([result]
     (doseq [[f f-result] result]
       (f f-result))
     nil)
    ([result [k v :as item]]
     (let [f (service-put k config)
           previous-result (get result f (f))]
       (assoc result f (f previous-result item))))))
