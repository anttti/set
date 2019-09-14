(ns set-game.views
  (:require [re-frame.core :as rf]
            [reagent.core :as r]))

(defn- get-fill [card]
  (cond
    (= (:fill card) :solid) (:color card)
    (= (:fill card) :dotted) (str "url(#dotted-" (name (:color card)) ")")
    (= (:fill card) :blank) "none"))

(defn- render-card-contents [card fill]
  (fn []
    [:div.flex.flex-row
     (for [i (range (:amount card))]
       ^{:key i} [:svg.m-1 {:width "20px" :height "20px" :view-box "0 0 180 180"}
                  (cond
                    (= (:shape card) :circle)
                    [:circle {:stroke (:color card) :stroke-width "20" :fill fill :cx "90" :cy "90" :r "80"}]
                    (= (:shape card) :square)
                    [:rect {:stroke (:color card) :stroke-width "20" :fill fill :x "10" :y "10" :width "160" :height "160"}]
                    (= (:shape card) :triangle)
                    [:polygon {:stroke (:color card) :stroke-width "20" :fill fill :points "85 10 170 170 10 170"}])])]))

(defn- render-card [card is-game-on is-selected on-click]
  (fn [card is-game-on is-selected on-click]
    (if is-game-on
      (let [fill (get-fill card)]
        [:button.w-24.h-16.m-2.flex.flex-col.items-center.justify-center.rounded.shadow-lg.bg-white
         {:on-click on-click
          :class (if is-selected "opacity-50")}
         [render-card-contents card fill]])
      [:div.w-24.h-16.m-2.flex.flex-col.items-center.justify-center.rounded.shadow-lg.bg-gray-700])))

(defn- button
  ([on-click title]
   [:button.px-6.py-2.border-2.border-solid.border-gray-700.rounded.text-gray-700.tracking-wide.uppercase.text-sm.font-bold {:on-click on-click} title])
  ([classes on-click title]
   [:button.px-6.py-2.border-2.border-solid.border-gray-700.rounded.text-gray-700.tracking-wide.uppercase.text-sm.font-bold {:class classes :on-click on-click} title]))

(defn- help []
  (let [is-open (r/atom false)]
    (fn []
      (if @is-open
        [:section.p-4.max-w-lg.rounded.text-gray-700.text-sm.bg-gray-400.opacity-75
         [:h2.mb-2.text-sm.uppercase.tracking-wide.font-bold.text-gray-700.text-center "Rules"]
         [:p.mb-3 "A #{} consists of three cards satisfying all of these conditions:"]
         [:ul.mb-3.pl-4.list-decimal
          [:li.mb-2 "They all have the same number or have three different numbers."]
          [:li.mb-2 "They all have the same shape or have three different shapes."]
          [:li.mb-2 "They all have the same shading or have three different shadings."]
          [:li.mb-2 "They all have the same color or have three different colors."]]
         [:p.mb-4 "The rules of #{} are summarized by: "
          [:i "If you can sort a group of three cards into \"two of ____ and one of ____\", then it is not a #{}."]]
         [:div.text-center
          [button #(reset! is-open false) "Close"]]]
        [button #(reset! is-open true) "Rules"]))))

(defn play-area []
  (fn []
    (let [dealt-cards @(rf/subscribe [:dealt-cards])
          selected-cards @(rf/subscribe [:selected-cards])
          possible-sets-left @(rf/subscribe [:possible-sets-left])
          is-game-on @(rf/subscribe [:is-game-on])
          time-elapsed @(rf/subscribe [:time-elapsed])]
      [:section.flex.flex-col.items-center
       [:div.h-16.flex.flex-col.justify-center
        (if is-game-on
          [:p.mb-2.text-sm.uppercase.tracking-wide.font-bold.text-gray-700
            (str "Possible #{}s left: " possible-sets-left)]
          [button #(rf/dispatch [:start-game]) "Start game"])]
       [:div.mb-8.flex.flex-row.flex-wrap.justify-center
        (for [card dealt-cards]
          (let [is-selected (some #(= (:key card) %) (map :key selected-cards))
                on-click-arg (if is-selected [:deselect-card card] [:select-card card])]
            ^{:key card} [render-card card is-game-on is-selected #(rf/dispatch on-click-arg)]))]
       [help]])))

(defn app []
  [:main
   [play-area]])
