(ns yaml-migration.cassandra
  (:require [clj-yaml.core :as yaml]
            [clojure.java.io :as io]
            [clojure.data :as d]
            [clojure.string :as st :refer [join]]
            [cemerick.pomegranate :refer [add-dependencies]]
            [cemerick.pomegranate.aether :as ae]
            [clojure.pprint :refer [pprint]])
  (:import [java.io StringReader]
           [java.util.regex Pattern]
           [java.lang.reflect Modifier Field])
  (:gen-class :main true))


(def private-or-static-bit-mask
  (bit-or Modifier/PRIVATE Modifier/STATIC))


(defn get-public-attributes
  [config]
  (set
   (map #(keyword (.getName %))
           (filter #(= 0 (bit-and (.getModifiers %) private-or-static-bit-mask))
                   (seq (.getDeclaredFields (class config)))))))

(defn get-field [obj key]
  (.get (.getField (class obj) (name key)) obj))

(defn migrate-yaml
  [old-file default-config]
  (let [old-data (yaml/parse-string (slurp old-file))
        valid-keys (get-public-attributes default-config)
        old-keys (set (keys old-data))
        obsolete-keys (clojure.set/difference old-keys valid-keys)
        new-keys (clojure.set/difference valid-keys old-keys)]
    (pprint obsolete-keys)
    (doseq [field-key new-keys]
      (println field-key " " (get-field default-config field-key)))
    ))

(comment
  (let [c (Config.)]
    (.get (.getDeclaredField (class c) (name :concurrent_replicates)) c))

  (migrate-yaml "resources/file1.yaml"
                (Config.)))


(defn -main [& args]
  (let [in-file (io/file (args 0))
        version (args 1)]
    (add-dependencies :coordinates [['org.apache.cassandra/cassandra-all version]])
    (import '[org.apache.cassandra.config Config])
    (migrate-yaml in-file (Config.))))
