(ns owl.web.popup
  (:use [domina :only [by-id value set-value! set-attr!]])
  (:require [domina.events :as ev]))

(defn set-options-link! [uri]
  (let [u uri]
    (set-attr! (by-id "options_link")
               :href
               (.getURL js/chrome.runtime u))))

(defn set-proxy-uri! [uri]
  (let [u uri
        s (.getItem js/localStorage :proxy_uri)]
    (set-value! (by-id "proxy_uri")
                (if (> (count s) 0) s u))))

(defn on-proxy-uri-change! [event]
  (let [e event]
    (.log js/console e)))

(defn on-doc-ready []
  (let [ready-state (.-readyState js/document)]
    (if (= "complete" ready-state)
      (do (set-options-link! "resources/public/options.html")
          (set-proxy-uri! "http://localhost:9001")
          (ev/listen! (by-id "proxy_uri") :onchange on-proxy-uri-change!)
        true)
      false)))

(set! (.-onreadystatechange js/document) on-doc-ready)
