(ns advent-2018.day-14)

(def digits
  (merge
    (zipmap (range 10) (map vector (range 10)))
    (zipmap (map #(+ % 9) (range 9)) (map #(vector 1 %) (range 9)))))

(defn part-1 []
  (loop [idx-1 0
         idx-2 1
         recipies [3 7]]
    (let [sum (+ (recipies idx-1) (recipies idx-2))])))