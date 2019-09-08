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

(rf/reg-event-db
  :select-card
  (fn [db [_ card]]
    (if (= (count (:selected-cards db)) 3)
      (do
        (println "GOT THREE")
        db)
      (update db :selected-cards #(add-selected-card (:selected-cards db) card)))))
