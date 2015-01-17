(ns owl.web.brepl
  (:require [clojure.browser.repl :as repl]
            ;[ring.adapter.jetty :as ws]
            [owl.web.routes :as route]))

(defn connect []
  (repl/connect "http://localhost:9000/repl"))

(defn ^:export init []
  (do (connect)
      (.log js/console "#inited owl.web.brepl")
      true))

#_ ((defn run []
       (defone ^:private server
         (ws/run-jetty #'route/app-routes
                       {:port 3000
                        :join? false}))))

(defn run []
  (route/app-routes))
