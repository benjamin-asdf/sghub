(ns org.sg.sghub.renderer.main
  (:require
   [refx.alpha :as refx :refer [use-sub dispatch]]
   [uix.core.alpha :as uix]
   [uix.dom.alpha :as uix.dom]))

(def debug? ^boolean goog.DEBUG)
;; (def findp (js/require "find-process"))
;; (def ipc-renderer (.-ipcRenderer (js/require "electron")))
;; (def dir (str js/__dirname "/.."))
;; (def fpath (js/require "path"))


(defn button [{:keys [on-click]} text]
  [:button.btn {:on-click on-click :style {:color "cyan"}} text])

(defn create-issue []
  )

(defn app []
  (let [state* (uix/state 0)]
    [:div
     {:style {:background "black" :color "white"}}
     [:h1 "Create issue"]
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
  (uix.dom/render [app] (.getElementById js/document "app")))

(defn ^:dev/after-load clear-cache-and-render!
  []
  (refx/clear-subscription-cache!)
  (render))

(defn ^:export main []
  (refx/dispatch-sync [:initialize])
  (render)
  (dev-setup))

(comment
  (render)
  js/goog.global.window
  (js/console.log "lol")
  (println "hi")
  js/frames

  (def p (nodejs/require "node:child_process"))

  (println
   (str (p.execFileSync "pwd")))
  (js/console.log
   (str (p.execFileSync "pwd")))

  (nodejs/require "path")
  (def path (nodejs/require "path"))
  js/__dirname
  (js/Object.getOwnPropertyNames path)

  )
