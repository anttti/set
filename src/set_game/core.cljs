(ns set-game.core
  (:require
   [reagent.core :as r]
   [re-frame.core :as rf]
   [re-pollsive.core :as poll]
   [set-game.subscriptions]
   [set-game.events]
   [set-game.views :refer [app]]))

(defn mount-root []
  (r/render [app] (.getElementById js/document "app")))

(defn init! []
  (rf/dispatch-sync [::poll/init])
  (rf/dispatch-sync [:initialize-db])
  (rf/dispatch-sync [:shuffle])
  (rf/dispatch-sync [:deal])
  (rf/dispatch [::poll/set-rules
                    [{:interval 1
                      :event [:second-passed]
                      :poll-when [:is-game-on]}]])
  (mount-root))
