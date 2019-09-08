(ns set-game.set)

(defn get-color
  [index]
  (let [i (mod index 9)]
    (cond
      (<= i 2) :red
      (<= i 5) :green
      :else :blue)))

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
  (let [fills [:solid :striped :blank]]
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
  (println "Creating deck")
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
