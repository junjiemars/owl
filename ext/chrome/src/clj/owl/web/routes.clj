(ns owl.web.routes
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]])
  (:require [owl.web.views :as v])
  (:gen-class))

(defn init []
  (println "#init lein ring..."))

(defn destroy []
  (println "#destroy lein ring..."))

(defroutes app-routes
  (GET "/" [] "<p>Hello,Owl is cOOl!</p>")
  (GET "/repl" [] (v/repl-demo-page))
  (route/not-found "<h1>Page not found</h1>"))

(def app
  (wrap-defaults app-routes site-defaults))
