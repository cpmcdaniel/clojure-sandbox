(ns prime-factors.core)

(defn prime-factors-seq [num candidate]
  (if (< num 2)
    '()
    (if (zero?
         (mod num candidate))
      (cons candidate (lazy-seq (prime-factors-seq
                                 (/ num candidate) candidate)))
      (lazy-seq (prime-factors-seq num (inc candidate))))))

(defn prime-factors
  "Returns a lazy-seq of prime factors"
  [x]
  (prime-factors-seq x 2))

