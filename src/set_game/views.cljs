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
      [:section
       [:div.mb-4.text-center
        [:p.mb-2.text-sm.uppercase.tracking-wide.font-bold.text-gray-600
         (str "Possible sets left: " possible-sets-left)]]
       [:div.flex.flex-row.flex-wrap
        (for [card dealt-cards]
          (let [is-selected (some #(= (:key card) %) (map :key selected-cards))
                on-click-arg (if is-selected [:deselect-card card] [:select-card card])]
            ^{:key card} [render-card card is-selected #(rf/dispatch on-click-arg)]))]])))
