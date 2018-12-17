(ns advent-2018.day-17
  (:require
   #?(:cljs [planck.core :refer [slurp read-string]])
   [#?(:clj clojure.java.io :cljs planck.io) :as io]
   [clojure.string :as string]))

#_(def input (-> "advent_2018/day_17/input" io/resource slurp))

(def input "x=495, y=2..7\ny=7, x=495..501\nx=501, y=3..7\nx=498, y=2..4\nx=506, y=1..2\nx=498, y=10..13\nx=504, y=10..13\ny=13, x=498..504")

(defn parse-vein [s]
  (let [[a b1 b2] (map read-string (re-seq #"\d+" s))]
    (map (if (= (first s) \x)
           #(vector a %)
           #(vector % a))
      (range b1 (inc b2)))))

(defn flow [clay water max-y [x y :as coords]]
  (prn x y)
  (let [down  [x (inc y)]
        left  [(dec x) y]
        right [(inc x) y]
        sand (fn [coords]
                (and (not (clay coords))
                     (not (water coords))))]
    (cond
      (= y max-y)
      (assoc water coords \|)

      (sand down)
      (let [water (flow clay water max-y down)]
        (if (= \~ (water down))
          (flow clay water max-y coords)
          (flow clay (assoc water coords \|) max-y down)))

      :else
      (into
        (if (and (sand left)
                 (or (clay down)
                     (= \~ (water down))))
          (flow clay (assoc water coords (if (clay down) \~ \|)) max-y left)
          (assoc water coords \~))
        (if (and (sand right)
                 (or (clay down)
                     (= \~ (water down))))
          (flow clay (assoc water coords (if (clay down) \~ \|)) max-y right)
          (assoc water coords \~))))))

(defn display [clay water]
  (doseq [y (range 0 14)]
    (doseq [x (range 494 508)]
      (if (clay [x y])
        (print "#")
        (print (or (water [x y]) "."))))
    (newline)))

(defn part-1 []
  (let [clay  (into #{} (mapcat parse-vein (string/split-lines input)))
        min-y (apply min (map second clay))
        max-y (apply max (map second clay))
        water (flow clay {} max-y [500 min-y])]
    (display clay water)))