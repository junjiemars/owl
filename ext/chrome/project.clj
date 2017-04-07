(defproject owl-web "0.1.0"
  
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [org.clojure/clojurescript "1.9.473"]
                 [domina "1.0.3"]]
  
  :plugins [[lein-figwheel "0.5.9"]
            [lein-cljsbuild "1.1.5"]
            [lein-packer "0.1.0"]]
  
  :clean-targets ^{:protect false} ["resources/public/js"
                                    "resources/public/js/owl.js"
                                    :target-path]
  :profiles {:dev {:dependencies [[figwheel-sidecar "0.5.9"]
                                  [com.cemerick/piggieback "0.2.1"]]
                   :cljsbuild
                   {:builds [{:id "dev"
                              :source-paths ["src" "externs"]
                              :figwheel {
                                         :open-urls ["http://localhost:3449/popup.html"]
                                         }
                              :compiler {:main "owl.web.core"
                                         :externs ["chrome_extensions.js"]
                                         :asset-path "js/out"
                                         :output-to "resources/public/js/owl.js"
                                         :output-dir "resources/public/js/out"
                                         :optimizations :none
                                         }}]
                    }
                   }
             :pro {:cljsbuild
                   {:builds
                    [{:source-paths ["src" "externs"]
                      :compiler {:externs ["chrome_extensions.js"]
                                 :output-to "resources/public/js/owl.js"
                                 ;; :closure-output-charset "US-ASCII"
                                 :optimizations :advanced
                                 :pretty-print false}
                      }]}
                   
                   :pack {:mapping [{:source-paths ["manifest.json"
                                                    "resources/public"]
                                     :target-path "target/pro"
                                     :excludes [#"\w+\.\w+\~"]}]
                          :target {:type "crx"
                                   :path "target/pro"}}}
             }
  
  
  :figwheel {
             :css-dirs ["resources/public/css"]
             :open-file-command "emacsclient"
             :server-logfile ".figwheel.log"
             }
  
  :aliases {"pro"     ["with-profile" "pro"
                       "do"
                       ["clean"]
                       ["cljsbuild" "once"]]
            "pack"     ["with-profile" "pro"
                        "do"
                        ["clean"]
                        ["cljsbuild" "once"]
                        ["packer" "once"]]}
  )

