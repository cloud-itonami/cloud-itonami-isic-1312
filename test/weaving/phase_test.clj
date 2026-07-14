(ns weaving.phase-test
  (:require [clojure.test :refer [deftest is]]
            [weaving.phase :as phase]))

(deftest initial-phase-is-idle
  "Initial state should be idle."
  (let [state {:phase :idle}]
    (is (= :idle (:phase state)))))

(deftest transition-to-proposed
  "Can transition to proposed phase."
  (let [state {:phase :idle}
        proposal {:op :actuation/log-weaving-batch}
        new-state (phase/transition-to-proposed state proposal)]
    (is (= :proposed (:phase new-state)))
    (is (= proposal (:proposal new-state)))
    (is (some? (:timestamp new-state)))))

(deftest transition-to-evaluated
  "Can transition to evaluated phase."
  (let [state {:phase :proposed}
        eval-result {:holds? false :clean? true}
        new-state (phase/transition-to-evaluated state eval-result)]
    (is (= :evaluated (:phase new-state)))
    (is (= eval-result (:evaluation new-state)))))

(deftest transition-to-escalated
  "Can transition to escalated phase."
  (let [state {:phase :evaluated}
        new-state (phase/transition-to-escalated state)]
    (is (= :escalated (:phase new-state)))))

(deftest transition-to-approved
  "Can transition to approved phase."
  (let [state {:phase :escalated}
        new-state (phase/transition-to-approved state)]
    (is (= :approved (:phase new-state)))))

(deftest transition-to-rejected
  "Can transition to rejected phase."
  (let [state {:phase :evaluated}
        new-state (phase/transition-to-rejected state "Policy violation")]
    (is (= :rejected (:phase new-state)))
    (is (= "Policy violation" (:rejection-reason new-state)))))

(deftest transition-to-executed
  "Can transition to executed phase."
  (let [state {:phase :approved}
        result {:status :success}
        new-state (phase/transition-to-executed state result)]
    (is (= :executed (:phase new-state)))
    (is (= result (:execution-result new-state)))))

(deftest transition-to-idle
  "Can reset to idle phase."
  (let [state {:phase :executed :execution-result {:status :success}}
        new-state (phase/transition-to-idle state)]
    (is (= :idle (:phase new-state)))))
