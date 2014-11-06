(ns owl.repl.connect
  (:require [clojure.browser.repl :as repl]))

(repl/connect "http://localhost:9000/repl")

(defn connect []
  (repl/connect "http://localhost:9000/repl"))
