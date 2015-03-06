(ns owl.web.popup
  (:use [domina :only [by-id
                       value
                       set-value!
                       set-attr!
                       set-text!
                       set-style!]])
  (:require [domina.events :as ev]
            [owl.web.core :as c]))

(defonce ^:export proxy-settings (atom {:value {:mode ""
                                                :rules {}
                                                :pacScript {}}
                                        :scope "regular"}))
(defonce ^:export proxy-types {:raw [:auto_detect
                                     :pac_script
                                     :direct
                                     :fixed_servers
                                     :system]
                               :run ["pac_script"
                                     "fixed_servers"]})
(defonce ^:export proxy-ui {:div (by-id "popup")
                            :button-run (by-id "uri_button")
                            :input-uri (by-id "uri_input")
                            :link-options (by-id "options_link")})

(declare on-proxy-error!)

(defn url-to-proxy-settings! [uri]
  (when-let [u (re-find #"(\w+)://([\w\.]+)(:(\d+))?(/(\w+\.\w+))?" uri)]
    (let [p {:url (first u)
             :scheme (nth u 1)
             :host (nth u 2)
             :port (nth u 4)
             :has-wpad? (not (nil? (nth u 6)))}
          s (if (:has-wpad? p)
              (assoc-in @proxy-settings [:value]
                        {:mode "pac_script"
                         :pacScript {:url (:url p)
                                     :mandatory true}})
              (assoc-in @proxy-settings [:value]
                        {:mode "fixed_servers"
                         :rules {:singleProxy
                                 {:scheme (:scheme p)
                                  :host (:host p)
                                  :port (js/parseInt (:port p))}
                                 :bypassList []}}))]
      (assoc-in s [:scope] "regular"))))

(defn proxy-settings-to-url [s]
  (when-let [p (:value s)]
    (let [m (:mode p)]
      (case m
        "pac_script" (:url (:pacScript p))
        "fixed_servers" (let [u (:singleProxy (:rules p))]
                         (str (:scheme u) "://"
                              (:host u) ":"
                              (:port u)))
        nil))))

(defn set-ui-state! [switch]
  (let [run (:button-run proxy-ui)
        url-style (:input-uri proxy-ui)
        style {:input-background-color (if switch "RoyalBlue" "")
               :input-font-color (if switch "White" "")
               :button-text (if switch "Stop" "Run ")}]
    (set-value! run (:button-text style))
    (set-style! url-style :background-color (:input-background-color style))
    (set-style! url-style :color (:input-font-color style))))

(defn set-ui-url! [url]
  (set-value! (:input-uri proxy-ui) url))

(defn restore-proxy-settings! []
  (let [g (clj->js {:incognito false})]
    (.get js/chrome.proxy.settings
          g (fn [d]
              (let [c (js->clj d :keywordize-keys true)
                    m (:mode (:value c))
                    b (:button-run proxy-ui)
                    u (:input-uri proxy-ui)
                    running? (some #(= m %) (:run proxy-types))]
                (set-ui-state! running?)
                (set-ui-url! (proxy-settings-to-url
                              (if running?
                                c
                                (c/load-item :proxy_settings)))))))))

(defn apply-proxy-settings! [settings]
  (when-let [s (clj->js settings)]
    (.. js/chrome.proxy -onProxyError (addListener on-proxy-error!))
    (.set js/chrome.proxy.settings
          s (fn [d]
              (reset! proxy-settings settings)
              (c/save-item :proxy_settings s)))
    (.sendRequest js/chrome.extension {:type "clearError"})))

(defn clear-proxy-settings! []
  (let [d (clj->js {:scope "regular"})]
    (.clear js/chrome.proxy.settings
            d (fn [] (.log js/console "#clear-proxy-settings")))))

(defn alert [msg close?]
  (when-let [div (.createElement js/document "div")]
    (.add (.-classList div) "overlay")
    (.setAttribute div "role" "alert")
    (set! (.-textContent div) msg)
    (.appendChild (.-body js/document) div)
    (js/setTimeout (fn []
                     (.add (.-classList div) "visible"))
                   10)
    (js/setTimeout (fn []
                     (if close?
                       (.remove (.-classList div) "visible")
                       (.close js/window)))
                   4000)))

(defn make-proxy-settings
  ([] (when-let [u (value (:input-uri proxy-ui))]
        (let [s (url-to-proxy-settings! u)
              bypass? (:rules (:value s))
              o (when bypass? (c/load-item :proxy_options))]
          (if (and  bypass? o)
            (assoc-in s [:value :rules :bypassList] o)
            s))))
  ([options] (when-let [s (c/load-item :proxy_settings)]
               (let [bypass? (:rules (:value s))]
                 (when bypass?
                   (assoc-in s [:value :rules :bypassList] options))))))

(defn on-proxy-run! [e]
  (let [running? (= "Stop" (value (:button-run proxy-ui)))]
    (if running?
      (clear-proxy-settings!)
      (do
        (.preventDefault e.evt)
        (.stopPropagation e.evt)
        (apply-proxy-settings! (make-proxy-settings))))
    (set-ui-state! (not running?))))

(defn on-proxy-error! [e]
  (when-let [d (js->clj e :keywordize-keys true)]
    (.log js/console (.stringify js/JSON (clj->js e)))
    ;(alert (str d) true)
    ))

(defn on-doc-ready []
  (when-let [ready-state (.-readyState js/document)]
    (when (and (= "complete" ready-state)
             (:div proxy-ui))
      (set-attr! (:link-options proxy-ui)
                 :href (.getURL js/chrome.runtime "options.html"))
      (restore-proxy-settings!)
      (ev/listen! (:button-run proxy-ui) :click on-proxy-run!))))

(set! (.-onreadystatechange js/document) on-doc-ready)
