(ns owl.web.core
  (:use [domina :only [by-id value set-value!]])
  (:require [domina.events :as ev]))

(defn say-hello []
  (js/alert "Hello, ClojureScript!"))

(defn add-some-numbers [& numbers]
  (apply + numbers))

(defn validate-form []
  (let [email (by-id "email") 
        passwd (by-id "password")]
    (if (and (> (count (value email)) 0)
             (> (count (value passwd)) 0))
      (do (.log js/console "gOOd!")
          true)
      (do (.log js/console "please complete the form")
          false))))

(defn ^:export init []
  (if (and js/document
           (.-getElementById js/document))
    (let [login (by-id "login_form")]
      (ev/listen! (by-id "submit") :click validate-form))))
      ;(set! (.-onsubmit login) validate-form))))


