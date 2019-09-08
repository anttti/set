(ns set-game.views
  (:require [re-frame.core :as rf]))

(defn- get-fill [card]
  (cond
    (= (:fill card) :solid) (:color card)
    (= (:fill card) :dotted) (str "url(#dotted-" (name (:color card)) ")")
    (= (:fill card) :blank) "none"))

(defn- render-card [card on-click]
  (fn []
    (let [fill (get-fill card)]
      [:div.w-40.h-20.m-2.p-4.flex.flex-col.items-center.justify-center.rounded.shadow-lg.bg-white {:on-click on-click}
       [:div.flex.flex-row
        (for [i (range (:amount card))]
          ^{:key i} [:svg.m-1 {:width "30px" :height "30px" :view-box "0 0 180 180"}
                     (cond
                       (= (:shape card) :circle)
                       [:circle {:stroke (:color card) :stroke-width "20" :fill fill :cx "90" :cy "90" :r "80"}]
                       (= (:shape card) :square)
                       [:rect {:stroke (:color card) :stroke-width "20" :fill fill :x "10" :y "10" :width "160" :height "160"}]
                       (= (:shape card) :triangle)
                       [:polygon {:stroke (:color card) :stroke-width "20" :fill fill :points "85 10 170 170 10 170"}])])]])))

(defn play-area []
  (fn []
    (let [dealt-cards @(rf/subscribe [:dealt-cards])
          cards-left @(rf/subscribe [:cards-left])
          selected-cards @(rf/subscribe [:selected-cards])]
      [:section
       [:div.mb-4.text-center
        [:p.mb-2.text-sm.uppercase.tracking-wide.font-bold.text-gray-600 (str "Cards left: " cards-left)]
        [:input.py-2.px-4.rounded.bg-white.shadow-lg.uppercase.tracking-wide.font-bold.text-gray-600 {:type "button" :value "Deal 18" :on-click #(rf/dispatch [:deal])}]]
       [:div.flex.flex-row.flex-wrap
        (for [card dealt-cards]
          ^{:key card} [render-card card #(rf/dispatch [:select-card card])])]
       [:h6 "Selected"]
       [:div.flex.flex-row.flex-wrap
        (for [card selected-cards]
          ^{:key card} [render-card card #(rf/dispatch [:deselect-card card])])]])))
