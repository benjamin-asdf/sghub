(ns org.sg.sghub.preload
  (:require
   ["electron" :refer [contextBridge ipcRenderer]]))


(.exposeInMainWorld
 contextBridge "electron"
 (clj->js
  {
   ;; :cmd (fn [args])
   :showDialog (fn [dat] (.send ipcRenderer dat))
   })

 )

(defn main [])
