This folder contains the java and class files for Flocking behaviour as well as the screenshots of the program.

There are three classes used for the implemetation of this program: Steering_Variables, CharFlock and Flocking.

Steering_Variables contains the data structure holding all the steering variables.
CharFlock contains all the functions of display(), update(), align(), separate(), cohesion(), velocityMatch() and flock().
Flocking is the main class calling all the functions. 

All the weights to separate, cohesion and velocity Match are assigned the flock() function in CharFlock. 
The implementation currently shows flocking behaviour with two wandering leads.