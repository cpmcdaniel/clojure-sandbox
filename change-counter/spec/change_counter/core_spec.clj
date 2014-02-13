(ns change-counter.core-spec
    (:use
        [speclj.core]
        [change-counter.core]))


(describe "Change counter"
          (around [it]
                  (with-out-str (it)))

          (it "tests the input of prompt"
              (should= "13"
                       (with-in-str "13"
                         (prompt "Enter amount of pennies"))))

          (it "tests the output of prompt"
              (should-contain #"^Where is my money?"
                       (with-out-str (with-in-str "10"
                                       (prompt "Where is my money?")))))
          (it "gets a valid number"
              (with-redefs
                [prompt (fn [& _] "11")]
                (should-not-throw (get-coin-amount "quarters"))
                (should= 11 (get-coin-amount "pennies"))
                (should-not= "11" (get-coin-amount "dimes"))
                (if (not= 11 (get-coin-amount "quarters"))
                  (should-fail "issues getting the correct coin amount"))
                )
              (with-redefs
                [prompt (fn [& _] "$5")]
                (should-throw Error "Not a valid amount"
                              (get-coin-amount "silver dollars"))))

          (it "returns how much money you have in pennies"
              (with-redefs [get-coin-amount (fn [& _] 2)]
                (should= 382 (count-change))))

          (with dollar-format (java.text.DecimalFormat. "$0.00"))

          (it "converts your change into dollars"
              (should= "$11.34"
                       (.format @dollar-format
                                (convert-to-dollars 1134.25634456463))))
)



(run-specs)


