package ex0;

import java.util.*;

public class Graph_DS implements graph {
    private HashMap<Integer, node_data> myGraph;
    private Integer nodeSize;
    private Integer edgeSize;
    private Integer MC;


    public Graph_DS() {
        this.myGraph = new HashMap<Integer, node_data>();
        this.nodeSize = 0;
        this.edgeSize = 0;
        this.MC = 0;
    }

    @Override
    public node_data getNode(int key) {                         //     O(1)    - Expenditure in hash table =  O(1)
        return this.myGraph.get(key);
    }

    /**
     * In case both nodes are in the graph
     * and node2 is in the node collection of node1
     * we will return true
     *
     * @param node1
     * @param node2
     * @return
     */

    @Override
    public boolean hasEdge(int node1, int node2) {   //O(1) - Search and insert in hashtable = O(1)
        if (!myGraph.containsKey(node1) || !myGraph.containsKey(node2))
            return false;
        return myGraph.get(node1).getNi().contains(myGraph.get(node2));
    }

    @Override
    public void addNode(node_data n) {        //O(1)  -  Add in hash table =  O(1)
        myGraph.put(n.getKey(), n);
        this.nodeSize++;
        MC++;
    }

    /**
     * In case both "t" and this are different from null
     * And yet t is not in the neighbors' collection of "this":
     * Adds t to neighbors list of "this"
     */
    @Override
    public void connect(int node1, int node2) {    //O(1)
        if (hasEdge(node1, node2))
            return;
        if (node1 == node2)        //The graph simply has no loops
            return;
        if (!this.myGraph.containsKey(node1) || !this.myGraph.containsKey(node2))
            return;
        this.myGraph.get(node1).addNi(this.myGraph.get(node2));
        this.edgeSize++;
        MC++;
    }

    /**
     * Returns a Collection view of the values contained in this graph.
     *
     * @return
     */
    @Override
    public Collection<node_data> getV() {    //O(1)
        return this.myGraph.values();
    }

    /**
     * Returns a collection view of the table with node(id) neighbors.
     */
    @Override
    public Collection<node_data> getV(int node_id) {  //O(1)
        return this.myGraph.get(node_id).getNi();
    }



    /**
     * In case the graph is not null
     * And the node is in the graph:
     * We will save the neighbors' keys of the current node with a queue
     * so we can remove it from their neighbors collection
     * Then we will remove the current node from the neighbors collection
     * of each of the nodes we saved in the queue
     * And then we will remove it from the graph
     * And finally the function will return the node to the user
     * @param key
     * @return
     */
    @Override
    public node_data removeNode(int key) {           //Total complexity of the function O(n) when n = this.Neighbors.size
        if (this.myGraph == null) return null;
        if (!this.myGraph.containsKey(key)) return null;
        node_data ans = this.myGraph.get(key);
        int NumberOfEdgeDeleted = getNode(key).getNi().size();
        MC += NumberOfEdgeDeleted;
        edgeSize -= NumberOfEdgeDeleted;
        Queue<Integer> q = new LinkedList<>();
        for (node_data node : this.myGraph.get(key).getNi()) {  // O(n)
            q.add(node.getKey());
        }
        while (!q.isEmpty()) {     // O(n)
            getNode(key).removeNode(this.myGraph.get(q.poll()));
        }
        this.myGraph.remove(key);
        this.nodeSize--;
        MC++;
        return ans;
    }


    /**
     * In case the two nodes are in the graph and are also neighbors:
     * Activates the function from the NodeData class that removes a edge between the two nodes
     * @param node1
     * @param node2
     */
    @Override
    public void removeEdge(int node1, int node2) {   //O(1)
        if (!this.myGraph.containsKey(node1) || !this.myGraph.containsKey(node2))
            return;
        if (!this.getNode(node1).getNi().contains(this.getNode(node2)))
            return;
        myGraph.get(node1).removeNode(this.getNode(node2));
        edgeSize--;
        MC++;
    }

    @Override
    public int nodeSize() {
        return this.nodeSize;
    }

    @Override
    public int edgeSize() {
        return this.edgeSize;
    }

    @Override
    public int getMC() {
        return MC;
    }

    public String toString() {
        return this.getV().toString();
    }

    public void setMC(int MC) {
        this.MC = MC;
    }


    //*******************    For me. Helps me with tests  ************************

    /**
     * Prints all the data of the graph
     */
    public void printALL() {
        for (node_data node : this.getV()) {
            if (node.getNi().size() == 0) System.out.println(node);
            else System.out.println(node + "  Neighbors: " + this.getV(node.getKey()));
        }
        System.out.println("node size = " + this.nodeSize);
        System.out.println("edge size = " + this.edgeSize);
        System.out.println("MC  = " + this.MC);
    }


}