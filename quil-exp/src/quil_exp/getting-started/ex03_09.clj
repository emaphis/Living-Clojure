(ns quil-exp.getting-started.ex03-09
  (:require [quil.core :as q]))

;; Drawing Order
;; Example 3-9: Control Your Drawing Order
;; pg. 20.

(defn draw []
  (q/ellipse 140 0 190 190)
  ;; The rectangle draws on top of the ellipse
  ;; because it comes after in the code.
  (q/rect 160 30 260 20))

(q/defsketch example3_9
  :title "Control Your Drawing Order"
  :size [480 120]
  :draw draw)
