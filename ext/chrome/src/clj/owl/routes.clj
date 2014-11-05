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
  ;(GET "/" request (str request))
  ;(GET "/:id" [id] (str "Hello," id "!"))
  (GET "/repl-demo" [] (repl-demo-page))
  (route/resources "/")
  (route/not-found "<h1>Page not found</h1>"))

(def app
  (-> (handler/site main-routes)
      (wrap-base-url)))
