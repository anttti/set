(ns set-game.views
  (:require [re-frame.core :as rf]))

(defn play-area []
  (fn []
    (let [dealt-cards @(rf/subscribe [:dealt-cards])
          cards-left @(rf/subscribe [:cards-left])
          selected-cards @(rf/subscribe [:selected-cards])]
      [:section
       [:p (str "Cards left: " cards-left)]
       [:input {:type "button" :value "Deal 18" :on-click #(rf/dispatch [:deal])}]
       [:ul
        (for [card dealt-cards]
          ^{:key card} [:li {:on-click #(rf/dispatch [:select-card card])}
                        (:shape card) ", "
                        (:color card) ", "
                        (:amount card) ", "
                        (:fill card)])]
       [:h6 "Selected"]
       [:ul
        (for [card selected-cards]
          ^{:key card} [:li
                        (:shape card) ", "
                        (:color card) ", "
                        (:amount card) ", "
                        (:fill card)])]])))
