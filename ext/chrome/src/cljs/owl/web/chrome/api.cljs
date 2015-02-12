(ns owl.web.chrome.api)

(defn ^:export get-url [uri]
  (.getURL js/chrome.runtime uri))
