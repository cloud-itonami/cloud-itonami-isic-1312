(ns weaving.facts-test
  (:require [clojure.test :refer [deftest is]]
            [weaving.facts :as facts]))

(deftest quality-standards-defined
  "Quality standards should define severity levels."
  (is (contains? facts/quality-standards :high))
  (is (contains? facts/quality-standards :medium))
  (is (contains? facts/quality-standards :low)))

(deftest high-severity-requires-escalation
  "High severity defects require human escalation."
  (is (true? (facts/defect-severity-requires-escalation? :high))))

(deftest fabric-batch-fact-creation
  "Fabric batch facts can be created."
  (let [fact (facts/fabric-batch-fact "batch-001" "mill-01" "loom-01" "cotton" 500.0 (System/currentTimeMillis))]
    (is (= :fabric-batch (:fact-type fact)))
    (is (= "batch-001" (:id fact)))
    (is (= "mill-01" (:plant-id fact)))))

(deftest quality-observation-fact-creation
  "Quality observation facts can be created."
  (let [fact (facts/quality-observation-fact "obs-001" "batch-001" "loose-weave" "high" (System/currentTimeMillis))]
    (is (= :quality-observation (:fact-type fact)))
    (is (= "batch-001" (:batch-id fact)))
    (is (= "high" (:severity fact)))
    (is (true? (:requires-escalation fact)))))

(deftest maintenance-event-fact-creation
  "Maintenance event facts can be created."
  (let [fact (facts/maintenance-event-fact "maint-001" "mill-01" "loom-01" "cleaning" (System/currentTimeMillis))]
    (is (= :maintenance-event (:fact-type fact)))
    (is (= "mill-01" (:plant-id fact)))))

(deftest batch-verification-fact-creation
  "Batch verification facts can be created."
  (let [fact (facts/batch-verification-fact "verify-001" "batch-001" "mill-01" true (System/currentTimeMillis))]
    (is (= :batch-verification (:fact-type fact)))
    (is (= "batch-001" (:batch-id fact)))
    (is (true? (:verified? fact)))))

(deftest regulatory-standards-available
  "Regulatory standards should be defined for jurisdictions."
  (is (contains? facts/regulatory-standards :japan))
  (let [japan-standards (facts/applicable-standards :japan)]
    (is (contains? japan-standards :labor-law))
    (is (contains? japan-standards :safety-law))))
