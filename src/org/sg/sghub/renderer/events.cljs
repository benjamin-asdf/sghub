(ns org.sg.sghub.renderer.events
    (:require [re-frame.core :as rf]))

(def default-db {})

(rf/reg-event-db
 :initialize
 (fn []
   default-db))
