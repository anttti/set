(ns set-game.core
  (:require
   [reagent.core :as r]
   [re-frame.core :as rf]
   [set-game.subscriptions]
   [set-game.events]
   [set-game.views :refer [play-area]]))


;;   [set-game.set :refer [create-deck deal match?]]))

;;(defonce deck (r/atom (create-deck)))
;;
;;(defonce selected-cards (r/atom []))
;;
;;(defonce click-count (r/atom 0))
;;
;;(defn split-deck [deck]
;;  (second (split-at 10 deck)))
;;
;;(defn add-card [selected card]
;;  (conj selected card))
;;
;;(defn select-card [selected card]
;;  (let [is-selected-already (some #(= (:key card) %) (map :key selected))]
;;    (if (not is-selected-already)
;;      (swap! selected add-card card))))

(defn app []
  [:header [:h2 "Set"]
   [play-area]])

;; -------------------------
;; Initialize app

(defn mount-root []
  (r/render [app] (.getElementById js/document "app")))

(defn init! []
  (rf/dispatch-sync [:initialize-db])
  (mount-root))
