(ns weaving.sim
  "Simulation and demo for Textile Weaving Plant Operations Actor.

  This module demonstrates the actor pattern: proposal -> governor evaluation ->
  escalation if needed -> human review -> execution."
  (:require [weaving.store :as store]
            [weaving.advisor :as advisor]
            [weaving.governor :as governor]
            [weaving.registry :as registry]))

;; ----------------------------- demo setup -----------------------------

(defn setup-demo-state
  "Create a demo state with registered and verified plants/looms/batches."
  []
  (let [st (store/mem-store)
        st (store/register-plant! st "cotton-mill-01" "Northern Cotton Weaving Works")
        st (store/verify-plant! st "cotton-mill-01")
        st (store/register-loom! st "loom-01" "cotton-mill-01" 250)
        st (store/register-loom! st "loom-02" "cotton-mill-01" 250)
        st (store/register-batch! st "batch-2026-001" "cotton-mill-01" "loom-01" "cotton-sateen" 500.0)
        st (store/verify-batch! st "batch-2026-001")]
    st))

;; ----------------------------- demo scenario 1: successful batch logging -----------------------------

(defn demo-successful-batch-logging
  "Scenario: Batch logging with full verification passes and escalates to human."
  []
  (println "\n=== Scenario 1: Successful Batch Logging ===")
  (let [st (setup-demo-state)
        adv (advisor/mock-advisor)
        proposal (advisor/weaving-batch-logging-proposal adv "batch-2026-001")
        eval-result (governor/evaluate proposal st)]
    (println "Proposal:" proposal)
    (println "Evaluation result:")
    (println "  Hard violations:" (:hard-violations eval-result))
    (println "  Soft violations:" (:soft-violations eval-result))
    (println "  Clean?" (:clean? eval-result))
    (println "  Holds?" (:holds? eval-result))
    (when (:holds? eval-result)
      (println "Status: PROPOSAL HELD for human review"))))

;; ----------------------------- demo scenario 2: loom control block -----------------------------

(defn demo-loom-control-block
  "Scenario: Proposal mentioning loom control is immediately blocked."
  []
  (println "\n=== Scenario 2: Loom Control Block ===")
  (let [st (setup-demo-state)
        proposal {:op :actuation/log-weaving-batch
                  :subject "batch-2026-001"
                  :effect :propose
                  :cites ["Textile Safety Regulations §12"]
                  :value {:evidence {:batch-registered true}
                          :confidence 0.9
                          :detail "Please increase warp tension to 900 and adjust shuttle speed"}}
        eval-result (governor/evaluate proposal st)]
    (println "Proposal:" proposal)
    (println "Evaluation result:")
    (println "  Hard violations:" (:hard-violations eval-result))
    (when (seq (:hard-violations eval-result))
      (println "Status: HARD BLOCK - loom control forbidden"))))

;; ----------------------------- demo scenario 3: quality defect escalation -----------------------------

(defn demo-quality-defect-escalation
  "Scenario: Quality defect always escalates to human."
  []
  (println "\n=== Scenario 3: Quality Defect Escalation ===")
  (let [st (setup-demo-state)
        adv (advisor/mock-advisor)
        proposal (advisor/fabric-quality-defect-proposal adv "batch-2026-001" "loose-weave" "high")
        eval-result (governor/evaluate proposal st)]
    (println "Proposal:" proposal)
    (println "Evaluation result:")
    (println "  Hard violations:" (:hard-violations eval-result))
    (println "  Soft violations:" (:soft-violations eval-result))
    (when (some #(= (:rule %) :quality-defect-escalation) (:hard-violations eval-result))
      (println "Status: QUALITY DEFECT - mandatory escalation to human"))))

;; ----------------------------- demo scenario 4: unverified batch block -----------------------------

(defn demo-unverified-batch-block
  "Scenario: Operations on unverified batches are blocked."
  []
  (println "\n=== Scenario 4: Unverified Batch Block ===")
  (let [st (store/mem-store)
        st (store/register-plant! st "test-mill" "Test Weaving Mill")
        st (store/verify-plant! st "test-mill")
        st (store/register-batch! st "batch-unverified" "test-mill" "loom-01" "wool" 100.0)
        ;; Do NOT verify the batch
        proposal (registry/weaving-batch-logging-draft "batch-unverified"
                   ["Textile Safety Regulations §12"]
                   {:batch-registered true}
                   0.9
                   "Logging unverified batch")
        eval-result (governor/evaluate proposal st)]
    (println "Proposal:" proposal)
    (println "Evaluation result:")
    (println "  Hard violations:" (:hard-violations eval-result))
    (when (some #(= (:rule %) :batch-not-verified) (:hard-violations eval-result))
      (println "Status: HARD BLOCK - batch not verified"))))

;; ----------------------------- main demo runner (Node/nbb compatible) -----------------------------

(defn -main
  "Run all demo scenarios."
  [& _args]
  (println "Textile Weaving Plant Operations Actor - Simulation")
  (println "=" (apply str (repeat 48 "=")))
  (demo-successful-batch-logging)
  (demo-loom-control-block)
  (demo-quality-defect-escalation)
  (demo-unverified-batch-block)
  (println "\n" (apply str (repeat 56 "=")))
  (println "Simulation complete."))

;; Node/nbb entry point
#?(:cljs
   (when (some? (try (js/require.resolve "nbb") (catch :default _e nil)))
     (-main)))
