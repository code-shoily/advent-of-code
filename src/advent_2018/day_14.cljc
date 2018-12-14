(ns advent-2018.day-14
  (:require
   [clojure.string :as string]))

(def digits
  (merge
    (zipmap (range 10) (map vector (range 10)))
    (zipmap (map #(+ % 10) (range 9)) (map #(vector 1 %) (range  9)))))

(defn solve [n]
  (loop [idx-1 0 idx-2 1 recipes [3 7]]
    (let [recipe-1 (recipes idx-1)
          recipe-2 (recipes idx-2)
          sum (+ recipe-1 recipe-2)
          recipes (apply conj recipes (digits sum))
          c (count recipes)]
      (if (> c (+ n 10))
        (string/join (subvec recipes n (+ n 10)))
        (recur (mod (+ idx-1 recipe-1 1) c) (mod (+ idx-2 recipe-2 1) c) recipes)))))