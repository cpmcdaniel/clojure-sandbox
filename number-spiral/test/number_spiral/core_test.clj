(ns number-spiral.core-test
  (:require [clojure.test :refer :all]
            [number-spiral.core :refer :all]))

(deftest test-zero
  (is (= [[0]] (make-spiral 0))))

(deftest test-one
  (let [data (make-spiral 1)]
    (is (= [[0 1]] (make-spiral 1)))))


(run-tests)
