(require '[clojure.test :refer :all])
(require '[change-counter.core :refer :all])


(def coin-type (atom nil))
(def entered-value (atom nil))

(Given #"^that I am prompted for \"([^\"])*\"$" [arg1]
       (reset! coin-type arg1))

(When #"^I enter \"([^\"]*)\"$" [arg1]
      (reset! entered-value arg1))

(Then #"^the prompt code returns \"([^\"]*)\"$" [arg1]
      (with-out-str
        (is (= @entered-value
               (with-in-str @entered-value
                 (prompt (str "Enter amount of " @coin-type)))))))

(Given #"^that I request a prompt of \"([^\"]*)\"$" [prompt-string]
       (assert false))


(Then #"^I should have \"([^\"]*)\" dollars$" [dollar-string]
      (let [formatter (java.text.DecimalFormat. "$0.00")]
        (is (= dollar-string
               (.format formatter (convert-to-dollars 2223.333))))))
