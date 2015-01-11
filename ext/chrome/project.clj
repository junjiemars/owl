(defproject owl-web "0.0.1"
  :description "Owl web front"
  :source-paths ["src/clj" "src/cljs" "src/brepl"]
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [org.clojure/clojurescript "0.0-2371"
                  :exclusions [org.apache.ant/ant]]
                 [compojure "1.2.1"]
                 [hiccup "1.0.5"]
                 [domina "1.0.3"]
                 ]
  :plugins [[lein-cljsbuild "1.0.3"]
            [lein-ring "0.9.0" #_ ("0.8.13")]
            ;[hiccup-bridge "1.0.1"]
            ]
  :hooks [leiningen.cljsbuild]
  :cljsbuild {
    :repl-listen-port 9000
    :builds {
             :dev {
               :source-paths ["src/cljs" "src/brepl"]
               :compiler {
                 :output-to "resources/public/js/main-dev.js"
                 :optimizations :whitespace
                 :pretty-print true}}
             :pro {
               :source-paths ["src/cljs"]
               :compiler {
                 :output-to "resources/public/js/main.js"
                 :optimizations :advanced
                 :pretty-print false}}}
             }
  :ring {:handler owl.web.routes/application}
  )


