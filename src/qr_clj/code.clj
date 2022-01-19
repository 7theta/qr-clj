;;   Copyright (c) 7theta. All rights reserved.
;;   The use and distribution terms for this software are covered by the
;;   MIT License (https://opensource.org/licenses/MIT) which can also be
;;   found in the LICENSE file at the root of this distribution.
;;
;;   By using this software in any fashion, you are agreeing to be bound by
;;   the terms of this license.
;;   You must not remove this notice, or any others, from this software.

(ns qr-clj.code
  (:import io.nayuki.qrcodegen.QrCode
           io.nayuki.qrcodegen.QrCode$Ecc
           java.awt.image.BufferedImage))

(def ^:private error-correction-level
  "% error toleration for each level:
  `:ecl-low`      ~= 7%
  `:ecl-medium`   ~= 15%
  `:ecl-quartile` ~= 25%
  `:ecl-high`     ~= 30%"
  {:ecl-low QrCode$Ecc/LOW
   :ecl-medium QrCode$Ecc/MEDIUM
   :ecl-quartile QrCode$Ecc/QUARTILE
   :ecl-high QrCode$Ecc/HIGH})

(def default-ecl :ecl-high)

(defn ^QrCode segments->qr
  ([segments]
   (segments->qr segments default-ecl))
  ([segments ecl]
   (QrCode/encodeSegments segments (error-correction-level ecl)))
  ([segments ecl min-version max-version mask boost-ecl]
   (QrCode/encodeSegments segments
                          (error-correction-level ecl)
                          (int min-version)
                          (int max-version)
                          (int mask)
                          boost-ecl)))

(defn ^BufferedImage qr->image
  ([^QrCode qr scale border]
   (qr->image qr scale border 0x000000 0xFFFFFF))
  ([^QrCode qr scale border dark-color light-color]
   (if (or (> border
              (quot Integer/MAX_VALUE 2))
           (> (+ (.size qr) (* border 2))
              (quot Integer/MAX_VALUE scale)))

     (throw "Scale or Border too large")

     (let [dim (* (+ (.size qr) (* border 2)) scale)
           img (BufferedImage. (int dim) (int dim) BufferedImage/TYPE_INT_RGB)]
       (doseq [x (range (.getWidth img))
               y (range (.getHeight img))]
         (.setRGB img
                  (int x)
                  (int y)
                  (if (.getModule qr
                                  (int (- (quot x scale) border))
                                  (int (- (quot y scale) border)))
                    (int dark-color)
                    (int light-color))))
       img))))
