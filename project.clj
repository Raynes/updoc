(defproject updoc "0.1.1"
  :description "Send an email about closed issues in a Github milestone"
  :url "https://github.com/geni/updoc"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.4.0"]
                 [tentacles "0.2.4"] 
                 [me.raynes/laser "0.1.22"]
                 [me.raynes/cegdown "0.1.0"]
                 [me.raynes/conch "0.5.0"]
                 [com.draines/postal "1.9.2"]
                 [org.clojure/tools.cli "0.2.2"]
                 [me.raynes/fs "1.4.0"]]
  :main me.raynes.updoc)
