(ns put-reducer.kinesis
  (:require [clojure.tools.logging :as log]
            [clojure.string :as string]
            [put-reducer.core :as core]))

(defmulti kinesis-put (fn [dest _] (name dest)))

(defmethod core/service-put "kinesis"
  [dest config]
  (kinesis-put dest config))

(defn stream-key->stream-name
  [dest config]
  (string/join "-"
               (name (:prefix config))
               (name dest)
               (name (:stage config))))

(defn put-to-stream
  [stream-name records]
  (log/infof "Putting %d records onto kinesis stream %s" (count records) stream-name))

(defn kinesis-put-reducer
  [config]
  (fn
    ([] {})
    ([result]
     (doseq [[dest records] result]
       (put-to-stream (stream-key->stream-name dest config) records)))
    ([result [dest record]]
     (update result dest conj record))))

(def kinesis-put-reducer-memoized (memoize kinesis-put-reducer))

(defmethod kinesis-put :default
  [dest config]
  (kinesis-put-reducer-memoized config))
