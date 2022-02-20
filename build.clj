(ns build
  (:require [clojure.tools.build.api :as b]
            [org.corfield.build :as bb]))

(def lib 'com.cnuernber/jarrow)

(def version "1.000")


(defn deploy
  []
  (bb/jar {:lib lib :version version})
  (bb/deploy {:lib lib :version version}))
