(ns yaml-migration.migrate
  (:require [clj-yaml.core :as yaml]
            [clojure.java.io :as io]
            [clojure.data :as d]
            [clojure.string :refer [join]])
  (:import [java.io StringReader]))


(defn comment-lines [^String s]
  (with-open [r (io/reader (StringReader. s))]
    (doall
     (join "\n"
      (map #(str "#" %) (line-seq r))))))

(let [[a-only b-only both]
      (d/diff
       (yaml/parse-string (slurp "resources/file1.yaml"))
       (yaml/parse-string (slurp "resources/file2.yaml")))]


  (spit "target/out.yaml"
        (str (yaml/generate-string both)
             "\n\n## New defaults in this version of Cassandra:\n"
             (yaml/generate-string b-only)
             "\n\n## The following options are no longer valid in this version of Cassandra:\n"
             (comment-lines (yaml/generate-string a-only)))))



(defn -main [[old-file new-file]]
  (let [old-data (yaml/parse-string (slurp old-file))
        new-data (yaml/parse-string (slurp new-file))]
    ))
