# OOP--EX2
OOP Exercise 2 Project 

What's in the project?

Part 1: Implementation of Directed Weighted Graph as defined at the Graph Theory

Interfaces : 

node_data – This interface represents a single vertex in a graph with a unique id. The node can be tagged with an int and contain data about its weight and geometric location. The node also can store information in it (in a String). 

Edge_data – This interface represents a directed weighted edge between two nodes. 

directed_weighted_graph – This interface represents a Directed Weighted Graph as defined at Graph Theory. In this kind of graph each edge has a direction and weight (which can represent length, cost etc.). This interface allow creating a graph object, adding nodes, connecting between nodes with edges, changing the weight of edges,removing nodes and edges, and gives access to to the number of edges, nodes and the changes in the graph. 

dw_graph_algorithms – This interface represents a set of algorithms on directed weighted graph:

1.isConnected – checking strong connectivity of the graph (graph is strongly connected if and only if there is a valid path from each node to every node in the graph.

2.ShortestPath and ShortestPathDist – calculating the shortest route and its weight between two nodes in the graph – if the path exists.

The interface also contains methods for loading and saving a graph from and to a file (as a JSON formatted String).

Geo_location – This interface represents a geometric location at a 3 dimensional space.

Implmentatios: 

nodeDate – Implementation of node_data interface. In this class the id represented by a key as int, the tag represented by int, the weight by a double and the info by a String. There are setters and getters for the tag, weight and the info, and for the key there is only a getter.

DW_Graph – Implementation of  directed_weighted_graph. Every object from this class has a HashMap for storing the nodes by their keys, a HashMap of HashMaps for each node's edges (edges that getting out from this node), and a HashMap that stores List for each node for the edges that going into the node. This implementation allows adding nodes and conneting between nodes with complexity of O(1), deleting an edge with complexity of O(1) and deleting a node with complexity of O(k) while k is the number of edges that going in and out from the node. 

DWGraph_Algo – Implementaion of dw_graph_algorithms. Every object form this class can is created and then need to initialize a graph. The class allows getting the weight of shortest path between two nodes, getting a list with the nodes in the shortest path between two nodes (both of methods use Dijkstra's algorithm, checking if the graph is strongly connected (using Kosaraju's algorithm) and save and load a graph to and from a JSON formatted String.

geolocation – implementation of of geo_location interface. Every Object from this class has x value, y value and z value. This class has a method for calculating distance between two points (locations).

How to use: 

For creating a node using the interface and the implementation, declare:
node_data *object name* = new NodeDate();

For creating a graph using the interface and the implementation, declare: 
directed_weighted_graph *object name* =  new DW_Graph ();

For adding a node to the graph, you need to get the key of the node you want to add:
Int key = *nodeObject*.getKey();
Now declare:
*graphObject*.addNode(key);

For deleting the node, declare:
*graphObject*.removeNode(key);

For connecting two nodes with an edge declare (key1 is the id of first node, key2 is the id of the second node,w is a positive double for the weight):
*graphObject*.connect(key1,key2,w);

For deleting the edge, declare:
*graphObject*.removeEdge(key1,key2);

For using algorithms on a graph, first create DWGraph_Algo object:
Dw_graph_algorithms  *objectName* = new DWGraph_Algo();
Than init the graph (suppose the name of the graph object is g):
*objectName*.init(g);

For calculating the weight of the shortest path between node1 and node2:
*objectName*.ShortestPathDist(key1,key2);

For getting a List of node_data with the route of the shortest path between node1 and node2:
*objectName*.ShortestPath(key,key2);

For saving the graph to a file:
*objectName*.save("filename");

For loading a graph from a file(the loaded graph will be initted):
*objectName*.load("filename")

Part 2:

A simulation of Pokemon vs. Agents game.

This simulation running a game of agents running and catching pokemons. The simulation uses the implementation of the directed weighted graph and algorithms in Part 1 of the project – the class Ex2 runs a thread for initializing a scenario (1 from 24 scenarios) from a 'server' jar file at the libs package, using a graph for analysis and staging the pokemons in their exact position. Every scenario has a number of agents allowed in the level, so as part of initialization of the scenario agents are added to the game.
The goal of the game: getting as much as points as possible in the given time. Every single pokemon has its own value (number of points if an agent catch it), a pokemon is caught if an agent went on the edge which the pokemon stands on.

While the game, the simulation runs an efficient algorithm for navigating the agents towards the pokemons aiming for getting as much as points as possible while doing the least steps as possible. 

The simulation has also a GUI for showing the game process. 
How to use? 

1. Clone this repository to your computer using Git Bash

2. Install Java on your computer 

3. Open your Terminal/CMD in the directory which the repository was downloaded to, type the following command : java -jar Ex2.jar *scenario number* *id*

For example, if your id is 123456789, to start a game on scenario 1, type:

java -jar Ex2.jar 1 123456789 

