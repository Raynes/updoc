(ns me.raynes.updoc
  (:require [me.raynes.updoc.email :refer [send-email email get-issues]]
            [clojure.tools.cli :refer [cli]]
            [me.raynes.fs :refer [temp-file]]
            [clojure.string :refer [join]])
  (:gen-class))

(defn prompt [spec]
  (doall
   (for [[prompt flag] spec]
     (do (print prompt)
         (flush)
         (if (= :pass flag)
           (-> (System/console) (.readPassword) (join))
           (read-line))))))

(defn -main [& args]
  (let [[options args] (cli args ["-p" "--preview" "Create a temp file with the email content."
                                  :flag true])
        [to user repo milestone] args]
    (if (and to user repo milestone)
      (let [creds (join ":" (prompt [["Github username: "]
                                     ["Github password: " :pass]])) 
            email (email (get-issues user repo milestone creds))]
        (if (:preview options)
          (let [f (temp-file "updoc-email")]
            (spit f email)
            (println "Created file" (.getPath f)))
          (send-email to milestone email)))
      (println "Usage: updoc email user-or-organization repo milestone"))))