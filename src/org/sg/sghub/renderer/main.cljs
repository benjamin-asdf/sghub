(ns org.sg.sghub.renderer.main
  (:require [reagent.dom :as r]
            [re-frame.core :as rf]
            [org.sg.sghub.renderer.events]))

(def debug? ^boolean goog.DEBUG)

(defn dev-setup
  []
  (when debug?
    (println "Development mode")))

(defn send-message
  [item]
  (println (str "About to send " item))
  (.api.send js/window (:channel item) (clj->js {:title   (:name item)
                                                 :message (:message item)
                                                 :detail  (:detail item)})))
(defn get-value
  [element]
  (-> element .-target .-value))

(defn main-component
  []
  [:h1 "hello"])

(defn render
  []
  (println "Inside render")
  (r/render [main-component]
    (.getElementById js/document "app")))

(defn ^:dev/after-load clear-cache-and-render!
  []
  (println "Inside after load")
  (rf/clear-subscription-cache!)
  (render))

(defn ^:export main
  []
  (println "Inside main")
  (rf/dispatch-sync [:initialize])
  (dev-setup)
  (render))

(comment


  (js/console.log "lol")

  (println "hi")

  )
