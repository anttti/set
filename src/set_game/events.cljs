(ns set-game.events
  (:require [re-frame.core :as rf]
            [set-game.db :as db]
            [set-game.set :as set]))

(rf/reg-event-db
  :initialize-db
  (fn [_ _]
    db/default-db))

(rf/reg-event-db
  :start-game
  (fn [db _]
    (-> db
      (assoc ,,, :score 0)
      (assoc ,,, :time-elapsed 0)
      (assoc ,,, :is-game-on true))))

(rf/reg-event-db
  :shuffle
  (fn [db _]
    (assoc db :deck (set/create-deck))))

(rf/reg-event-db
  :deal
  (fn [db _]
    (let [[dealt deck] (split-at 18 (:deck db))]
      (-> db
        (assoc ,,, :dealt-cards dealt)
        (assoc ,,, :deck deck)))))

(rf/reg-event-db
  :second-passed
  (fn [db _]
    (update db :time-elapsed inc)))

(defn add-selected-card [selected card]
  (let [is-selected-already (some #(= (:key card) %) (map :key selected))]
    (if (not is-selected-already)
      (conj selected card)
      selected)))

(defn remove-selected-cards [selected-cards dealt-cards]
  (let [selected-keys (map :key selected-cards)]
    (filter (fn [c] (not (some #(= (:key c) %) selected-keys))) dealt-cards)))

(defn replace-missing-cards [dealt-cards deck]
  (let [missing-amount (- 18 (count dealt-cards))
        [newly-drawn deck] (split-at missing-amount deck)]
    [(flatten (conj newly-drawn dealt-cards)) deck]))

(defn calculate-dealt-cards [selected-cards dealt-cards deck]
  (let [without-selected (remove-selected-cards selected-cards dealt-cards)
        [with-newly-drawn deck] (replace-missing-cards without-selected deck)]
    [with-newly-drawn deck]))

(rf/reg-event-db
  :select-card
  (fn [db [_ card]]
    (let [new-selected-cards (add-selected-card (:selected-cards db) card)]
      (if (= (count new-selected-cards) 3)
        (if (set/match? new-selected-cards)
          (let [[dealt-cards deck] (calculate-dealt-cards new-selected-cards (:dealt-cards db) (:deck db))]
            (-> db
              (assoc ,,, :dealt-cards dealt-cards)
              (assoc ,,, :deck deck)
              (assoc ,,, :selected-cards [])
              (update ,,, :score inc)))
          (assoc db :selected-cards []))
        (assoc db :selected-cards new-selected-cards)))))

(rf/reg-event-db
  :deselect-card
  (fn [db [_ card]]
    (println "Deselect" card)
    (let [new-selected-cards (remove-selected-cards [card] (:selected-cards db))]
      (assoc db :selected-cards new-selected-cards))))
