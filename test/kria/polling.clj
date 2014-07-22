(ns kria.polling)

(set! *warn-on-reflection* true)

(defn poll
  "Returns true if function `f` returns value `v` within `max-i`
  iterations. Returns false otherwise. Each iteration waits `i-delay`
  milliseconds."
  [v f max-i i-delay]
  (loop [i 0]
    (if (>= i max-i)
      false
      (let [fv (f)]
        (if (= v fv)
          true
          (do
            (Thread/sleep i-delay)
            (recur (inc i))))))))

(defn poll+
  "Returns true if `poll` function returns true and predicate
  function remains true after an additional time delay. This is
  intended to confirm that the function does not change after
  becoming true."
  [v f max-i i-delay]
  (when (poll v f max-i i-delay)
    (Thread/sleep (* 2 i-delay))
    (= v (f))))
