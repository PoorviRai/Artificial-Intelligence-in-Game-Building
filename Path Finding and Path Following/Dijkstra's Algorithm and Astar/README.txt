This folder contains the java and class files for Dijkstra's Agorithm and A* as well as the screenshots of the program.

There are two classes used for the implemetation of this program: Dijkstra and Astar.

Dijkstra contains the functions of findPath(), AddChildren(), getEdgeWeight(), getChildren(), lowestCsf(), getCsf() and getPath().
Astar is constructed similarly with exceptions of lowestEtc() and getEtc() functions instead of lowestCsf() and getCsf(), and also the addition of the function euclidean() for computing the heuristic.

Both classes print out their fill and fringe size as well as their total run time.