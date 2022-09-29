(ns org.sg.sghub.preload
  (:require ["electron" :refer [contextBridge ipcRenderer]]))

(.exposeInMainWorld contextBridge
                    "api"
                    (clj->js {}))

(defn main
  []
  )
