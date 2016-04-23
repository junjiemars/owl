(defproject owl-web "0.0.1"
  :description "Owl web front"
  :source-paths ["src/clj" "src/cljs" "src/brepl"]
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [org.clojure/clojurescript "1.8.40"
                  :exclusions [org.apache.ant/ant]]
                 [compojure "1.5.0"]
                 [ring/ring-defaults "0.2.0"]
                 [hiccup "1.0.5"]
                 [domina "1.0.3"]]
  :plugins [[lein-cljsbuild "1.1.3"]
            [lein-ring "0.9.7"
             :exclusions [org.clojure/clojure]]
            [lein-packer "0.1.0"]]
  :hooks [leiningen.cljsbuild]
  :profiles {:dev {
                   :dependencies []
                   :ring {:handler owl.web.routes/app
                          :init owl.web.routes/init
                          :destroy owl.web.routes/destroy
                          :repl-options {:repl-listen-port 9000}}
                   :cljsbuild {:builds
                               [{:source-paths ["src/brepl" "src/cljs"]
                                 :compiler {:externs
                                            ["externs/chrome_extensions.js"]
                                            :output-to
                                            "resources/public/js/main.js"
                                            :optimizations :whitespace
                                            :pretty-print true}
                                 :notify-command ["lein" "packer" "once"]
                                 }]}
                   :pack {:mapping [{:source-paths ["manifest.json"
                                                    "resources/public"]
                                     :target-path "target/dev"
                                     :excludes [#"\w+\.\w+\~"]}]
                          :target {:type "crx"
                                   :path "target/dev"}}}
             :pro {:cljsbuild {:builds
                               [{:source-paths ["src/cljs"]
                                 :compiler {:externs
                                            ["externs/chrome_extensions.js"]
                                            :output-to
                                            "resources/public/js/main.js"
                                            :optimizations :advanced
                                            :pretty-print false
                                            :warnings true}}]}
                   :pack {:mapping [{:source-paths ["manifest.json"
                                                    "resources/public"]
                                     :target-path "target/pro"
                                     :excludes [#"\w+\.\w+\~"]}]
                          :target {:type "crx"
                                   :path "target/pro"}}}})
