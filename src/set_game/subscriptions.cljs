(ns set-game.subscriptions
  (:require [re-frame.core :as rf]))

(rf/reg-sub
  :deck
  (fn [db]
    (:deck db)))

(rf/reg-sub
  :dealt-cards
  (fn [db]
    (:dealt-cards db)))

(rf/reg-sub
  :selected-cards
  (fn [db]
    (:selected-cards db)))

(rf/reg-sub
  :cards-left
  (fn [db]
    (count (:deck db))))
