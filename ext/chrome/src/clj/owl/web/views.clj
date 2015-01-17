(ns owl.web.views
  (:require [hiccup
             [page :refer [html5 include-js]]
             [element :refer [javascript-tag]]]
            [net.cgrand.enlive-html :as enlive])
  (:gen-class))

(defn- run-clojurescript [path init]
  (list
    (include-js path)
    (javascript-tag init)))

(defn repl-demo-page []
  (html5
    [:head
      [:title "OWL -REPL Demo"]]
    [:body
      [:h1 "REPL Demo"]
      [:hr]
      "This page is meant to be accessed by running this in one terminal:"
      [:pre "lein ring server-headless 3000"]
      "And then this in a different terminal:"
      [:pre "lein trampoline cljsbuild repl-launch http://localhost:3000/repl"]
      [:hr]
      "Alternately, you can run:"
      [:pre "lein ring server-headless 3000 &
lein trampoline cljsbuild repl-listen"]
      "And then browse to this page manually."]
      [:hr]
      [:h2 {:id "fun"} "Try some fun REPL commands!"]
      [:pre "> (js/alert \"Hello!\")
> (load-namespace 'goog.date.Date)
> (js/alert (goog.date.Date.))
> (.log js/console (reduce + [1 2 3 4 5]))
> (load-namespace 'goog.dom)
> (goog.dom.setTextContent (goog.dom.getElement \"fun\") \"I changed something....\") "]
      #_ ((run-clojurescript
            "main-dev.js"
            "owl.web.brepl.init()"))
      [:script (browser-connected-repl-js)]))
