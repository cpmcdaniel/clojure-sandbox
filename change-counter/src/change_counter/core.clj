(ns change-counter.core)


(defn prompt [message]
  (println message)
  (read-line))

(defn get-coin-amount [coin-type]
  (let [coin-amount
        (prompt (str "How many " coin-type " do you have?"))]
    (try
      (Integer/parseInt coin-amount)
      (catch Exception e
        (throw (Error. "Not a valid amount"))))))

(defn count-change []
  (+
    (get-coin-amount "pennies")
    (* 5 (get-coin-amount "nickels"))
    (* 10 (get-coin-amount "dimes"))
    (* 25 (get-coin-amount "quarters"))
    (* 50 (get-coin-amount "half dollars"))
    (* 100 (get-coin-amount "silver dollars"))))

(defn convert-to-dollars [cents]
  (/ cents 100))
