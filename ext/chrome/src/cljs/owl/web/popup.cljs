(ns owl.web.popup
  (:use [domina :only [by-id value set-value! set-attr!]])
  (:require [domina.events :as ev]))

(defn set-link! [id uri]
  (set-attr! (by-id id) :href (.getURL js/chrome.runtime uri)))

(defn load-proxy-uri! [uri]
  (let [u uri
        s (.getItem js/localStorage :proxy_uri)]
    (set-value! (by-id "proxy_uri")
                (if (> (count s) 0) s u))))

(defn set-chrome-proxy! [uri]
  (let [c {:mode "fixed_servers"
           :rules {:proxyForHttp {:scheme "socks5"
                                 :host uri}}}]
    (.set js/chrome.proxy.settings
          {:value c :scope "regular"}
          #(.log js/console (str "####" %)))))

(defn on-proxy-run []
  (let [uri (value (by-id "proxy_uri"))]
    (.log js/console "begin run proxy")
    (.setItem js/localStorage
              :proxy_uri
              uri)
    (.getSelected js/chrome.tabs
                  (fn [t] (.log js/console t.url)))
    (set-chrome-proxy! uri)))

(defn on-doc-ready
  []
  (let [ready-state (.-readyState js/document)]
    (if (and (= "complete" ready-state)
             (by-id "popup"))
      (do 
          (.log js/console "#popup:on-doc-ready")
          (set-link! "options_link" "resources/public/options.html")
          (set-link! "echo_link" "resources/public/echo.html")
          (load-proxy-uri! "http://localhost:9001")
          (ev/listen! (by-id "proxy_run") :click on-proxy-run)
        true)
      false)))

(set! (.-onreadystatechange js/document) on-doc-ready)
