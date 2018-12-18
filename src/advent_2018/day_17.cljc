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

(defn display [clay water]
  (doseq [y (range 0 14)]
    (doseq [x (range 494 508)]
      (if (clay [x y])
        (print "#")
        (print (or (water [x y]) "."))))
    (newline))
  (newline))

(defn flow [clay water max-y coords]
  (display clay water)
  (let [down   (fn [[x y]] [x (inc y)])
        left   (fn [[x y]] [(dec x) y])
        right  (fn [[x y]] [(inc x) y])
        sand?  (fn [coords]
                 (and (not (clay coords))
                      (not (water coords))))
        holds? (fn holds? [dir coords]
                 (and (or (clay (down coords))
                          (= \~ (water (down coords))))
                      (or (clay (dir coords))
                          (holds? dir (dir coords)))))
        fill   (fn fill [water dir coords]
                 (-> (cond-> water
                       (not (clay (dir coords)))
                       (fill dir (dir coords)))
                   (assoc coords \~)))
        water  (assoc water coords \|)]
    (cond
      (= (second coords) max-y)
      water

      (sand? (down coords))
      (let [water (flow clay water max-y (down coords))]
        (recur clay water max-y coords))

      (and (holds? left coords)
           (holds? right coords))
      (-> water
        (fill left coords)
        (fill right coords))

      :else
      (into
        (if (and (sand? (left coords))
                 (or (clay (down coords))
                     (= \~ (water (down coords)))))
          (flow clay water max-y (left coords))
          water)
        (if (and (sand? (right coords))
                 (or (clay (down coords))
                     (= \~ (water (down coords)))))
          (flow clay water max-y (right coords))
          water)))))

(defn part-1 []
  (let [clay  (into #{} (mapcat parse-vein (string/split-lines input)))
        min-y (apply min (map second clay))
        max-y (apply max (map second clay))
        water (flow clay {} max-y [500 min-y])]
    (display clay water)))