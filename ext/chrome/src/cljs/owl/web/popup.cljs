(ns owl.web.popup
  (:use [domina :only [by-id value set-value! set-attr!]])
  (:require [domina.events :as ev]))

(defn set-link! [id uri]
  (set-attr! (by-id id) :href (.getURL js/chrome.runtime uri)))

(defn set-proxy-uri! [uri]
  (let [u uri
        s (.getItem js/localStorage :proxy_uri)]
    (set-value! (by-id "proxy_uri")
                (if (> (count s) 0) s u))))

(defn on-proxy-uri-change! [event]
  (let [e event]
    (.log js/console e)
    (.log js/console (.getBackgroundPage js/chrome.extension))))

(defn on-proxy-run []
  (.log js/console "begin run proxy")
  (.getSelected js/chrome.tabs (fn [t] (.log js/console t.url))))
                                 

(defn on-doc-ready
  []
  (let [ready-state (.-readyState js/document)]
    (if (and (= "complete" ready-state)
             (by-id "popup"))
      (do 
          (.log js/console "#popup:on-doc-ready")
          ;(set-options-link! "resources/public/options.html")
          (set-link! "options_link" "resources/public/options.html")
          (set-link! "echo_link" "resources/public/echo.html")
          (set-proxy-uri! "http://localhost:9001")
          (ev/listen! (by-id "proxy_uri") :onchange on-proxy-uri-change!)
          (ev/listen! (by-id "proxy_run") :click on-proxy-run)
        true)
      false)))

(set! (.-onreadystatechange js/document) on-doc-ready)
