(ns weaving.registry
  "Registry of proposal drafts for testing and verification.")

;; ----------------------------- proposal templates for testing -----------------------------

(defn weaving-batch-logging-draft
  "Create a weaving batch logging proposal draft."
  [batch-id cites evidence confidence detail]
  {:op :actuation/log-weaving-batch
   :subject batch-id
   :effect :propose
   :cites cites
   :value {:evidence evidence :confidence confidence :detail detail}})

(defn loom-maintenance-draft
  "Create a loom maintenance scheduling proposal draft."
  [plant-id cites evidence confidence detail]
  {:op :actuation/schedule-loom-maintenance
   :subject plant-id
   :effect :propose
   :cites cites
   :value {:evidence evidence :confidence confidence :detail detail}})

(defn fabric-quality-defect-draft
  "Create a fabric quality defect flag proposal draft."
  [batch-id cites evidence confidence defect-type severity detail]
  {:op :actuation/flag-fabric-quality
   :subject batch-id
   :effect :propose
   :cites cites
   :value {:evidence evidence :confidence confidence :defect-type defect-type
           :severity severity :detail detail}})

(defn fabric-shipment-draft
  "Create a fabric shipment coordination proposal draft."
  [batch-id cites evidence confidence detail destination quantity]
  {:op :actuation/coordinate-fabric-shipment
   :subject batch-id
   :effect :propose
   :cites cites
   :value {:evidence evidence :confidence confidence :destination destination
           :quantity quantity :detail detail}})
