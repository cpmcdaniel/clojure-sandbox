(ns redirect
  (:require
   [ring.adapter.jetty :as jetty]
   [ring.util.response :as response]))

(defn redirect-to-github
  [req]
  {:status 307
   :headers {"Location" "http://github.com"}
   :body ""})

(defn -main []
  ;; Run the server on port 3000
  (jetty/run-jetty redirect-to-github {:port 3000}))
