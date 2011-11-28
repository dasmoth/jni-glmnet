(ns glmnet.demo.server
  (:import (glmnet ClassificationLearner)
	   (cern.colt.matrix.tdouble.impl DenseDoubleMatrix2D))
  (:use clojure.java.io clojure.contrib.json compojure.core ring.adapter.jetty)
  (:require [compojure.route :as route]
	    [compojure.handler :as handler])
  (:gen-class))

(defn- glmnet-classify [x y]
  (let [cls (ClassificationLearner.)]
    (.learn cls y x)))

(defn- lol-to-matrix [l]
  (let [rows (count l)
	cols (count (first l))
	m (DenseDoubleMatrix2D. rows cols)]
    (doseq [r (range rows) :let [rv (nth l r)]]
      (doseq [c (range cols) :let [v (nth rv c)]]
	(.set m r c v)))
    m))
    

(defn- post-classify [req]
  (let [{x :x y :y} (read-json (reader (:body req)))]
    (let [models (glmnet-classify (lol-to-matrix x) (boolean-array y))]
      ; (println "Passes: " (.getNumPasses models))
      ; (println "Fits: " (.getNumFits models))
      ; (println "Weights: " (for [i (range (.size wm))] (.get wm i)))
      {:status 200
       :body (json-str (for [mi (range (.getNumFits models))
			     :let [model (.getModel models mi)
				   wm    (.getWeights model)]]
			 {:intercept (.getIntercept model)
			  :weights   (for [i (range (.size wm))] (.get wm i))}))})))

(defroutes glmnet-demo
  (POST "/v1/classify" [] post-classify)
  (GET "/" [] {:status 302 :headers {"Location" "/classifier.html"}})
  (route/files "/" {:root ".."})
  (route/not-found "Error!"))

(def app (handler/site glmnet-demo))

; (def server-port 8011)

; (defn -main []
;   (run-jetty glmnet-demo {:port server-port}))