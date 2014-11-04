(defproject owl-web "0.0.1"
  :description "Owl web front"
  :source-paths ["src/clj" "src/cljs"]
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [org.clojure/clojurescript "0.0-2371"
                  :exclusions [org.apache.ant/ant]]
                 [compojure "1.1.6"]
                 [hiccup "1.0.5"]]
  :plugins [[lein-cljsbuild "1.0.3"]
            [lein-ring "0.8.8"]]
  :hooks [leiningen.cljsbuild]
  :cljsbuild {
    :repl-listen-port 9000
    :builds {
             :dev {
               :source-paths ["src/cljs"]
               :compiler {
                 :output-to "resources/public/main-dev.js"
                 :optimizations :whitespace
                 :pretty-print true}}}
             }
  :ring {:handler owl.routes/app}
  )
