(ns weaving.facts
  "Immutable facts for textile weaving plant operations.
  Tracks regulatory compliance, batch lineage, and quality records.")

;; ----------------------------- quality fact database -----------------------------

(def quality-standards
  "Known quality thresholds and defect severity levels."
  {:high {:threshold 5 :human-review? true}
   :medium {:threshold 15 :human-review? false}
   :low {:threshold 50 :human-review? false}})

(def regulatory-standards
  "Regulatory requirements by jurisdiction."
  {:japan {:labor-law "Labor Standards Act (労働基準法) §35"
           :safety-law "Industrial Safety and Health Act (労働安全衛生法) §20"
           :quality-standard "Quality Management Standards (品質管理基準) §8"}
   :eu {:machinery-directive "Machinery Directive 2006/42/EC"
        :osha-equivalent "ATEX Directive 2014/34/EU"}})

;; ----------------------------- fact assertions (immutable records) -----------------------------

(defn fabric-batch-fact
  "Create a fact record for a fabric batch."
  [batch-id plant-id loom-id yarn-type length timestamp]
  {:fact-type :fabric-batch
   :id batch-id
   :plant-id plant-id
   :loom-id loom-id
   :yarn-type yarn-type
   :length length
   :timestamp timestamp
   :verified false})

(defn quality-observation-fact
  "Create a fact record for a quality observation."
  [observation-id batch-id defect-type severity timestamp]
  {:fact-type :quality-observation
   :id observation-id
   :batch-id batch-id
   :defect-type defect-type
   :severity severity
   :timestamp timestamp
   :requires-escalation (get-in quality-standards [severity :human-review?] true)})

(defn maintenance-event-fact
  "Create a fact record for a maintenance event."
  [event-id plant-id loom-id maintenance-type timestamp]
  {:fact-type :maintenance-event
   :id event-id
   :plant-id plant-id
   :loom-id loom-id
   :maintenance-type maintenance-type
   :timestamp timestamp})

(defn batch-verification-fact
  "Create a fact record for batch verification."
  [verification-id batch-id plant-id verified? timestamp]
  {:fact-type :batch-verification
   :id verification-id
   :batch-id batch-id
   :plant-id plant-id
   :verified? verified?
   :timestamp timestamp})

;; ----------------------------- regulatory linkage -----------------------------

(defn applicable-standards
  "Return applicable regulatory standards for a jurisdiction."
  [jurisdiction]
  (get regulatory-standards jurisdiction {}))

(defn defect-severity-requires-escalation?
  "Check if a defect severity level requires human escalation."
  [severity]
  (get-in quality-standards [severity :human-review?] true))
