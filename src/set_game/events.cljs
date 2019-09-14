(ns set-game.events
  (:require [re-frame.core :as rf]
            [set-game.db :as db]
            [set-game.set :as set]))

(rf/reg-event-db
  :initialize-db
  (fn [_ _]
    db/default-db))

(rf/reg-event-db
  :shuffle
  (fn [db _]
    (assoc db :deck (set/create-deck))))

(rf/reg-event-db
  :deal
  (fn [db [_]]
    (let [[dealt deck] (split-at 18 (:deck db))]
      (-> db
        (assoc ,,, :dealt-cards dealt)
        (assoc ,,, :deck deck)))))

(defn add-selected-card [selected card]
  (let [is-selected-already (some #(= (:key card) %) (map :key selected))]
    (if (not is-selected-already)
      (conj selected card)
      selected)))

(defn remove-selected-card [cards card]
  ())

(defn remove-selected-cards [selected-cards dealt-cards]
  (let [selected-keys (map :key selected-cards)]
    (filter (fn [c] (not (some #(= (:key c) %) selected-keys))) dealt-cards)))

(rf/reg-event-db
  :select-card
  (fn [db [_ card]]
    (println "Select" card)
    (let [new-selected-cards (add-selected-card (:selected-cards db) card)]
      (if (= (count new-selected-cards) 3)
        (if (set/match? new-selected-cards)
          (do
            (println "Match!")
            (-> db
              (assoc ,,, :dealt-cards (remove-selected-cards new-selected-cards (:dealt-cards db)))
              (assoc ,,, :selected-cards [])
              (update ,,, :score inc)))
          (do
            (println "No match!")
            (assoc db :selected-cards [])))
        (assoc db :selected-cards new-selected-cards)))))

(rf/reg-event-db
  :deselect-card
  (fn [db [_ card]]
    (println "Deselect" card)
    (let [new-selected-cards (remove-selected-card (:selected-cards db) card)]
      (assoc db :selected-cards new-selected-cards))))
