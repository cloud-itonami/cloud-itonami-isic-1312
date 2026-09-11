(ns weaving.phase
  "State phases for weaving actor operations.")

(defn- current-time-ms
  "Get current time in milliseconds (portable across JVM and ClojureScript)."
  []
  #?(:clj (System/currentTimeMillis)
     :cljs (js/Date.now)))

;; ----------------------------- phase definitions -----------------------------

(def phases
  "Enumerated phases for a proposal in the weaving actor lifecycle."
  {:idle "Actor waiting for input"
   :proposed "Proposal received from advisor"
   :evaluated "Governor evaluation complete"
   :escalated "Proposal held for human review"
   :approved "Proposal approved to execute"
   :rejected "Proposal rejected by governor"
   :executed "Actuation complete"})

;; ----------------------------- phase transitions -----------------------------

(defn transition-to-proposed
  "Transition to proposed phase when advisor creates a proposal."
  [state proposal]
  (assoc state
    :phase :proposed
    :proposal proposal
    :timestamp (current-time-ms)))

(defn transition-to-evaluated
  "Transition to evaluated phase after governor evaluation."
  [state eval-result]
  (assoc state
    :phase :evaluated
    :evaluation eval-result
    :timestamp (current-time-ms)))

(defn transition-to-escalated
  "Transition to escalated phase when hard or soft violations exist."
  [state]
  (assoc state
    :phase :escalated
    :timestamp (current-time-ms)))

(defn transition-to-approved
  "Transition to approved phase after human review."
  [state]
  (assoc state
    :phase :approved
    :timestamp (current-time-ms)))

(defn transition-to-rejected
  "Transition to rejected phase if proposal is rejected."
  [state reason]
  (assoc state
    :phase :rejected
    :rejection-reason reason
    :timestamp (current-time-ms)))

(defn transition-to-executed
  "Transition to executed phase after successful actuation."
  [state result]
  (assoc state
    :phase :executed
    :execution-result result
    :timestamp (current-time-ms)))

(defn transition-to-idle
  "Transition back to idle after completion."
  [_state]
  {:phase :idle :timestamp (current-time-ms)})
