package ex1.src;

import java.util.*;

public class WGraph_DS implements weighted_graph {


    private class NodeInfo implements node_info {
        private int key;
        private String info;
        private double tag;
        private int tagUpdateDate;

        /**
         * default constructor
         */
        private NodeInfo() {
            this.key = countKey++;
            this.info = 'v' + String.valueOf(this.key);
            this.tag = -1;
        }

        /**
         *  constructor that receives String info
         *  and creates a vertex whose data is info
         * @param info
         */
        private NodeInfo(String info) {
            this.key = countKey++;
            this.info = info;
            this.tag = -1;
        }

        /**
         * constructor that gets String info and int "key"
         * and creates a vertex whose data is info and whose key is "key"
         * @param key
         * @param info
         */

        private NodeInfo(Integer key, String info) {
            this.key = key;
            this.info = info;
            this.tag = -1;
        }

        private NodeInfo(Integer key) {
            this.key = key;
            this.info = 'v' + String.valueOf(key);
            this.tag = -1;
        }

        @Override
        public int getKey() {
            return this.key;
        }

        @Override
        public String getInfo() {
            return this.info;
        }

        @Override
        public void setInfo(String s) {
            this.info = s;
        }

        /**
         * In case the tag is updated after the reset
         * returns the tag value
         * otherwise resets the tag value to - 1 and returns -1
         */
        @Override
        public double getTag() {
            if (tagUpdateDate < resetTag) this.tag = -1;
            return this.tag;
        }


        /**
         * Updates the tag and update date
         * @param t - the new value of the tag
         */
        @Override
        public void setTag(double t) {
            this.tag = t;
            tagUpdateDate = resetTag;
        }

        /**
         * @return a String that represents the node
         */

        public String toString() {

            StringBuilder str = new StringBuilder();
            str.append("key: ");
            str.append(String.valueOf(this.getKey()));
            str.append("    info: ");
            str.append(this.getInfo());
            str.append("  tag: ");
            str.append(this.getTag());
            str.append("\n");
//            str.append("key: " + String.valueOf(this.getKey()) + "    info: " + this.getInfo() + "  tag: " + this.getTag() + "\n");
//            if (neighbors_weight.containsKey(this.getKey())) str.append("edge: " + neighbors_weight.get(this.getKey()));
            if (neighbors_weight.containsKey(this.getKey())) {
                str.append("edge: ");
                str.append(neighbors_weight.get(this.getKey()));
            }
            return String.valueOf(str + "\n");
        }

        /**
         * Compares two vertices and checks if the key, info, tag are equal
         * @param o
         * @return
         */
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            NodeInfo nodeInfo = (NodeInfo) o;
            return this.getKey() == nodeInfo.getKey() &&
                    Double.compare(nodeInfo.getTag(), this.getTag()) == 0 &&
                    Objects.equals(this.getInfo(), nodeInfo.getInfo());
        }
    }


    private static int countKey;
    private static int resetTag;
    private HashMap<Integer, node_info> myGraph;
    private HashMap<Integer, HashMap<Integer, Double>> neighbors_weight;
    private Integer nodeSize;
    private Integer edgeSize;
    private Integer MC;


    public WGraph_DS() {
        this.myGraph = new HashMap<Integer, node_info>();
        this.neighbors_weight = new HashMap<Integer, HashMap<Integer, Double>>();
        this.nodeSize = 0;
        this.edgeSize = 0;
        this.MC = 0;
    }


    @Override
    public node_info getNode(int key) {
        return this.myGraph.get(key);
    }

    @Override
    public boolean hasEdge(int node1, int node2) {
        if (neighbors_weight.get(node1) == null) return false;
        return neighbors_weight.get(node1).containsKey(node2);
    }

    @Override
    public double getEdge(int node1, int node2) {
        if (node1==node2) return 0;
        if (!hasEdge(node1, node2)) return -1;
        return neighbors_weight.get(node1).get(node2);
    }


    @Override
    public void addNode(int key) {
        if (this.myGraph.containsKey(key)) return;
        HashMap<Integer, Double> neiW = new HashMap<Integer, Double>();
        neighbors_weight.put(key, neiW);
        NodeInfo newNode = new NodeInfo(key);
        this.myGraph.put(key, newNode);
        nodeSize++;
        MC++;
    }


/*
   In case both exist in the graph and the weight is greater than 0:
   If they are not neighbors yet add each of them to the other's neighbors list,
   And if they are neighbors and weight is not updated, updates the weight
*/

    @Override
    public void connect(int node1, int node2, double w) {
        if (w < 0) return;
        if (!this.myGraph.containsKey(node1) || !this.myGraph.containsKey(node2)) return;
        if (node1 == node2) return;
        if (!hasEdge(node1, node2)) edgeSize++;
        else if (this.getEdge(node1, node2) == w) return;
        MC++;
        neighbors_weight.get(node1).put(node2, w);
        neighbors_weight.get(node2).put(node1, w);

    }

    @Override
    public Collection<node_info> getV() {
        return this.myGraph.values();
    }


/*
Creates a collection of neighboring nodes
And goes through all the keys that represent the neighbors of node with the key node id
And adds them to the collection.
Returns the collection of neighbors
 */

    @Override
    public Collection<node_info> getV(int node_id) {

        Set<Integer> setNei = neighbors_weight.get(node_id).keySet();

        Collection<node_info> neighbors=new LinkedList<>();
        for (Integer i : setNei) {
            neighbors.add(this.getNode(i));
        }
        return (Collection<node_info>) neighbors;


//        HashMap<Integer, node_info> neighbors = new HashMap<Integer, node_info>();
//        for (Integer i : setNei) {
//            neighbors.put(i, this.getNode(i));
//        }
//        return neighbors.values();
    }

/*
In case the node is in the graph:
We will keep the neighbors' keys of the current junction with a queue so we can remove the rib in their sons.
Then we will remove the rib between each of the nodes we saved in the queue and the node we want to remove
Then we will remove it from the graph
And finally the function will return to the user the node we removed
 */

    /**
     * If the node is in the graph:
     *   We will remove it from the graph
     * And return to the user the node we removed
     * @param key
     * @return
     */

    @Override
    public node_info removeNode(int key) {
        if (!this.myGraph.containsKey(key)) return null;
        node_info deleteNode =this.getNode(key);
        Queue<Integer> q = new LinkedList<Integer>();
        for (node_info neighbor : getV(key)) {
            q.add(neighbor.getKey());
        }
        while (!q.isEmpty()) {
            removeEdge(key, q.poll());
        }
        this.neighbors_weight.remove(key);
        this.myGraph.remove(key);
        nodeSize--;
        MC++;
        return deleteNode;
    }


    /**
     * In case the two nodes are neighbors:
     * The function removes an edge between the two nodes
     * @param node1
     * @param node2
     */
    @Override
    public void removeEdge(int node1, int node2) {
        if (!hasEdge(node1, node2)) return;
        this.neighbors_weight.get(node1).remove(node2);
        this.neighbors_weight.get(node2).remove(node1);
        edgeSize--;
        MC++;
    }

    @Override
    public int nodeSize() {
        return nodeSize;
    }

    @Override
    public int edgeSize() {
        return edgeSize;
    }

    @Override
    public int getMC() {
        return MC;
    }

    /**
     * Manually updates the MC
     * @param MC
     */
    public void setMC(Integer MC) {
        this.MC = MC;
    }


    /**
     * A static function resets the Tag for all nodes.
     * The way the function works is:
     * Increase the resetTag and now the tag of all vertices is in place
     * tagUpdateDate <resetTag
     * And so we will know that they were updated before "resetTag" was updated
     */

    public static void reset() {
        resetTag++;
    }


    /**
     * @return  a String that represents the graph
     */
    public String toString() {
        StringBuilder str = new StringBuilder();
        for (node_info node : this.getV()) {
            str.append(node);
        }
        str.append("nodSize =" + this.nodeSize + "\n");
        str.append("edgSize =" + this.edgeSize() + "\n");
        str.append("MC =" + this.getMC());
        return String.valueOf(str);
    }


    /**
     * Compares two graphs
     * @param o
     * @return true
     * In case the two graphs have an equal set of vertices between the two graphs,
     * The collection of ribs and their equal weight between the two graphs,
     * The number of sides is equal between the two graphs
     * Equal number of vertices
     * MC is worth it
     */
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WGraph_DS graph_ds = (WGraph_DS) o;
        return Objects.equals(this.myGraph, graph_ds.myGraph) &&
                Objects.equals(this.neighbors_weight, graph_ds.neighbors_weight) &&
                Objects.equals(this.nodeSize, graph_ds.nodeSize) &&
                Objects.equals(this.edgeSize, graph_ds.edgeSize) &&
                Objects.equals(this.MC, graph_ds.MC);
    }
}






