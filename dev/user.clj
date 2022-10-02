(ns user)

(defn lazy-fn [symbol]
  (fn [& args] (apply (requiring-resolve  symbol) args)))

(def start!       (lazy-fn 'shadow.cljs.devtools.server/start!))
(def watch        (lazy-fn 'shadow.cljs.devtools.api/watch))
(def repl         (lazy-fn 'shadow.cljs.devtools.api/repl))

(defn cljs
  ([] (cljs [:renderer :main :preload]))
  ([build-ids]
   (start!)
   (doseq
    [build-id build-ids]
    (watch build-id)
    (repl build-id))))

(comment

  (shadow.cljs.devtools.api/repl-runtimes :renderer)
  (repl :renderer)


  

  )
