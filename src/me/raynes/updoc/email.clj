(ns me.raynes.updoc.email
  (:require [tentacles.issues :refer [issues specific-issue repo-milestones]]
            [clojure.java.io :refer [resource]]
            [me.raynes.cegdown :refer [to-html]] 
            [clojure.string :as string]
            [postal.core :as mail])
  (:require [me.raynes.laser :as l :refer [defdocument]]))

(defn section [email]
  (let [body (:body email)]
    (l/node :div
            :content (into
                      [(l/node :h2 :content (:title email))]
                      (l/nodes (to-html (if (seq body)
                                          body
                                          "This issue has no description.")
                                        [:fenced-code-blocks
                                         :hardwraps
                                         :autolinks]))))))

(defdocument email
  (resource "email.html")
  [emails]
  (l/element= :div) (l/replace
                     (for [email emails]
                       (section email))))

(defn get-issues [user repo milestone auth]
  (let [milestone (-> (filter (comp #{milestone} :title)
                              (repo-milestones user repo {:state :closed
                                                          :auth auth}))
                      (first)
                      (:number))]
    (issues user repo {:milestone milestone
                       :state :closed
                       :auth auth
                       :all-pages true})))

(defn send-email [to release text]
  (mail/send-message {:to [to]
                      :from ""
                      :subject (str "Closed Github issues for release milestone " release)
                      :body [{:type "text/html"
                              :content text}]}))