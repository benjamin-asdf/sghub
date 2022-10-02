(ns org.sg.sghub.main
  (:require
   ["electron" :as electron :refer
    [app shell BrowserWindow dialog ipcMain Menu]]
   ["process" :as process]
   [cljs.nodejs :as nodejs]))

(def path (nodejs/require "path"))
(def url (nodejs/require "url"))
(def main-window (atom nil))
(defn- ^js/electron.BrowserWindow get-main-window []
  @main-window)

;; (def ls (js/require "electron-localstorage"))

(defn show-dialog
  [data]
  (let [message (js->clj data :keywordize-keys true)
        options (clj->js {:type      "info"
                          :buttons   ["Ok"]
                          :defaultId 0
                          :icon      (.join path (js* "__dirname") "assets" "korag.png")
                          :title     (or (str (:title message)) "No title")
                          :message   (or (str (:message message)) "No message")})]
    (.showMessageBox dialog (get-main-window) options #())))

(defn create-menu-template
  []
  (let [darwin? (= (.-platform process) "darwin")]
    [(when darwin?
       {:label   (str (.-name app))
        :submenu [{:role "about"}
                  {:label   (str "Version " (.getVersion app))
                   :enabled true}
                  {:type "separator"}
                  {:role "quit"}]})
     {:label "View"
      :submenu [{:role "toggledevtools"}]}
     {:role "help"
      :submenu
      [{:label "Documentation"
        :click (fn []
                 (.openExternal shell "https://www.github.com"))}
       {:type "separator"}
       {:label "Bad documentation link"
        :click (fn []
                 (.openExternal shell "https://www.example.com"))}]}]))



     ;; :width (max (or (.getItem ls "width") 800) 800)
     ;; :height (max (or (.getItem ls "height") 600) 600)
     ;; :titleBarStyle "hiddenInset"

(defn create-browser-window
  []
  (BrowserWindow.
   (clj->js
    {:title          "sghub"
     :transparent    true
     :alwaysOnTop    true
     :frame          false
     :opacity        0.75
     :webPreferences
     {:zoomFactor 1.5
      :nodeIntegration true
      ;; :nodeIntegrationInWorker true
      ;; :nodeIntegrationInSubFrames true
      }
     :show           false}))
  ;; (BrowserWindow.
  ;;  (clj->js {:title          "CLJS electron"
  ;;            :width          1280
  ;;            :height         800
  ;;            :webPreferences {:nodeIntegration            false
  ;;                             :nodeIntegrationInWorker    true
  ;;                             :nodeIntegrationInSubFrames false
  ;;                             :contextIsolation           true
  ;;                             :enableRemoteModule         false
  ;;                             :disableBlinkFeatures       "AuxClick"
  ;;                             :preload                    (.join path (js* "__dirname") "preload.js")}
  ;;            :show           false
  ;;            :modal          false
  ;;            :icon           (.join path (js* "__dirname") "assets" "clojure.png")}))
  )

(defn verify-url
  [event navigationUrl]
  (println "Verifying url before navigation")
  (let [parsedUrl (js/URL. navigationUrl)
        origin (.-origin parsedUrl)]
    (println parsedUrl " " origin)
    (when-not (= "https://www.github.com" origin)
      (println "Prevent browsing")
      (.preventDefault event))))

(defn initialize-main-window
  []
  (let [window (reset! main-window (create-browser-window))
        index-path (clj->js {:pathname
                             (.join path
                                    (js* "__dirname")
                                    "index.html")
                             :protocol "file:"
                             :slashes true})]
    (println "Index path " index-path)

    (.loadURL window
              (.format url index-path))

    (.setApplicationMenu Menu
                         (.buildFromTemplate
                          Menu
                          (clj->js (->> (create-menu-template)
                                        (remove nil?)
                                        (mapv identity)))))

    (.on window "closed" #(reset! main-window nil))
    (.webContents.on window "did-finish-load" #(.show window))))


(defn main
  []
  (println "Inside main function")

  (.on app "window-all-closed"
       #(when-not (= (.-platform process) "darwin")
          (println "Quit application")
          (.quit app)))

  (.on app "ready" (fn [] (initialize-main-window)))

  ;; (.on app "resize" (fn []
  ;;                     (let [[w h] (js->clj (.getSize (get-main-window)))]
  ;;                       (.setItem ls "width" w)
  ;;                       (.setItem ls "height" h))))

  (.on app "web-contents-created"
       (fn [_ ^js/electron.webContents web-contents]
         (.on web-contents "will-navigate" verify-url)))

  (.on ipcMain "showDialog" #(show-dialog %2)))

(comment

  (js/Object.getOwnPropertyNames js*)
  (println "hur")
  (js/console.log "hur")

  (def o #js {:foo :bar})
  o.foo

  (def p (nodejs/require "node:child_process"))
  (str (p.execFileSync "pwd"))



  )
