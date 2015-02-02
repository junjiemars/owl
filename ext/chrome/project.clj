(defproject owl-web "0.0.1"
  :description "Owl web front"
  :source-paths ["src/clj" "src/cljs" "src/brepl"]
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [org.clojure/clojurescript "0.0-2665"
                  :exclusions [org.apache.ant/ant]]
                 [compojure "1.2.1"]
                 [ring/ring-defaults "0.1.3"]
                 [hiccup "1.0.5"]
                 [domina "1.0.3"]
                 ]
  :plugins [[lein-cljsbuild "1.0.3"]
            [lein-ring "0.9.0" #_ ("0.8.13")
             :exclusions [org.clojure/clojure]]
            [org.clojars.junjiemars/lein-packer "0.1.0"]]
  :hooks [leiningen.cljsbuild]
  :profiles {:dev {
                   :dependencies []
                   :ring {:handler owl.web.routes/app
                          :init owl.web.routes/init
                          :destroy owl.web.routes/destroy
                          :repl-options {:repl-listen-port 9000}}
                   :cljsbuild {:builds
                               [{:source-paths ["src/brepl" "src/cljs"]
                                 :compiler {:output-to
                                            "resources/public/js/main.js"
                                            :optimizations :whitespace
                                            :pretty-print true}}]}
                   :pack {:mapping [{:source-paths ["manifest.json"
                                                    "resources/public"]
                                     :target-path "target/packed"
                                     :excludes [#"\w+\.\w+\~"]}]
                          :target {:type "crx"
                                   :path "target/packed"}}}
             :pro {:cljsbuild {:builds
                               [{:source-paths ["src/cljs"]
                                 :compiler {:output-to
                                            "resources/public/js/main.js"
                                            :optimizations :advanced
                                            :pretty-print false}}]}
                   :pack {:mapping [{:source-paths ["manifest.json"
                                                    "resources/public"]
                                     :target-path "target/packed"
                                     :excludes [#"\w+\.\w+\~"]}]
                          :target {:type "crx"
                                   :path "target/packed"}}}})
