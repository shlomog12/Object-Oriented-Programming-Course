package api;
/**
 * This Class represents a directional weighted graph , and implements the directed_weighted_graph interface
 * Every graph includes nodes and edges between nodes.
 * Every edge is directed - which means that there is only way from the source to the destnation of the edge
 *
 * @Author Shlomo Glick
 * @Author Gilad Shotland
 */

import java.util.*;

public class DWGraph_DS implements directed_weighted_graph {

    /****** Static variable for identification of the nodes in the graph - no two nodes with same key*****/
    private static int countKey;
    /******initalization variables******/
    private HashMap<Integer, node_data> myGraph; //HashMap for saving all the nodes in the graph
    private Integer nodesize; //check with shlomo - not necessary
    private Integer edgesize; //counter of edges - for O(1) complexity for getting the number of edges in the graph
    private Integer MC; //Mode counter of the graph - counts changes in the graph
    private HashMap<Integer, LinkedList<Integer>> upside;//HashMap of LinkedLists for every node's neighbor that an edge getting out of it streight to the node - for good Complexity of removing nodes from the graph
    private HashMap<Integer, HashMap<Integer, edge_data>> neighbors_weight;//HashMap of Hashmaps for every node's neighbors that the node has an edge from it to the neighbors

    /*********Constructor***********/
    public DWGraph_DS() {
        this.myGraph = new HashMap<Integer, node_data>();
        this.nodesize = 0;
        this.edgesize = 0;
        this.MC = 0;
        this.neighbors_weight = new HashMap<Integer, HashMap<Integer, edge_data>>();
        this.upside=new HashMap<Integer, LinkedList<Integer>>();
    }

    /**********Getters**********/


    /**
     * This method returns the node_data by the node_id,
     * @param key - the node_id
     * @return the node_data by the node_id, null if none.
     */
    @Override
    public node_data getNode(int key) {//Total Complexity - O(1)
        return this.myGraph.get(key);//O(1)
    }

    /**
     * This method returns the data of the edge (src,dest), null if none.
     * @param src
     * @param dest
     * @return the edge_data represents the edge, null if none
     */
    @Override
    public edge_data getEdge(int src, int dest) { //Total Complexity - O(1)
        if (!this.myGraph.containsKey(src) || !this.myGraph.containsKey(dest)) return null;
        if (!this.neighbors_weight.containsKey(src)) return null;
        if (!this.neighbors_weight.get(src).containsKey(dest)) return null;
        return this.neighbors_weight.get(src).get(dest); //O(1)
    }



    /**
     * This method returns a pointer (shallow copy) for the
     * collection representing all the nodes in the graph.
     * @return Collection<node_data>
     */
    @Override
    public Collection<node_data> getV() {//Total Complexity - O(1)
        return this.myGraph.values();//O(1)
    }
    /**
     * This method returns a pointer (shallow copy) for the
     * collection representing all the edges getting out of
     * the given node (all the edges starting (source) at the given node).
     * @return Collection<edge_data>
     */
    @Override
    public Collection<edge_data> getE(int node_id) {//Total Complexity - O(1)
        /*
        Set<Integer> setNei = neighbors_weight.get(node_id).keySet();
        Collection<edge_data> neighbors=new LinkedList<>();
        for (Integer i : setNei) {
            neighbors.add(this.neighbors_weight.get(node_id).get(i));
        }
         */


        return neighbors_weight.get(node_id).values(); //O(1)
    }
    /** This method returns the number of vertices (nodes) in the graph.
     * @return the number of nodes in the graph
     */
    @Override
    public int nodeSize() {//Total Complexity - O(1)
        return this.nodesize;//O(1)
    }

    /**
     * This method returns the number of edges (assume directional graph).
     * @return the number of the edges in the graph
     */
    @Override
    public int edgeSize() {//Total Complexity - O(1)

        return this.edgesize;//O(1)
    }
    /**
     * This method returns the Mode Count - for testing changes in the graph.
     * @return the mode count of the graph
     */
    @Override
    public int getMC() {
        return this.MC;
    }

    public void setMC(int MC) {
        this.MC = MC;
    }


    /**
     * This method adds a new node to the graph with the given node_data.
     * @param n is the new node for the graph
     */
    @Override
    public void addNode(node_data n) {//Total Complexity - O(1)
        if (this.myGraph.containsKey(n.getKey())) return;
        HashMap<Integer, edge_data> neiW = new HashMap<Integer, edge_data>();//initializing new map -  O(1)
        neighbors_weight.put(n.getKey(), neiW);//O(1)
        this.upside.put(n.getKey(),new LinkedList<Integer>());//O(1)
        nodesize++;//O(1)
        MC++;//O(1)
        this.myGraph.put(n.getKey(), n);//O(1)
    }

    /**
     * This method connects an edge with weight w between node src to node dest.
     * @param src - the source of the edge.
     * @param dest - the destination of the edge.
     * @param w - positive weight representing the cost (aka time, price, etc) between src-->dest.
     */
    @Override
    public void connect(int src, int dest, double w) { //Total Complexity - O(1)
        if (w < 0) return;
        if (!this.myGraph.containsKey(src) || !this.myGraph.containsKey(dest)) return;
        if (src == dest) return;
        if (this.getEdge(src, dest) == null) {
            edgesize++;//O(1)
            upside.get(dest).add(src);//O(1)
        } else if (this.getEdge(src, dest).getWeight() == w) return;
        edgeData newEdge = new edgeData(src, dest, w);//initializing edge - O(1)
        this.neighbors_weight.get(src).put(dest, newEdge); //O(1)
        MC++;//O(1)
    }


    /**
     * Deletes the node (with the given ID) from the graph -
     * and removes all edges which starts or ends at this node.
     * This method should run in O(k), V.degree=k, as all the edges should be removed.
     * @param key is the id of the node
     * @return the data of the removed node (null if none).
     */
    @Override
    public node_data removeNode(int key) {//Define k as number of neighbors of the node. Total Complexity - O(n+m) => O(k)
        if (!this.myGraph.containsKey(key)) return null;
        node_data deleteNode = this.getNode(key);
        Queue<Integer> q = new LinkedList<Integer>();
        //Define n as number of edges that go out from the node, m number of edges that go inside the node. m+n = k
        for (edge_data edge : this.getE(key)) {//O(n)
            q.add(edge.getDest());
        }
        while (!q.isEmpty()) {//O(n)
            removeEdge(key, q.poll());
        }
        for (Integer i: this.upside.get(key)) {//O(m)
            removeEdge(i,key);
        }
        this.upside.remove(key);//O(1)

        this.neighbors_weight.remove(key);//O(1)
        this.myGraph.remove(key);//O(1)
        this.nodesize--;//O(1)
        this.MC++;//O(1)
        return deleteNode;
    }
    /**
     *This method deletes the edge from the graph,
     * @param src
     * @param dest
     * @return the data of the removed edge (null if none).
     */
    @Override
    public edge_data removeEdge(int src, int dest) {
        if (this.getEdge(src, dest) == null) return null;
        edge_data deleteEdge = this.getEdge(src, dest);
        this.neighbors_weight.get(src).remove(dest);
        this.edgesize--;
        this.MC++;
        return deleteEdge;
    }


    /**
     * Equals method for testing the graph
     * @param o another graph for comparing
     * @return true if and only if two of the graphs have the same vertices and edges
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DWGraph_DS that = (DWGraph_DS) o;
        return Objects.equals(myGraph, that.myGraph) &&
                Objects.equals(nodesize, that.nodesize) &&
                Objects.equals(edgesize, that.edgesize) &&
                Objects.equals(MC, that.MC) &&
                Objects.equals(neighbors_weight, that.neighbors_weight);
    }



}





