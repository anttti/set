(ns set-game.views
  (:require [re-frame.core :as rf]))

(defn- get-fill [card]
  (cond
    (= (:fill card) :solid) (:color card)
    (= (:fill card) :dotted) (str "url(#dotted-" (name (:color card)) ")")
    (= (:fill card) :blank) "none"))

(defn- render-card [card is-selected on-click]
  (fn [card is-selected on-click]
    (let [fill (get-fill card)]
      [:div.w-40.h-20.m-2.p-4.flex.flex-col.items-center.justify-center.rounded.shadow-lg.bg-white
       {:on-click on-click
        :class (if is-selected "border-gray-500 border-4")}
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
          selected-cards @(rf/subscribe [:selected-cards])
          possible-sets-left @(rf/subscribe [:possible-sets-left])]
      [:section.flex.flex-col.items-center
       [:div.mb-4.text-center
        [:p.mb-2.text-sm.uppercase.tracking-wide.font-bold.text-gray-600
         (str "Possible #{}s left: " possible-sets-left)]]
       [:div.mb-8.flex.flex-row.flex-wrap.justify-center
        (for [card dealt-cards]
          (let [is-selected (some #(= (:key card) %) (map :key selected-cards))
                on-click-arg (if is-selected [:deselect-card card] [:select-card card])]
            ^{:key card} [render-card card is-selected #(rf/dispatch on-click-arg)]))]
       [:section.max-w-lg.rounded.text-gray-600.text-sm
        [:p.mb-3 "A #{} consists of three cards satisfying all of these conditions:"]
        [:ul.mb-3.list-decimal.list-inside
         [:li "They all have the same number or have three different numbers."]
         [:li "They all have the same shape or have three different shapes."]
         [:li "They all have the same shading or have three different shadings."]
         [:li "They all have the same color or have three different colors."]]
        [:p "The rules of #{} are summarized by: "
         [:i "If you can sort a group of three cards into \"two of ____ and one of ____\", then it is not a #{}."]]]])))
