(ns owl.routes
  (:use compojure.core
        owl.views
        [hiccup.middleware :only (wrap-base-url)])
  (:require [compojure.route :as route]
            [compojure.handler :as handler]
            [compojure.response :as response])
  (:gen-class))

(defroutes main-routes
  (GET "/" [] "<p>Hello,Owl is cOOl!</p>");;(index-page))
  (GET "/repl-demo" [] (repl-demo-page))
  (route/resources "/")
  (route/not-found "Page not found"))

(def app
  (-> (handler/site main-routes)
      (wrap-base-url)))
