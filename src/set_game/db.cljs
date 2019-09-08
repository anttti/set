(ns set-game.db
  (:require [set-game.set :as set]))

(def default-db
  {:deck (set/create-deck)
   :selected-cards []})
