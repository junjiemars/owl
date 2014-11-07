(ns owl.web.repl
  (:require [clojure.browser.repl :as repl]))

(defn connect []
  (repl/connect "http://localhost:9000/repl"))

(defn ^:export init []
  (.log js/console "inited....")
  (connect))
