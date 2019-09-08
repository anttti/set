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
  (filter (fn [c] (not (= (:key c) (:key card)))) cards))

(rf/reg-event-db
  :select-card
  (fn [db [_ card]]
    (let [new-selected-cards (add-selected-card (:selected-cards db) card)
          new-dealt-cards (remove-selected-card (:dealt-cards db) card)]
      (if (= (count new-selected-cards) 3)
        (if (set/match? new-selected-cards)
          (-> db
            (assoc ,,, :selected-cards [])
            (assoc ,,, :dealt-cards new-dealt-cards)
            (update ,,, :score inc))
          (-> db
            (assoc ,,, :dealt-cards (flatten (conj (:selected-cards db) (:dealt-cards db))))
            (assoc ,,, :selected-cards [])))
        (-> db
         (assoc ,,, :selected-cards new-selected-cards)
         (assoc ,,, :dealt-cards new-dealt-cards))))))

(rf/reg-event-db
  :deselect-card
  (fn [db [_ card]]
    (let [new-dealt-cards (add-selected-card (:dealt-cards db) card)
          new-selected-cards (remove-selected-card (:selected-cards db) card)]
      (-> db
        (assoc ,,, :dealt-cards new-dealt-cards)
        (assoc ,,, :selected-cards new-selected-cards)))))
