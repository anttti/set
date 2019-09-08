(ns set-game.views
  (:require [re-frame.core :as rf]))

(defn play-area []
  (fn []
    (let [deck @(rf/subscribe [:deck])
          selected-cards @(rf/subscribe [:selected-cards])]
      [:section
       [:input {:type "button" :value "Shuffle the deck" :on-click #(rf/dispatch [:shuffle])}]
       [:ul
        (for [card (take 10 deck)]
          ^{:key card} [:li {:on-click #(rf/dispatch [:select-card card])}
                        (:shape card) ", "
                        (:color card) ", "
                        (:amount card) ", "
                        (:fill card)])]
       [:h6 "Selected"]
       [:ul
        (for [card selected-cards]
          ^{:key card} [:li (:shape card) ", "
                        (:color card) ", "
                        (:amount card) ", "
                        (:fill card)])]])))
