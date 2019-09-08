(ns set-game.set
  (:require [clojure.math.combinatorics :as combinatorics]))

(defn get-color
  [index]
  (let [i (mod index 9)]
    (cond
      (<= i 2) "#E53E3E" ;; red
      (<= i 5) "#38A169" ;; green
      :else "#3182CE"))) ;; blue

(defn get-shape
  [index]
  (let [i (mod index 27)]
    (cond
      (<= i 8) :circle
      (<= i 17) :square
      :else :triangle)))

(defn get-amount
  [index]
  (cond
    (<= index 26) 1
    (<= index 54) 2
    :else 3))

(defn get-fill
  [index]
  (let [fills [:solid :dotted :blank]]
    (fills (mod index (count fills)))))

(defn create-card
  [index]
  {:key index
   :color (get-color index)
   :amount (get-amount index)
   :fill (get-fill index)
   :shape (get-shape index)})

(defn create-deck
  []
  (shuffle (map create-card (take 81 (range)))))

(defn deal
  [number deck]
  (split-at number deck))

(defn attribute-match?
  [attributes]
  (let [[a b c] attributes]
    (or (and (= a b)
             (= b c))
        (and (not= a b)
             (not= b c)
             (not= a c)))))

(defn match?
  [cards]
  (and (attribute-match? (map :color cards))
       (attribute-match? (map :shape cards))
       (attribute-match? (map :fill cards))
       (attribute-match? (map :amount cards))))

(defn get-matches [cards]
  (let [sets (combinatorics/combinations cards 3)]
    (filter match? sets)))
