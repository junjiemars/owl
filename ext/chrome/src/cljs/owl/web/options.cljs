(ns owl.web.options
  (:use [domina :only [by-id
                       value
                       set-value!
                       set-attr!
                       set-text!
                       set-style!]])
  (:require [domina.events :as ev]
            [owl.web.popup :as p]
            [owl.web.core :as c]
            [clojure.string :as str]))

(defonce ^:export options-ui {:div (by-id "options")
                              :urls (by-id "excluded_urls")
                              :save (by-id "save_button")})

(defn on-save! [e]
  (when-let [u (:urls options-ui)]
    (let [b (str/split (value u) #"[,; \n]")
          s (vec (remove str/blank? b))]
      (c/save-item :proxy_options s)
      (p/apply-proxy-settings! (p/make-proxy-settings s)))))

(set-value! (:urls options-ui)
            (apply str (interpose "; " (c/load-item :proxy_options))))


(ev/listen! (:save options-ui) :click on-save!)

(.log js/console "#options loaded")


