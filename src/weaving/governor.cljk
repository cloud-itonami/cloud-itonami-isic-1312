(ns weaving.governor
  "Textile Weaving Plant Operations Governor -- the independent compliance layer that earns
  the Weaving Operations Advisor the right to propose and log actions.
  The LLM has no notion of weaving plant safety standards, labor regulations,
  or when fabric batch logging or maintenance scheduling is a real-world actuation,
  so this MUST be a separate system able to *reject* a proposal and fall back
  to HOLD.

  HARD violations (a human approver CANNOT override):
    1. Spec-basis       -- no official jurisdiction citation
    2. Plant verification -- plant/batch record must be verified/registered
    3. Quality escalation -- quality defects ALWAYS escalate (never silent log)
    4. Loom-control operations -- NO direct loom parameter control
                                  (those remain mill engineer exclusive authority)

  SOFT violation (can be approved by human):
    5. Confidence floor / actuation gate -- low confidence OR real actuation

  CRITICAL SCOPE BOUNDARY:
  This actor coordinates LOGISTICS and COMPLIANCE PAPERWORK around textile
  weaving production. It does NOT:
    - Control loom operation or warp setup
    - Control shuttle speed, warp tension, or other loom process parameters
    - Operate loom equipment directly
    - Make process-engineering decisions about weft selection or weave pattern design

  Those remain the exclusive authority of licensed mill engineers."
  (:require [weaving.store :as store]))

(def confidence-floor 0.6)

(def high-stakes
  "Operations that require human sign-off for real-world actuation:
  Fabric batch logging and quality defect flags with escalation."
  #{:actuation/log-weaving-batch :actuation/flag-fabric-quality})

(def loom-control-keywords
  "Words that indicate loom engineering authority (FORBIDDEN for this actor).
  If a proposal mentions any of these, it's a hard block."
  #{"warp.tension" "shuttle.speed" "weave.pattern" "weft.feed" "shed.control"
    "loom.control" "loom.mechanism" "thread.tension" "warp.control"
    "spindle.speed" "rpm" "tension.adjustment" "speed.adjustment"})

;; ----------------------------- checks -----------------------------

(defn- spec-basis-violations
  "A proposal with no spec-basis citation is a HARD violation --
  never invent a jurisdiction's requirements."
  [proposal _st]
  (let [op (:op proposal)]
    (when (contains? #{:actuation/log-weaving-batch
                       :actuation/flag-fabric-quality
                       :actuation/schedule-loom-maintenance} op)
      (when (or (empty? (:cites proposal))
                (and (contains? (:value proposal) :spec-basis)
                     (nil? (:spec-basis (:value proposal)))))
        [{:rule :no-spec-basis
          :detail "公式な仕様基準の引用が無い提案は処理できない"}]))))

(defn- plant-verification-violations
  "For batch operations, the owning plant must be verified.
  For plant operations (maintenance), the plant itself must be verified."
  [proposal st]
  (let [op (:op proposal)
        subject (:subject proposal)]
    (when (= op :actuation/schedule-loom-maintenance)
      ;; Maintenance on a plant - check if plant is verified
      (when-not (store/plant-verified? st subject)
        [{:rule :plant-not-verified
          :detail "工場のライセンス / 登録が未確認"}]))))

(defn- batch-verification-violations
  "Batch record must be verified/registered before operations,
  and the owning plant must also be verified."
  [{:keys [op subject]} st]
  (when (contains? #{:actuation/log-weaving-batch
                     :actuation/flag-fabric-quality
                     :actuation/coordinate-fabric-shipment} op)
    (let [batch (store/get-batch st subject)
          plant-id (:plant-id batch)]
      (cond
        (not (store/batch-verified? st subject))
        [{:rule :batch-not-verified
          :detail "織物ロットが未検証 / 未登録"}]

        (and plant-id (not (store/plant-verified? st plant-id)))
        [{:rule :plant-not-verified
          :detail "織物ロットの所有工場が未検証"}]

        :else nil))))

(defn- loom-control-block-violations
  "HARD BLOCK: This actor does NOT make loom engineering decisions.
  If a proposal mentions loom control, warp/shuttle parameters, or other
  loom equipment operations, reject it immediately.
  Those decisions remain the exclusive authority of licensed mill engineers."
  [proposal _st]
  (let [detail (str (:detail (:value proposal)) " " (:op proposal))
        detail-lower (.toLowerCase detail)
        ;; Normalize separators (replace hyphens/spaces with dots for matching)
        normalized (-> detail-lower
                       (.replace "-" ".")
                       (.replace " " "."))
        ;; Check if any keyword pattern appears in the normalized detail
        forbidden (some #(not (nil? (re-find (re-pattern %) normalized))) loom-control-keywords)]
    (when forbidden
      [{:rule :loom-control-forbidden
        :detail (str "織機制御は認可エンジニアの排他的権限です。"
                    "この提案には禁止キーワード '" forbidden "' が含まれています。")}])))

(defn- quality-defect-escalation-violations
  "If quality defect is flagged, this MUST escalate to human.
  Never silently log a quality issue."
  [{:keys [op]} _st]
  (when (= op :actuation/flag-fabric-quality)
    [{:rule :quality-defect-escalation
      :detail "品質不良フラグは必ず人間にエスカレートされる"}]))

(defn- confidence-gate-violations
  "Low confidence or high-stakes actuation -> escalate to human."
  [{:keys [op]} {:keys [confidence]}]
  (let [confidence (or confidence 0.5)]
    (when (or (< confidence confidence-floor)
              (contains? high-stakes op))
      [{:rule :escalate
        :detail (if (< confidence confidence-floor)
                  (str "信頼度が低い (confidence=" confidence ")")
                  "実際の操作には人間の承認が必要")}])))

;; ----------------------------- governor evaluation -----------------------------

(defn evaluate
  "Evaluate a proposal against all hard and soft gates.
  Returns a map:
    {:holds? boolean
     :hard-violations [...]
     :soft-violations [...]
     :clean? boolean}"
  [proposal st]
  (let [hard-checks-store [spec-basis-violations
                           plant-verification-violations
                           batch-verification-violations
                           loom-control-block-violations]
        hard-checks-value [quality-defect-escalation-violations]
        soft-checks [confidence-gate-violations]
        hard-violations-store (mapcat #(% proposal st) hard-checks-store)
        hard-violations-value (mapcat #(% proposal (:value proposal)) hard-checks-value)
        hard-violations (concat hard-violations-store hard-violations-value)
        soft-violations (mapcat #(% proposal (:value proposal)) soft-checks)]
    {:holds? (seq hard-violations)
     :hard-violations (vec hard-violations)
     :soft-violations (vec soft-violations)
     :clean? (and (empty? hard-violations) (empty? soft-violations))}))
