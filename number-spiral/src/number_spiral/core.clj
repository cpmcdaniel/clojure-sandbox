(ns number-spiral.core
  (require [clojure.pprint :refer [pprint]]))

(defn nil-vec
  "Create a vector of nils of size n"
  [n]
  (vec (repeat n nil)))

(defn rows
  "Gets the number of rows"
  [data]
  (count data))

(defn cols
  "Gets the number of columns"
  [data]
  (count (first data)))

(defn expand-box
  "If the coordinates are outside of the box, expand the box."
  [[x y] data]
  (cond
   ;; top - insert row at front
   (and (= [0 0] [x y])
        (or (empty? data)
            (not (nil? (-> data (nth x) (nth y))))))
   (vec (cons (nil-vec (cols data)) data))

   ;; bottom - insert row at end
   (>= x (count data))
   (conj data (nil-vec (cols data)))

   ;; left bottom - insert column at front
   (and (= x (dec (rows data)))
        (= y 0)
        (not (nil? (-> data (nth x) (nth y)))))
   (vec (for [row data] (vec (cons nil row))))

   ;; default - just return the data as-is
   :default data))

(defn inc-coords
  "Move to the next coordinate in the spiral"
  [[x y] data]
  (cond
   ;; don't move if current cell is nil
   (nil? (-> data (nth x) (nth y)))
   [x y]

   ;; move right?
   (and
    (= x 0)
    (< y (cols data))
    (= (rows data) (cols data)))
   [x (inc y)]

   ;; move down?
   (and (= y (dec (cols data)))
        (<= x (rows data))
        (< (rows data) (cols data)))
   [(inc x) y]

   ;; move left?
   (and (= x (dec (rows data)))
        (> y 0))
   [x (dec y)]

   ;; move up?
   (and (= y 0)
        (or
         (and (= x (dec (rows data)))
              (> (cols data) (rows data)))
         (> (dec (rows data)) x 0)))
   [(dec x) y]

   :default [x y]))


(defn make-spiral
  "Creates a 2-dimensional spiral data structure up to the number specified."
  [n]
  (loop [num 0
         [x y] [0 0]
         data [[nil]]]
    (if (> num n) data
        (let [new-box (expand-box [x y] data)
              new-data (assoc new-box x
                              (assoc (new-box x) y num))]
          (recur (inc num)
                 (inc-coords [x y] new-data)
                 new-data)))))


(defn spiral-str
  "Creates a string representation of the spiral"
  [n]
  (let [spiral (make-spiral n)
        cell-len (inc (count (str n)))
        cell-format (str "%" cell-len "d")]
    (reduce (fn [s row]
              (str s
                   (reduce (fn [s cell]
                             (str s (format cell-format cell))) "" row)
                   "\n"))
            "" spiral)))

(comment
  ;; For experimenting at the REPL:

  (make-spiral 24)

  (println (spiral-str 120)))
