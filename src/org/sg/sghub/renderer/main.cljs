(ns org.sg.sghub.renderer.main
  (:require
   [refx.alpha :as refx :refer [use-sub dispatch]]
   [uix.core.alpha :as uix]
   [uix.dom.alpha :as uix.dom]))

(def debug? ^boolean goog.DEBUG)

(defn button [{:keys [on-click]} text]
  [:button.btn {:on-click on-click}
   text])

(defn app []
  (let [state* (uix/state 0)]
    [:div
     [:h1 "Hurr"]
     [:<>
      [button
       {:on-click #(swap! state* dec)} "-"]
      [:span @state*]
      [button
       {:on-click #(swap! state* inc)} "+"]]]))

(defn dev-setup
  []
  (when debug?
    (println "Development mode")))

(defn render
  []
  (println "Inside render")
  (uix.dom/render [app] (.getElementById js/document "app")))

(defn ^:dev/after-load clear-cache-and-render!
  []
  (println "Inside after load")
  (refx/clear-subscription-cache!)
  (render))

(defn ^:export main []
  (refx/dispatch-sync [:initialize])
  (render)
  (dev-setup))

(comment
  js/goog.global.window
  (js/console.log "lol")
  (println "hi"))
