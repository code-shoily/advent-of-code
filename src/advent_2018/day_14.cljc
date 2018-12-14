(ns advent-2018.day-14)

(def digits
  (merge
    (zipmap (range 10) (map vector (range 10)))
    (zipmap (map #(+ % 9) (range 9)) (map #(vector 1 %) (range 9)))))

(defn part-1 []
  (loop [idx-1 0
         idx-2 1
         recipes [3 7]]
    (prn idx-1 idx-2)
    (let [recipe-1 (recipes idx-1)
          recipe-2 (recipes idx-2)
          sum (+ recipe-1 recipe-2)
          recipes (apply conj recipes (digits sum))
          c (count recipes)]
      (if (> c 5)
        recipes
        (recur (mod (inc recipe-1) c) (mod (inc recipe-2) c) recipes)))))