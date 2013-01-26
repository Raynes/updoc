(ns me.raynes.updoc
  (:require [me.raynes.updoc.email :refer [send-email email get-issues]]
            [clojure.tools.cli :refer [cli]]
            [me.raynes.fs :refer [temp-file]])
  (:gen-class))

(defn -main [& args]
  (let [[options args] (cli args ["-p" "--preview" "Create a temp file with the email content."
                                  :flag true])
        [to user repo milestone creds] args]
    (if (and to user repo milestone)
      (let [email (email (get-issues user repo milestone creds))]
        (if (:preview options)
          (let [f (temp-file "updoc-email")]
            (spit f email)
            (println "Created file" (.getPath f)))
          (do (send-email to milestone email)
              (println "Email sent."))))
      (println "Please provide the email address to send to, the user (or organization)"
               "holding the repo, the repo name, the milestone to look for, and Github credentials"
               "to access the repo if the repo is private in the form of user:password."))))