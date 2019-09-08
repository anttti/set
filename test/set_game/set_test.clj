(ns set-game.set-test
  (:require [clojure.test :refer [deftest is]]
            [set-game.set :refer [create-card match?]]))

(def r-c-1-so (create-card 0)) ;; red-circle-1-solid
(def g-c-1-so (create-card 3)) ;; green-circle-1-solid
(def b-c-1-so (create-card 6)) ;; blue-circle-1-solid
(def r-s-1-so (create-card 9)) ;; red-square-1-solid
(def r-t-1-so (create-card 18)) ;; red-triangle-1-solid
(def g-t-2-st (create-card 76));; green-triangle-2-dotted
(def b-s-3-bl (create-card 0));; blue-square-3-blank

(deftest test-create-card
  (is (= (:color r-c-1-so) :red))
  (is (= (:color g-c-1-so) :green))
  (is (= (:color b-c-1-so) :blue))
  (is (= (:shape r-c-1-so) :circle))
  (is (= (:amount r-c-1-so) 1))
  (is (= (:fill r-c-1-so) :solid))
  (is (= (:shape r-s-1-so) :square))
  (is (= (:shape r-t-1-so) :triangle))

  (is (= (:color g-t-2-st) :green))
  (is (= (:shape g-t-2-st) :triangle))
  ;;(is (= (:amount g-t-2-st) 2))
  (is (= (:fill g-t-2-st) :dotted)))

(deftest test-match
  (is (match? [r-c-1-so r-s-1-so r-t-1-so]))
  (is (not (match? [r-c-1-so g-c-1-so r-t-1-so]))))
