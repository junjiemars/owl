(defproject owl-web "0.1.0"
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [org.clojure/clojurescript "1.9.229"]
                 [domina "1.0.3"]]
  :plugins [[lein-figwheel "0.5.9"]
            [lein-cljsbuild "1.1.5"]]
  :clean-targets ^{:protect false} ["resources/public/js"
                                    "resources/public/js/owl.js"
                                    :target-path]
  :profiles {:dev {:dependencies [[figwheel-sidecar "0.5.9"]
                                  [com.cemerick/piggieback "0.2.1"]]}
             }
  :repl-options {:nrepl-middleware [cemerick.piggieback/wrap-cljs-repl]}
  :cljsbuild {
              :builds {
                       :dev {:source-paths ["src"]
                             :compiler {:main "owl.web.core"
                                        :externs
                                        ["external/chrome_extensions.js"]
                                        :asset-path "js/out"
                                        :output-to "resources/public/js/owl.js"
                                        :output-dir "resources/public/js/out"
                                        :optimizations :none
                                        }
                             :figwheel {:open-urls ["http://localhost:3449/popup.html"]
                                        }
                             }
                       :pro {:source-paths ["src"]
                             :compiler {:externs
                                        ["external/chrome_extensions.js"]
                                        :output-to "resources/public/js/owl.js"
                                        :optimizations :advanced
                                        :pretty-print false}
                             }
                       }
              }
  :figwheel {:css-dirs ["resources/public/css"]
             :open-file-command "emacsclient"}
  )

