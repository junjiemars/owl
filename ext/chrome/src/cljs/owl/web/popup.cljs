(ns owl.web.popup
  (:use [domina :only [by-id value set-value! set-attr!]])
  (:require [domina.events :as ev]))

(defonce ^:export default-proxy-settings (atom {}))

(defn set-link! [id uri]
  (set-attr! (by-id id) :href (.getURL js/chrome.runtime uri)))

(defn load-proxy-uri! [uri]
  (let [u uri
        s (.getItem js/localStorage :proxy_uri)]
    (set-value! (by-id "proxy_uri")
                (if (> (count s) 0) s u))))

(defn on-set-proxy [details]
  (let [d details
        s default-proxy-settings]
    (.log js/console (js->clj d))
    (swap! s
           (fn [v] (conj v (js->clj d))))
    (.log js/console (clj->js @s))))

(defn set-proxy-settings! [uri]
  (let [c {:mode "fixed_servers"
           :rules {:proxyForHttp {:scheme "http"
                                  :host uri}}}
        j (clj->js {:value c :scope "regular"})]
    (.log js/console j)
    (.set js/chrome.proxy.settings
          j
          on-set-proxy)))

(defn restore-proxy-settings! [settings]
  (let [s settings
        m (swap! s dissoc :levelOfControl)]
    (.log js/console (clj->js @s))
    (.log js/console (clj->js m))
    (.set js/chrome.proxy.settings
          (clj->js m)
          (fn [](.log js/console "#restored")))))

(defn on-proxy-run []
  (let [uri (value (by-id "proxy_uri"))]
    (.log js/console "#on-proxy-run")
    (.setItem js/localStorage
              :proxy_uri
              uri)
    (.getSelected js/chrome.tabs
                  (fn [t] (.log js/console t.url)))))
    ;(set-proxy-settings! uri)))

(defn on-doc-ready
  []
  (when-let [ready-state (.-readyState js/document)]
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

