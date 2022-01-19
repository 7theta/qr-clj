;;   Copyright (c) 7theta. All rights reserved.
;;   The use and distribution terms for this software are covered by the
;;   MIT License (https://opensource.org/licenses/MIT) which can also be
;;   found in the LICENSE file at the root of this distribution.
;;
;;   By using this software in any fashion, you are agreeing to be bound by
;;   the terms of this license.
;;   You must not remove this notice, or any others, from this software.

(ns qr-clj.core
  (:require [qr-clj.code :as qrc]
            [qr-clj.segment :as qrs]))

(defn encode-bytes
  ([^bytes data scale border]
   (encode-bytes data scale border qrc/default-ecl))
  ([^bytes data scale border ecl]
   (-> data
       (qrs/->byte-segment)
       (vector)
       (qrc/segments ecl)
       (qrc/qr->image scale border))))

(defn encode-string
  ([^String data scale border]
   (encode-string data scale border qrc/default-ecl))
  ([^String data scale border ecl]
   (-> data
       (qrs/->segments)
       (qrc/segments ecl)
       (qrc/qr->image scale border))))
