(ns owl.web.popup
  (:use [domina :only [by-id value set-value!]])
  (:require [domina.events :as ev]))

(defn say-hello []
  (js/alert "Hello, ClojureScript!"))

(defn add-some-numbers [& numbers]
  (apply + numbers))

(defn ^:export init []
  (if (and js/document
           (.-getElementById js/document))
    (do (.log js/console "init popup.js succeed")
        true)
    (do (.log js/console "init popup.js failed")
        false)))


