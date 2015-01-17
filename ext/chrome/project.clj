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
            ]
  :hooks [leiningen.cljsbuild]
  :ring {:handler owl.web.routes/app
         :init owl.web.routes/init
         :destroy owl.web.routes/destroy}
  :profiles {:dev {;:plugins [[com.cemerick/austin "0.1.6"]]
                   :repl-options {:repl-listen-port 9000}
                   :cljsbuild {:builds
                               [{:source-paths ["src/cljs" "src/brepl"]
                                 :compiler {:output-to
                                            "resources/public/js/main-dev.js"
                                            :optimizations :whitespace
                                            :pretty-print true}}]}}
             :pro {:cljsbuild {:builds
                               [{:source-paths ["src/cljs"]
                                 :compiler {:output-to
                                            "resources/public/js/main.js"
                                            :optimizations :advanced
                                            :pretty-print false}}]}}})
