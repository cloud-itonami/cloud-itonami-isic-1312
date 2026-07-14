(ns weaving.store-contract-test
  (:require [clojure.test :refer [deftest is]]
            [weaving.store :as store]))

(deftest plant-registration
  "Plants can be registered and verified."
  (let [st (store/mem-store)
        st (store/register-plant! st "mill-01" "Test Mill")
        st (store/verify-plant! st "mill-01")]
    (is (store/plant-registered? st "mill-01"))
    (is (store/plant-verified? st "mill-01"))))

(deftest batch-registration
  "Batches can be registered and verified."
  (let [st (store/mem-store)
        st (store/register-plant! st "mill-01" "Test Mill")
        st (store/register-batch! st "batch-01" "mill-01" "loom-01" "cotton" 100.0)
        st (store/verify-batch! st "batch-01")]
    (is (store/batch-registered? st "batch-01"))
    (is (store/batch-verified? st "batch-01"))))

(deftest loom-registration
  "Looms can be registered."
  (let [st (store/mem-store)
        st (store/register-plant! st "mill-01" "Test Mill")
        st (store/register-loom! st "loom-01" "mill-01" 200)]
    (is (store/loom-registered? st "loom-01"))
    (let [loom (store/get-loom st "loom-01")]
      (is (= "loom-01" (:id loom)))
      (is (= 200 (:width loom))))))

(deftest quality-flags
  "Quality issues can be flagged."
  (let [st (store/mem-store)
        st (store/register-plant! st "mill-01" "Test Mill")
        st (store/register-batch! st "batch-01" "mill-01" "loom-01" "cotton" 100.0)
        st (store/flag-quality-issue! st "batch-01" "weave-defect" "high")]
    (is (store/has-quality-flags? st "batch-01"))))

(deftest maintenance-records
  "Maintenance records can be logged."
  (let [st (store/mem-store)
        st (store/register-plant! st "mill-01" "Test Mill")
        st (store/log-maintenance! st "mill-01" "loom-cleaning" "2026-07-14")]
    (is (> (count (:maintenance-records st)) 0))))
