(defproject glmnet-demo-server "0.0.1-SNAPSHOT"
  :description "Backend for web-based glmnet demos"
  :dependencies [[org.clojure/clojure "1.2.0"]
                 [org.clojure/clojure-contrib "1.2.0"]
		 [compojure "0.6.5"]
		 [incanter/parallelcolt "0.9.4"]]
  :dev-dependencies [[lein-ring "0.4.6"]]
  :ring {:handler glmnet.demo.server/app}
  :keep-non-project-classes true
  :jvm-opts ["-Djava.library.path=."]
  :main glmnet.demo.server)
