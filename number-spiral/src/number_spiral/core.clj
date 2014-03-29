(ns number-spiral.core)

(comment
  [[0]]

  [[0 1]]

  [[0 1]
   [nil 2]]

  [[0 1]
   [3 2]]

  [[nil 0 1]
   [4 3 2]]

  [[5 0 1]
   [4 3 2]]

  [[6 nil nil]
   [5 0 1]
   [4 3 2]]

  [[6 7 8 9]
   [5 0 1 nil]
   [4 3 2 nil]])

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

   ;; default - just return the data as-is
   :default data))

(defn inc-coords
  "Move to the next coordinate in the spiral"
  [[x y] data]
  (cond
   ;; move right?
   (and
    (= x 0)
    (< y (cols data))
    (= (rows data) (cols data)))
   [x (inc y)]

   ;; move down?
   (and (= (inc y) (cols data))
        (<= x (rows data))
        (< (rows data) (cols data)))
   [(inc x) y]

   ;; move left?
   (and (= (inc x) (rows data))
        (> y 0))
   [x (dec y)]

   :default [0 0]
   ))

(inc-coords [1 0] [[0 1]
                   [2 3]])

(expand-box [1 1] [[0 1]
                   [2 3]])


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



(make-spiral 4)
