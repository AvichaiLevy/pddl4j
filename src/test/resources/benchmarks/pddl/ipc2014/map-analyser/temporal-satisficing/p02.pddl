(define (problem citycar-3-3-4)
(:domain mapanalyzer)
(:objects  
junction0-0 junction0-1 junction0-2 
junction1-0 junction1-1 junction1-2 
junction2-0 junction2-1 junction2-2 - junction
car0 car1 car2 car3 - car
garage0 garage1 - garage
road0 road1 road2 road3 road4 - road
)
(:init
	(=(build-time) 5)
	(=(remove-time) 3)
	(=(arrived-time) 1)
	(=(start-time) 1)
	(=(speed car0) 4)
	(=(speed car1) 15)
	(=(speed car2) 3)
	(=(speed car3) 3)
(available road0)
(available road1)
(available road2)
(available road3)
(available road4)
(connected junction0-0 junction0-1)
(connected junction0-1 junction0-0)
(=(distance junction0-0 junction0-1) 84)
(=(distance junction0-1 junction0-0) 84)
(connected junction0-1 junction0-2)
(connected junction0-2 junction0-1)
(=(distance junction0-1 junction0-2) 50)
(=(distance junction0-2 junction0-1) 50)
(connected junction1-0 junction1-1)
(connected junction1-1 junction1-0)
(=(distance junction1-0 junction1-1) 11)
(=(distance junction1-1 junction1-0) 11)
(connected junction1-1 junction1-2)
(connected junction1-2 junction1-1)
(=(distance junction1-1 junction1-2) 51)
(=(distance junction1-2 junction1-1) 51)
(connected junction2-0 junction2-1)
(connected junction2-1 junction2-0)
(=(distance junction2-0 junction2-1) 2)
(=(distance junction2-1 junction2-0) 2)
(connected junction2-1 junction2-2)
(connected junction2-2 junction2-1)
(=(distance junction2-1 junction2-2) 85)
(=(distance junction2-2 junction2-1) 85)
(connected junction0-0 junction1-0)
(connected junction1-0 junction0-0)
(=(distance junction0-0 junction1-0) 24)
(=(distance junction1-0 junction0-0) 24)
(connected junction1-0 junction2-0)
(connected junction2-0 junction1-0)
(=(distance junction1-0 junction2-0) 65)
(=(distance junction2-0 junction1-0) 65)
(connected junction0-1 junction1-1)
(connected junction1-1 junction0-1)
(=(distance junction0-1 junction1-1) 99)
(=(distance junction1-1 junction0-1) 99)
(connected junction1-1 junction2-1)
(connected junction2-1 junction1-1)
(=(distance junction1-1 junction2-1) 4)
(=(distance junction2-1 junction1-1) 4)
(connected junction0-2 junction1-2)
(connected junction1-2 junction0-2)
(=(distance junction0-2 junction1-2) 41)
(=(distance junction1-2 junction0-2) 41)
(connected junction1-2 junction2-2)
(connected junction2-2 junction1-2)
(=(distance junction1-2 junction2-2) 75)
(=(distance junction2-2 junction1-2) 75)
(clear junction0-0)
(clear junction0-1)
(clear junction0-2)
(clear junction1-0)
(clear junction1-1)
(clear junction1-2)
(clear junction2-0)
(clear junction2-1)
(clear junction2-2)
(at_garage garage0 junction0-1)
(at_garage garage1 junction0-2)
(starting car0 garage0)
(starting car1 garage0)
(starting car2 garage1)
(starting car3 garage0)
)
(:goal
(and
(arrived car0 junction2-2)
(arrived car1 junction2-1)
(arrived car2 junction2-0)
(arrived car3 junction2-1)
)
)
(:metric minimize (total-time))
)