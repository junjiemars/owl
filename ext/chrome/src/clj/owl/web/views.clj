>(ns owl.web.views
  (:require
    [hiccup
      [page :refer [html5]]
      [element :refer [javascript-tag]]
      [page :refer [include-js]]])
  (:gen-class))

(defn- run-clojurescript [path init]
  (list
    (include-js path)
    (javascript-tag init)))

(defn index-page []
  (html5
    [:head
      [:title "Owl - Web Front"] ]
    [:body
      [:h1 "Owl, Hello"]
      (run-clojurescript
        "main-dev.js" 
        "owl.web.core.say_hello()")]))

(defn repl-demo-page []
  (html5
    [:head
      [:title "REPL Demo"]]
    [:body
      [:h1 "REPL Demo"]
      [:hr]
      "This page is meant to be accessed by running this in one terminal:"
      [:pre "lein ring server-headless 3000"]
      "And then this in a different terminal:"
      [:pre "lein trampoline cljsbuild repl-launch firefox http://localhost:3000/repl-demo"]
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
      (run-clojurescript
        "main-dev.js"
        "owl.web.repl.init()")))
