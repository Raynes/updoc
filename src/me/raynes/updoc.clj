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
           (-> (System/console) (.readPassword) (String.))
           (read-line))))))

(defn arguments [args]
  (cli args
       ["-p" "--preview" "Create a temp file with the email content." :flag true]
       ["-h" "--help" "Show help." :flag true]))

(defn -main [& args]
  (let [[options args banner] (arguments args) 
        [to user repo milestone] args]
    (cond
     (:help options) (println banner)
     (and to user repo milestone)
     (let [creds (join ":" (prompt [["Github username: "]
                                    ["Github password: " :pass]]))
           issues (get-issues user repo milestone creds)]
       (if (map? issues)
         (prn issues)
         (let [email (email issues)]
           (if (:preview options)
             (println "Created file" (.getPath (doto (temp-file "updoc-email")
                                                 (spit email)))) 
             (send-email to milestone email)))))
     :else (println "Usage: updoc email user-or-organization repo milestone"))))