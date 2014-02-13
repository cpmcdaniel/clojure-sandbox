(ns prime-factors.core-test
  (:require [clojure.test :refer :all]
            [simple-check.generators :as gen]
            [simple-check.properties :as prop]
            [simple-check.clojure-test :as ct :refer [defspec]]
            [prime-factors.core :refer :all]))

(deftest test-prime-factors
  (testing "Prime Factors"
    (testing "factors of 1"
      (is (= '() (prime-factors 1))))
    (testing "factors of 2"
      (is (= '(2) (prime-factors 2))))
    (testing "factors of 3"
      (is (= '(3) (prime-factors 3))))
    (testing "factors of 4"
      (is (= '(2 2) (prime-factors 4))))
    (testing "factors of 5"
      (is (= '(5) (prime-factors 5))))
    (testing "factors of 6"
      (is (= '(2 3) (prime-factors 6))))
    (testing "factors of 8"
      (is (= '(2 2 2) (prime-factors 8))))
    (testing "factors of 9"
      (is (= '(3 3) (prime-factors 9))))
    (testing "first factor of a really large number"
      (is (= 2 (first (prime-factors 1200002)))))))


(defspec prime-factors-spec
  100 ;; # of iterations for simple-check to test
  (prop/for-all [l gen/nat]
                (seq? (prime-factors l))))



;(runner/run 1 500 #'prime-factors-spec)
;(run-tests)
