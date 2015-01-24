(ns owl.web.popup
  (:use [domina :only [by-id value set-value! set-attr!]])
  (:require [domina.events :as ev]))

(defonce ^:export proxy-settings (atom {}))
(defonce ^:export proxy-switch (atom 0))

(defn set-link! [id uri]
  (set-attr! (by-id id) :href (.getURL js/chrome.runtime uri)))

(defn load-proxy-uri [uri]
  (let [u uri
        s (.getItem js/localStorage :proxy_uri)]
    (set-value! (by-id "proxy_uri")
                (if (empty? s) u s))))

(defn save-proxy-uri [uri]
  (.setItem js/localStorage :proxy_uri uri))

(defn switch-proxy! []
  (let [ps proxy-switch
        id (by-id "proxy_run")]
    (reset! ps (bit-xor @ps 1))
    (if (zero? @ps)
      (set-value! id "Run ")
      (set-value! id "Stop "))))

(defn apply-proxy-settings! [uri]
  (let [c {:mode "fixed_servers"
           :rules {:singleProxy {:scheme "socks4"
                                  :host "localhost"
                                  :port 11032}}}
        d (clj->js {:value c :scope "regular"})]
    (.set js/chrome.proxy.settings
          d
          (fn [s]
            (.log js/console (js->clj s))
            (swap! proxy-settings
                   (fn [v] (conj v (js->clj proxy-settings))))))
    (.sendRequest js/chrome.extension {:type "clearError"})))

(defn clear-proxy-settings! []
  (let [s (clj->js {:scope "regular"})]
    (.clear js/chrome.proxy.settings s
            (fn [] (.log js/console "#clear-proxy-settings")))))

(defn restore-proxy-settings! [settings]
  (let [s settings
        m (swap! s dissoc :levelOfControl)]
    (.log js/console (clj->js @s))
    (.log js/console (clj->js m))
    (.set js/chrome.proxy.settings
          (clj->js m)
          (fn [] (.log js/console "#restored")))))

(defn on-proxy-run [e]
  (when-let [uri (value (by-id "proxy_uri"))]
    (.preventDefault e.evt)
    (.stopPropagation e.evt)
    (switch-proxy!)
    (if (not (zero? @proxy-switch))
      (do (save-proxy-uri uri)
          (.getSelected js/chrome.tabs
                        (fn [t] (.log js/console t.url)))
          (apply-proxy-settings! uri))
      (clear-proxy-settings!))))

(defn on-doc-ready []
  (when-let [ready-state (.-readyState js/document)]
    (if (and (= "complete" ready-state)
             (by-id "popup"))
      (do 
          (.log js/console "#popup:on-doc-ready")
          (set-link! "options_link" "options.html")
          (set-link! "echo_link" "resources/public/echo.html")
          (load-proxy-uri "http://localhost:9001")
          (ev/listen! (by-id "proxy_run") :click on-proxy-run)
        true)
      false)))

(set! (.-onreadystatechange js/document) on-doc-ready)

