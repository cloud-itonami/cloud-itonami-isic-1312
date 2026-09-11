(ns weaving.facts-test
  (:require [clojure.test :refer [deftest is]]
            [weaving.facts :as facts]))

(deftest ^{:doc "Quality standards should define severity levels."} quality-standards-defined
  (is (contains? facts/quality-standards :high))
  (is (contains? facts/quality-standards :medium))
  (is (contains? facts/quality-standards :low)))

(deftest ^{:doc "High severity defects require human escalation."} high-severity-requires-escalation
  (is (true? (facts/defect-severity-requires-escalation? :high))))

(deftest ^{:doc "Fabric batch facts can be created."} fabric-batch-fact-creation
  (let [fact (facts/fabric-batch-fact "batch-001" "mill-01" "loom-01" "cotton" 500.0 (System/currentTimeMillis))]
    (is (= :fabric-batch (:fact-type fact)))
    (is (= "batch-001" (:id fact)))
    (is (= "mill-01" (:plant-id fact)))))

(deftest ^{:doc "Quality observation facts can be created."} quality-observation-fact-creation
  (let [fact (facts/quality-observation-fact "obs-001" "batch-001" "loose-weave" "high" (System/currentTimeMillis))]
    (is (= :quality-observation (:fact-type fact)))
    (is (= "batch-001" (:batch-id fact)))
    (is (= "high" (:severity fact)))
    (is (true? (:requires-escalation fact)))))

(deftest ^{:doc "Maintenance event facts can be created."} maintenance-event-fact-creation
  (let [fact (facts/maintenance-event-fact "maint-001" "mill-01" "loom-01" "cleaning" (System/currentTimeMillis))]
    (is (= :maintenance-event (:fact-type fact)))
    (is (= "mill-01" (:plant-id fact)))))

(deftest ^{:doc "Batch verification facts can be created."} batch-verification-fact-creation
  (let [fact (facts/batch-verification-fact "verify-001" "batch-001" "mill-01" true (System/currentTimeMillis))]
    (is (= :batch-verification (:fact-type fact)))
    (is (= "batch-001" (:batch-id fact)))
    (is (true? (:verified? fact)))))

(deftest ^{:doc "Regulatory standards should be defined for jurisdictions."} regulatory-standards-available
  (is (contains? facts/regulatory-standards :japan))
  (let [japan-standards (facts/applicable-standards :japan)]
    (is (contains? japan-standards :labor-law))
    (is (contains? japan-standards :safety-law))))
