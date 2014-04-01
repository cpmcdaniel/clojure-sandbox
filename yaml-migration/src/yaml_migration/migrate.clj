(ns yaml-migration.migrate
  (:require [clj-yaml.core :as yaml]
            [clojure.java.io :as io]
            [clojure.data :as d]
            [clojure.string :as st :refer [join]])
  (:import [java.io StringReader]
           [java.util.regex Pattern])
  (:gen-class :main true))

(defn comment-lines
  "Prepends each line with '# '"
  [^String s]
  (st/replace s (Pattern/compile "^" Pattern/MULTILINE) "# "))

(defn diff-to-str
  "Takes old-only data, new-only data, and data in both, and generates the YAML
   string."
  [old-only new-only both]
  (str (yaml/generate-string both)
       "\n\n## New defaults in this version of the config:\n"
       (yaml/generate-string new-only)
       "\n\n## The following options are no longer valid in this version of the config:\n"
       (comment-lines (yaml/generate-string old-only))))

(defn migrate-yaml
  "Takes an old file and a new file, and attempts to migrate to the
   new file version."
  [old-file new-file out-file]
  ;; Uses clojure.data/diff to get the differences between the 2
  ;; data structures.
  (let [old-data (yaml/parse-string (slurp old-file))
        new-data (yaml/parse-string (slurp new-file))]
    (spit out-file
          (apply diff-to-str (d/diff old-data new-data)))))

;; Allow command line usage
(defn -main [& args]
  (apply migrate-yaml args))

(comment
  ;; For testing at the REPL
  (migrate-yaml "resources/file1.yaml" "resources/file2.yaml" "target/out.yaml")
  (migrate-yaml "cassandra-1.2.12.yaml" "cassandra-2.0.3.yaml" "target/out.yaml"))
