(ns set-game.subscriptions
  (:require [re-frame.core :as rf]))

(rf/reg-sub
  :deck
  (fn [db]
    (:deck db)))

(rf/reg-sub
  :selected-cards
  (fn [db]
    (:selected-cards db)))
