(ns owl.web.options
  (:use [domina :only [by-id
                       value
                       set-value!
                       set-attr!
                       set-text!
                       set-style!]])
  (:require [domina.events :as ev]
            [owl.web.popup :as p]))

(defonce ^:export options-ui {:div (by-id "options")
                              :urls (by-id "excluded_urls")
                              :restore (by-id "restore_button")
                              :save (by-id "save_button")})

(defn on-restore! [e]
  (.log js/console "#clicked restore button"))

(defn on-save! [e]
  (.log js/console "#clicked save button"))

(ev/listen! (:restore options-ui) :click on-restore!)
(ev/listen! (:save options-ui) :click on-save!)

(.log js/console "#options loaded")
(.log js/console (clj->js p/proxy-settings))
(.log js/console (.stringify js/JSON (clj->js p/proxy-settings)))
