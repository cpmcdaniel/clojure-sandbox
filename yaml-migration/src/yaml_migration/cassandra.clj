(ns yaml-migration.cassandra
  (:require [clj-yaml.core :as yaml]
            [clojure.java.io :as io]
            [clojure.data :as d]
            [clojure.string :as st :refer [join]]
            [cemerick.pomegranate :refer [add-dependencies]]
            [cemerick.pomegranate.aether :as ae]
            [clojure.pprint :refer [pprint]]
            [yaml-migration.migrate :refer [comment-lines]])
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

(defn options-with-filter
  [f]
  (fn [config options]
   (into {}
         (filter f
                 (for [option options]
                   [option (get-field config option)])))))

(def new-options-with-defaults
  (options-with-filter #(not (nil? (second %)))))

(def new-options-without-defaults
  (options-with-filter #(nil? (second %))))

(defn migrate-yaml
  [old-file default-config out-file]
  (let [old-data (yaml/parse-string (slurp old-file))
        valid-keys (get-public-attributes default-config)
        old-keys (set (keys old-data))
        obsolete-keys (clojure.set/difference old-keys valid-keys)
        new-keys (clojure.set/difference valid-keys old-keys)]

    (spit out-file
          (str
           (yaml/generate-string
            (select-keys old-data
                         (clojure.set/difference old-keys obsolete-keys)))

           "\n\n## New options with defaults:\n"
           (yaml/generate-string
            (new-options-with-defaults default-config new-keys))

           "\n\n## New options without defaults:\n"
           (comment-lines
            (yaml/generate-string
             (new-options-without-defaults default-config new-keys)))

           "\n\n## The following options are no longer valid in this version of the config:\n"
           (comment-lines
            (yaml/generate-string
             (select-keys old-data
                          obsolete-keys)))))))


;; To execute this one, run lein run -m yaml-migration.cassandra <in-file> <version> <out-file>
(defn -main [& args]
  (let [in-file (nth args 0)
        version (nth args 1)
        out-file (nth args 2)]
    (add-dependencies :coordinates [['org.apache.cassandra/cassandra-all version]])
    (import '[org.apache.cassandra.config Config])
    (eval `(migrate-yaml ~in-file (Config.) ~out-file))))
