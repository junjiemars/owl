(ns owl.web.popup
  (:use [domina :only [by-id
                       value
                       set-value!
                       set-attr!
                       set-text!]])
  (:require [domina.events :as ev]))

(defonce ^:export proxy-settings (atom {:value {:mode "fixed_servers"
                                                :rules {:singleProxy
                                                        {:scheme "socks5"
                                                         :host "localhost"
                                                         :port 11032}}}
                                        :scope "regular"}))
(defonce ^:export proxy-switch (atom false))
(defonce ^:export proxy-types {:raw [:auto_detect
                                     :pac_script
                                     :direct
                                     :fixed_servers
                                     :system]
                               :run ["pac_script"
                                     "fixed_servers"]})

(defn set-link! [id uri]
  (set-attr! (by-id id) :href (.getURL js/chrome.runtime uri)))

(defn make-proxy-settings! [uri]
  (let [u (re-seq #"(\w+)://([\w\.]+):(\d+)" uri)
        ss (first u)
        n (assoc-in @proxy-settings [:value :rules :singleProxy]
                    {:scheme (second ss)
                     :host (nth ss 2)
                     :port (js/parseInt (last ss))})]
    (reset! proxy-settings n)))

(defn proxy-settings-to-uri [s]
  (let [u (:singleProxy (:rules (:value s)))
        uri (str (:scheme u) "://"
                 (:host u) ":"
                 (:port u))]
    uri))

(defn switch-proxy! []
  (let [g (clj->js {:incognito false})]
    (.get js/chrome.proxy.settings
          g (fn [d]
              (let [c (js->clj d :keywordize-keys true)
                    m (:mode (:value c))
                    b (by-id "proxy_run")
                    i (by-id "proxy_indicator")]
                (.log js/console c)
                (.log js/console m)
                (set-value! (by-id "proxy_uri")
                            (proxy-settings-to-uri c))
                (if (some #(= m %) (:run proxy-types))
                  (do
                    (set-text! i "&#x25f7;")
                    (set-value! b "Stop")
                    (reset! proxy-switch true))
                  (do
                    (set-text! i "")
                    (set-value! b "Run ")
                    (reset! proxy-switch false))))))))

(defn apply-proxy-settings! [e]
  (let [c (make-proxy-settings! (value (by-id "proxy_uri")))
        d (clj->js c)]
    (.preventDefault e.evt)
    (.stopPropagation e.evt)
    (.log js/console d)
    (.set js/chrome.proxy.settings
          d (fn [s]
              (.log js/console (js->clj s))))
    (.sendRequest js/chrome.extension {:type "clearError"})))

(defn clear-proxy-settings! []
  (let [s (clj->js {:scope "regular"})]
    (.clear js/chrome.proxy.settings s
            (fn [] (.log js/console "#clear-proxy-settings")))))

(defn on-proxy-run! [e]
  (let [s @proxy-switch]
    (if (true? s)
      (do
        (clear-proxy-settings!))
      (do
        (apply-proxy-settings! e)))
    (switch-proxy!)))

(defn on-doc-ready []
  (when-let [ready-state (.-readyState js/document)]
    (if (and (= "complete" ready-state)
             (by-id "popup"))
      (do 
          (.log js/console "#popup:on-doc-ready")
          (set-link! "options_link" "resources/public/options.html")
          (set-link! "echo_link" "resources/public/echo.html")
          (switch-proxy!)
          (ev/listen! (by-id "proxy_run") :click on-proxy-run!)
        true)
      false)))

(set! (.-onreadystatechange js/document) on-doc-ready)
