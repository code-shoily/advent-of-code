(ns advent-2018.day-17
  (:require
   #?(:cljs [planck.core :refer [slurp read-string]])
   [#?(:clj clojure.java.io :cljs planck.io) :as io]
   [clojure.string :as string]))

(def input (-> "advent_2018/day_17/input" io/resource slurp))

#_(def input "x=495, y=2..7\ny=7, x=495..501\nx=501, y=3..7\nx=498, y=2..4\nx=506, y=1..2\nx=498, y=10..13\nx=504, y=10..13\ny=13, x=498..504")

(defn parse-vein [s]
  (let [[a b1 b2] (map read-string (re-seq #"\d+" s))]
    (map (if (= (first s) \x)
           #(vector a %)
           #(vector % a))
      (range b1 (inc b2)))))

(defn part-1 []
  (let [clay (into #{} (mapcat parse-vein (string/split-lines input)))
        min-y (apply min (map second clay))
        max-y (apply max (map second clay))]
    [min-y max-y]))