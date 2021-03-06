(ns workshop.chap03)

(use 'clojure.repl)

;;; Destructuring

(defn print-coords [coords]
  (let [lat (first coords)
        lon (last coords)]
    (println (str "Latitude: " lat " - " "Longitude: " lon))))

(print-coords [48.9615 2.4372])

;; destructuring
(defn print-coords [coords]
  (let [[lat lon] coords]
    (println (str "Latitude: " lat " - " "Longitude: " lon))))

(print-coords [48.9615 2.4372])

(let [[a b c] [1 2 3]] (println a b c))

(let [[a b c] '(1 2 3)] (println a b c))

;; Airport example
(def paris
  {:lat  48.9615, :lon 2.4372, :code 'LFPB', :name "Paris Le Bourget Airport"})

(defn print-coords [airport]
  (let [lat (:lat airport)
        lon (:lon airport)
        name (:name airport)]
    (println (str name " is located at Latitude: " lat " - " "Longitude: " lon))))

(print-coords paris)

;; associative destructuring

(defn print-coords [airport]
  (let [{lat :lat lon :lon airport-name :name} airport]
    (println (str airport-name " is located at Latitude: " lat " - " "Longitude: " lon))))

(print-coords paris)

;; when the keys and symbols have the same name - `:keys`

(defn print-coords [airport]
  (let [{:keys [lat lon name]} airport]
    (println (str name " is located at Latitude: " lat " - " "Longitude: " lon))))

(print-coords paris)


;;; Exercise 3.01: Parsing Fly Vector's Data with Sequential Destructuring
;;  airport continued

;; examples
;; a coordinate point
[48.9615, 2.4372]
;; a flight is a tuple of two coordinate points
[[48,9615, 2.4372], [37.742, -25.6978]]
;; a booking is some information followed by one or more flights
;; internal ID
;; name of the passenger.
;; some sensitive data.
[1425,
 "Bob Smith",
 "Allergic to unsalted peanuts only",
 [[48.9615, 2.4372], [37.742, -25.6976]],
 [[37.742, -25.6976], [48.9615, 2.4372]]
 ]

(def booking [1425,
              "Bob Smith",
              "Allergic to unsalted peanuts only",
              [[48.9615, 2.4372], [37.742, -25.6976]],
              [[37.742, -25.6976], [48.9615, 2.4372]] ])

;; parsing  - dont print out sensitive data
(let [[id customer-name sensitive-info flight1 flight2 flight3] booking]
  (println id customer-name flight1 flight2 flight3))

(conj booking [[37.742, -25.6976], [51.1537, 0.1821]]
      [[51.1537, 0.1821], [48.9615, 2.4372]])

;; skip last flight.
(let [big-booking (conj booking [[37.742, -25.6976], [51.1537, 0.1821]]
                        [[51.1537, 0.1821], [48.9615, 2.4372]])
      [id customer-name sensitive-info flight1 flight2 flight3] big-booking]
  (println id customer-name flight1 flight2 flight3))

;; ignore id and sensitive data - `_` symbol
(let [[_ customer-name _ flight1 flight2 flight3] booking]
  (println customer-name flight1 flight2 flight3))

;; repeating flights  - `&` symbol
(let [[_ customer-name _ & flights] booking]
  (println customer-name " booked " (count flights) " flights"))

;; function - print-flight
(defn print-flight [flight]
  (let [[[lat1 lon1] [lat2 lon2]] flight]
    (println (str "Flying from: Lat " lat1 " Lon " lon1 " Flying to: Lat " lat2 " Lon " lon2))))

(print-flight [[48.9615, 2.4372], [37.742 -25.6976]])

;; simplify
(defn print-flight [flight]
  (let [[departure arrival] flight
        [lat1 lon1] departure
        [lat2 lon2] arrival]
    (println (str "Flying from: Lat " lat1 " Lon " lon1 " Flying to: Lat " lat2 " Lon " lon2))))

(print-flight [[48.9615, 2.4372], [37.742 -25.6976]])


(defn print-booking [booking]
  (let [[_ customer-name _ & flights] booking]
    (println customer-name " booked " (count flights) " flights")
    (let [[flight1 flight2 flight3] flights]
      (when flight1 (print-flight flight1))
      (when flight2 (print-flight flight2))
      (when flight3 (print-flight flight3)))))

(print-booking booking)


;;; Exercise 3.02: Parsing MapJet Data with Associative Destructuring

(def mapjet-booking
  {:id 8773
   :customer-name "Alice Smith"
   :catering-notes "Vegetarian on Sundays"
   :flights [{:from {:lat 48.9615 :lon 2.4372 :name "Paris Le Bourget Airport"},
              :to {:lat 37.742 :lon -25.6976 :name "Ponta Delgada Airport"}},
             {:from {:lat 37.742 :lon -25.6976 :name "Ponta Delgada Airport"},
              :to {:lat 48.9615 :lon 2.4372 :name "Paris Le Bourget Airport"}}
             ]})


;; associative destructuring
(let [{:keys [customer-name flights]} mapjet-booking]
  (println (str customer-name " booked " (count flights) " flights.")))

(defn print-mapjet-flight [flight]
  (let [{:keys [from to]} flight
        {lat1 :lat lon1 :lon} from
        {lat2 :lat lon2 :lon} to]
    (println (str "Flying from: Lat " lat1 " Lon " lon1 " Flying to: Lat " lat2 " Lon " lon2))))

(print-mapjet-flight (first (:flights mapjet-booking)))

;; nested associative destructuring expressions

(defn print-mapjet-flight [flight]
  (let [{{lat1 :lat lon1 :lon} :from,
         {lat2 :lat lon2 :lon} :to} flight]
    (println (str "Flying from: Lat " lat1 " Lon " lon1 " Flying to: Lat " lat2 " Lon " lon2))))

(print-mapjet-flight (first (:flights mapjet-booking)))

(defn print-mapjet-booking [booking]
  (let [{:keys [customer-name flights]} booking]
    (println (str customer-name " booked " (count flights) " flights."))
    (let [[flight1 flight2 flight3] flights]
      (when flight1 (print-mapjet-flight flight1)) flights
      (when flight2 (print-mapjet-flight flight2))
      (when flight3 (print-mapjet-flight flight3)))))

(print-mapjet-booking mapjet-booking)


;;; Advanced Call Structures

;;; Destructuring Function Parameters

(defn print-flight
  [[[lat1 lon1] [lat2 lon2]]]
  (println (str "Flying from: Lat " lat1 " Lon " lon1 " Flying to: Lat " lat2 " Lon " lon2)))

(print-flight [[48.9615, 2.4372], [37.742 -25.6976]])

;; nested associative destructuring
(defn print-mapjet-flight
  [{{lat1 :lat lon1 :lon} :from, {lat2 :lat lon2 :lon} :to}]
  (println (str "Flying from: Lat " lat1 " Lon " lon1 " Flying to: Lat " lat2 " Lon " lon2)))

(print-mapjet-flight { :from {:lat 48.9615 :lon 2.4372}, :to {:lat 37.742 :lon -25.6976} })


;;; Arity Overloading

(defn no-overloading []
  (println "Same old, same old ..."))

(no-overloading)

(defn overloading
  ([] "No argument")
  ([a] (str "One argument: " a))
  ([a b] (str "Two arguments: a " a " b: " b)))

(overloading)
(overloading 1)
(overloading 1 2)
(overloading 1 nil)


;; game
(def weapon-damage {:fists 10 :staff 35 :sword 100 :cast-iron-saucepan 150})

(defn strike
  ([enemy] (strike enemy :fists))
  ([enemy weapon]
   (let [damage (weapon weapon-damage)]
     (update enemy :health - damage))))

(strike {:name "n00b-hunter" :health 100})
;; => {:name "n00b-hunter", :health 90}

(strike {:name "n00b-hunter" :health 100} :sword)
;; => {:name "n00b-hunter", :health 0}

(strike {:name "n00b-hunter" :health 100} :cast-iron-saucepan)
;; => {:name "n00b-hunter", :health -50}


;;; Variadic Functions

;; takes multiple parameters but isn't overloaded
(str "Concatenating " "is " "difficult " "to " "spell " "but " "easy " "to " "use!")
;; => "Concatenating is difficult to spell but easy to use!"


(defn welcome
  [player & friends]
  (println (str "Welcome to the Parenthmazes " player "!"))
  (when (seq friends)
    (println (str "Sending " (count friends) " friend request(s) to the following players: " (clojure.string/join ", " friends)))))

(welcome "Jon")
(welcome "Jon" "Arya" "Tyrion" "Petyr")

;; improved
(defn welcome
  ([player] (println (str "Welcome to Parenthmazes (single-player mode), " player "!")))
  ([player & friends]
   (println (str "Welcome Parenthmazes (multi-player mode), " player "!"))
   (println (str "Sending " (count friends) " friend request(s) to the following players: " (clojure.string/join ", " friends)))))

(welcome "Jon")
(welcome "Jon" "Arya" "Tyrion" "Petyr")


;;; Exercise 3.03: Multi-arity and Destructuring with Parenthmazes

(def weapon-damage {:fists 10.0 :staff 35.0 :sword 100.0 :cast-iron-saucepan 150.0})

(defn strike
  ([target weapon]
   (let [points (weapon weapon-damage)]
     (if (= :gnomes (:camp target))
       (update target :health + points)
       (update target :health - points)))))

(def enemy {:name "Zulkaz", :health 250, :camp :trolls})

(strike enemy :sword)
;; => {:name "Zulkaz", :health 150.0, :camp :trolls}

(def ally {:name "Carla", :health 80, :camp :gnomes})

(strike ally :staff)
;; => {:name "Carla", :health 45.0, :camp :gnomes}

(defn strike
  ([target weapon]
   (let [points (weapon weapon-damage)]
     (if (= :gnomes (:camp target))
       (update target :health + points)
       (let [armor (or (:armor target) 0)
             damage (* points (- 1 armor))]
         (update target :health - damage))))))

;; without armor
(strike enemy :cast-iron-saucepan)
;; => {:name "Zulkaz", :health 100.0, :camp :trolls}

;; with armor
(def enemy2 {:name "Zulkaz", :health 250, :armor 0.8, :camp :trolls})

(strike enemy2 :cast-iron-saucepan)
;; => {:name "Zulkaz", :health 220.0, :armor 0.8, :camp :trolls}

;; associative destructuring of parameters
(defn strike
  ([{:keys [camp armor] :as target} weapon]
   (let [points (weapon weapon-damage)]
     (if (= :gommes camp)
       (update target :health + points)
       (let [damage (* points (- 1 (or armor 0)))]
         (update target :health - damage))))))

(strike enemy2 :cast-iron-saucepan)
;; => {:name "Zulkaz", :health 220.0, :armor 0.8, :camp :trolls}

;; `:or` provides a default value for `:armor`
(defn strike
  "With one argument, strike a target with a default :fists `weapon`.
  With two argument, strike a target with `weapon`"
  ([target] (strike target :fists))
  ([{:keys [camp armor], :or {armor 0}, :as target} weapon]
   (let [points (weapon weapon-damage)]
     (if (= :gnomes camp)
       (update target :health + points)
       (let [damage (* points (- 1 armor))]
         (update target :health - damage))))))

(strike enemy2)
;; => {:name "Zulkaz", :health 248.0, :armor 0.8, :camp :trolls}

(strike enemy2 :cast-iron-saucepan)
;; => {:name "Zulkaz", :health 220.0, :armor 0.8, :camp :trolls}

(strike ally :staff)
;; => {:name "Carla", :health 115.0, :camp :gnomes}

(strike enemy)
;; => {:name "Zulkaz", :health 240.0, :camp :trolls}


;;; Higher-Order Programming

;;; First-Class Functions

;; functions as parameters
(update {:itme "Tomato" :price 1.0} :price (fn [x] (/ x 2)))
;; => {:itme "Tomato", :price 0.5}

;; pass a function and a parameter
(update {:time "Tomato" :price 1.0} :price / 2)
;; => {:time "Tomato", :price 0.5}

(update {:item "Tomato" :fruit false} :fruit not)
;; => {:item "Tomato", :fruit true}

;; our own function
(defn operate [f x] (f x))

(operate inc 2)
;; => 3

(operate clojure.string/upper-case "hello.")
;; => "HELLO."

;; any number of arguments
(defn operate [f & args] (apply f args))
;;(operate + [1 2 3])

(apply + [1 2 3])
;; => 6

(operate str "It " "Should " "Concatenate!")
;; => "It Should Concatenate!"

;; return a function
(defn random-fn [] (first (shuffle [+ - * /])))

((random-fn) 2 3)
;; => 2/3
;; => 5

(fn? random-fn)
;; => true

(fn? (random-fn))
;; => true

(let [mysterious-fn (random-fn)]
  (mysterious-fn 2 3))
;; => -1
;; => 5
;; => 2/3

;;; Partial Functions -  `partial`

(def marketing-adder (partial + 0.99))

(marketing-adder 10 5)
;; => 15.99

(def format-price (partial str "$"))

(format-price "100")
;; => "$100"
(format-price 10 50)
;; => "$1050"

;;; Composing Functions - `comp`
(defn sample [coll] (first (shuffle coll)))

(sample [1 2 3 4])
;; => 2

;; more elegant
(def sample (comp first shuffle))

(sample [1 2 3 4])
;; => 1

((comp inc *) 2 2)
;; => 5

;; ((comp * inc) 2 2)  ; wrong order

;; combine `partial` and `comp`
(def checkout
  (comp (partial str "Only ") format-price marketing-adder))

(checkout 10 5 15 6 9)
;; => "Only $45.99"

;; `#()` reader macro

(fn [s] (str "Hello" s))
;; or
#(str "Hello" %)

(fn [x y] (* (+ x 10) (+ y 20)))
;; or
#(* (+ %1 10) (+ %2 20))

(#(str %1 " " %2 " " %3) "First" "Second" "Third")
;; => "First Second Third"


;;; Exercise 3.04: High-Order Functions with Parenthmazes

(def weapon-fn-map
  {:fists (fn [health] (if (< health 100) (- health 10) health))
   :staff (partial + 35)
   :sword #(- % 100)
   :cast-iron-saucepan #(- % 100 (rand-int 50))
   :sweet-potato identity})

((weapon-fn-map :fists) 150)
;; => 150
((weapon-fn-map :fists) 50)
;; => 40
((weapon-fn-map :staff) 150)
;; => 185
((weapon-fn-map :sword) 150)
;; => 50
((weapon-fn-map :cast-iron-saucepan) 200)
;; => 78
((weapon-fn-map :cast-iron-saucepan) 200)
;; => 60
((weapon-fn-map :sweet-potato) 150)
;; => 150

;; `strike` using the new `weapon-fn-map`
(defn strike
  "With one argument, strike a target with a default `:fists` `weapon`. With
  two arguments, strike a target with `weapon` an return the target entity"
  ([target] (strike target :fists))
  ([target weapon]
   (let [weapon-fn (weapon weapon-fn-map)]
     (update target :health weapon-fn))))

(def enemy {:name "Arnold", :health 250})

(strike enemy :sweet-potato)
;; => {:name "Arnold", :health 250}
(strike enemy :sword)
;; => {:name "Arnold", :health 150}
(strike enemy :cast-iron-saucepan)
;; => {:name "Arnold", :health 134}

;; more than one weapon
(strike (strike enemy :sword) :cast-iron-saucepan)
;; => {:name "Arnold", :health 5}

;; `update` using `comp`
(update enemy :health (comp (:sword weapon-fn-map) (:cast-iron-saucepan weapon-fn-map)))
;; => {:name "Arnold", :health 3}
;; => {:name "Arnold", :health 31}

;; use `comp` derive a function that uses all weapons
(defn mighty-strike
  "Strike a `target` with all weapons!"
  [target]
  (let [weapon-fn (apply comp (vals weapon-fn-map))]
    (update target :health weapon-fn)))

(mighty-strike enemy)
;; => {:name "Arnold", :health 32}
;; => {:name "Arnold", :health 29}


;;; Multimethods - runtime polymorphism.

(defmulti strike (fn [m] (get m :weapon)))
;; equivalent
(ns-unmap 'workshop.chap03 'strike)
(defmulti strike :weapon)

(defmethod strike :sword
  [{{:keys [:health]} :target}]
  (- health 100))

(defmethod strike :default [{{:keys [:health]} :target}] health)

(strike {:weapon :cast-iron-saucepan :target {:health 200}})
;; => 200

(ns-unmap 'workshop.chap03 'strike)

(defmulti strike
  (fn [{{:keys [:health]} :target weapon :weapon}]
    (if (< health 50) :finisher weapon)))

(defmethod strike :finisher [_] 0)

(defmethod strike :sword
  [{{:keys [:health]} :target}]
  (- health 100))

(defmethod strike :default [{{:keys [:health]} :target}] health)

(strike {:weapon :sword :target {:health 200}})
;; => 100

(strike {:weapon :spoon :target {:health 30}});
;; => 0


;;; Exercise 3.05: Using Multimethods
;;  Parenthmazes 4.0

;; Player entity
(def player
  {:name "Lea" :health 200 :position {:x 10 :y 10 :facing :north}})

(defmulti move #(:facing (:position %)))

(ns-unmap 'workshop.chap03 'move)

(defmulti move (comp :facing :position))

(defmethod move :north
  [entity]
  (update-in entity [:position :y] inc))

;;player
;; => {:name "Lea", :health 200, :position {:x 10, :y 10, :facing :north}}

(move player)
;; => {:name "Lea", :health 200, :position {:x 10, :y 11, :facing :north}}

(defmethod move :south
  [entity]
  (update-in entity [:position :y] dec))

(defmethod move :west
  [entity]
  (update-in entity [:position :x] inc))

(defmethod move :east
  [entity]
  (update-in entity [:position :x] dec))

(move {:position {:x 10 :y 10 :facing :west}})
;; => {:position {:x 11, :y 10, :facing :west}}
(move {:position {:x 10 :y 10 :facing :south}})
;; => {:position {:x 10, :y 9, :facing :south}}
(move {:position {:x 10 :y 10 :facing :east}})
;; => {:position {:x 9, :y 10, :facing :east}}

;; default move method
(defmethod move :default [entity] entity)

(move {:position {:x 10 :y 10 :facing :wall}})
;; => {:position {:x 10, :y 10, :facing :wall}}


;;; Activity 3.01: Building a Distance and Cost Calculator
;;  Back to flight Booking - pg 118


;; #1
(def walking-speed 4)
(def driving-speed 70)

;; #2
(def paris {:lat 48.856483 :lon 2.352413})
(def bordeaux {:lat 44.834999 :lon -0.575490})
(def london {:lat 51.507351, :lon -0.127758})
(def manchester {:lat 53.480759, :lon -2.242631})

;; #6
(def vehicle-cost-fns
  {:sporche (partial * 0.12 1.3)
   :tayato (partial * 0.07 1.3)
   :sleta (partial * 0.2 0.1)})

;; #3
(defn distance
  "Calculate the distance between to coordinate points"
  [{lat1 :lat lon1 :lon} {lat2 :lat lon2 :lon}]
  (let [x-dis (- lat2 lat1)
        y-dis (* (Math/cos lat2) (- lon2 lon1))]
    (* 110.25 (Math/sqrt (+ (* y-dis y-dis) (* x-dis x-dis))))))

;; #4
(defmulti itinerary
  "Calculate the distance of travel between two location, and the cost and
  duration based on the type of transportation"
  :transport)

;; #5
(defmethod itinerary :walking
  [{:keys [:from :to]}]
  (let [walking-distance (distance from to)
        duration (/ walking-distance walking-speed)]
    {:cost 0 :distance walking-distance :duration duration}))

;; #7
(defmethod itinerary :driving
  [{:keys [:from :to :vehicle]}]
  (let [driving-distance (distance from to)
        cost ((vehicle vehicle-cost-fns) driving-distance)
        duration (/ driving-distance driving-speed)]
    {:cost cost :distance driving-distance :duration duration}))


(itinerary {:from paris :to bordeaux :transport :walking})
;; => {:cost 0, :distance 491.61380776549225, :duration 122.90345194137306}
(itinerary {:from paris :to bordeaux :transport :driving :vehicle :tayato})
;; => {:cost 44.7368565066598, :distance 491.61380776549225, :duration 7.023054396649889}
(itinerary {:from london :to manchester :transport :walking})
;; => {:cost 0, :distance 318.4448148814284, :duration 79.6112037203571}
(itinerary {:from manchester :to london :transport :driving :vehicle :sleta})
;; => {:cost 4.604730845743489, :distance 230.2365422871744, :duration 3.2890934612453484}
