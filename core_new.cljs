(ns cljscript.core
    (:require [reagent.core :as reagent :refer [atom]][time.core][tick.alpha.api :as t][clojure.string :as str]))

(enable-console-print!)

(defonce app-state (atom {:text "Hello world!"}))

; heute
(def aPositiv (atom 0))
(def aNegativ (atom 0))

; gestern
(def aOldPositiv (atom 0))
(def aOldNegativ (atom 0))


(def aDone false)

(def aImgState (atom "pfeilR.png"))

(def aText (atom "Bewerten Sie hier Ihren Tag"))


;new ______
(def hour (atom 0))


(defn page []
  [:div#page1
   [:p#text1 @aText]
   [:p#text4 (str "Auswertung: " @aOldNegativ "-, " @aOldPositiv "+ Heute: " @aNegativ "-, " @aPositiv "+" )]
   [:button#button1 {:on-click (fn [e] (do(swap! aPositiv inc)(reset! aText "Danke")(js/setTimeout #(reset! aText "Bewerten Sie hier Ihren Tag") 3000)))} " + "]
   [:button#button2 {:on-click (fn [e] (do(swap! aNegativ inc)(reset! aText "Danke")(js/setTimeout #(reset! aText "Bewerten Sie hier Ihren Tag") 3000)))} " - "]
   [:br]
   [:br]
   [:p#text2 "Letzter Trend"]
   [:img#trend{:src @aImgState}]
   [:br] 
   [:br]
   [:img#logo{:src "logo.jpg"}]
  ]
)

(reagent/render-component [page] (. js/document (getElementById "app")))

(defn on-js-reload []
  ;; optionally touch your app-state to force rerendering depending on
  ;; your application
  ;; (swap! app-state update-in [:__figwheel_counter] inc)
)


(js/setInterval #(do
	
	(reset! hour (int(nth(str/split(nth(str/split(str(time.core/date)) #" ")4)#":")0)))

	(if (not= (str(t/day-of-week (t/today))) "SATURDAY")
		(if (not= (str(t/day-of-week (t/today))) "FRIDAY")
			(if (= @hour 7)(reset! aDone false)())
		())
	())

	(if (= @hour 8)
		(if (@aDone)
			(do
				(reset! aOldPositiv @aPositiv)
				(reset! aOldNegativ @aNegativ)
				(reset! aDone true)
				(reset! aPositiv 0)
				(reset! aNegativ 0)
				(if (< (+ @aOldNegativ 5) @aOldPositiv)(reset! aImgState "pfeilU.png")())
				(if (> @aOldNegativ (+ @aOldPositiv 5))(reset! aImgState "pfeilD.png")())
				(if-not (and (< (+ @aOldNegativ 5) @aOldPositiv)(< @aOldNegativ (+ @aOldPositiv 5)))(reset! aImgState "pfeilR.png")())	
			);do
		());@aDone
	());@hour
  );do
2000);setInterval 2sek