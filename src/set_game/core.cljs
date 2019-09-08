(ns set-game.core
  (:require
   [reagent.core :as r]
   [re-frame.core :as rf]
   [set-game.subscriptions]
   [set-game.events]
   [set-game.views :refer [play-area]]))

(defn app []
  [:main
   [:header [:h1.text-center.text-xl.text-gray-800.font-bold.uppercase.tracking-wider "Set"]]
   [play-area]])

(defn mount-root []
  (r/render [app] (.getElementById js/document "app")))

(defn init! []
  (rf/dispatch-sync [:initialize-db])
  (rf/dispatch-sync [:shuffle])
  (rf/dispatch-sync [:deal])
  (mount-root))
