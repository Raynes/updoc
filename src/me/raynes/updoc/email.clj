(ns me.raynes.updoc.email
  (:require [tentacles.issues :refer [issues specific-issue repo-milestones]]
            [clojure.java.io :refer [resource]]
            [me.raynes.cegdown :refer [to-html]] 
            [clojure.string :as string]
            [postal.core :as mail])
  (:require [me.raynes.laser :as l :refer [defdocument defragment]]))

(defn section [div issue]
  (l/at div
        (l/element= :a) (comp (l/attr :href (:html_url issue))
                              (l/content (:title issue)))
        (l/element= :p) (let [body (:body issue)]
                          (l/replace (l/nodes (to-html (if (seq body)
                                                         body 
                                                         "This issue has no description.")
                                                       [:fenced-code-blocks
                                                        :hardwraps
                                                        :autolinks]))))))

(defdocument email (resource "email.html")
  [issues]
  (l/element= :div) #(map (partial section %) issues))

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