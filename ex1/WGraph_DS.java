package ex1;


import java.util.*;

public class WGraph_DS implements weighted_graph {
    private class NodeInfo implements node_info {
        private int key;
        private String info;
        private double tag;
        private int personalDateTagForReset;


        private NodeInfo() {
            this.key = countKey++;
            this.info = 'v' + String.valueOf(this.key);
            this.tag = -1;
        }

        private NodeInfo(String info) {
            this.key = countKey++;
            this.info = info;
            this.tag = -1;
        }

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

        @Override
        public double getTag() {
            if (personalDateTagForReset < resetTag) this.tag = -1;
            return this.tag;
        }


        @Override
        public void setTag(double t) {
            this.tag = t;
            personalDateTagForReset = resetTag;
        }

        public String toString() {

            StringBuilder str=new StringBuilder();
            str.append("key: "+ String.valueOf(this.getKey()) +"    info: "+this.getInfo()+ "  tag: " +this.getTag()+"\n");
            if (neighbors_weight.containsKey(this.getKey())) str.append("edge: "+neighbors_weight.get(this.getKey()));

            return String.valueOf(str);
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
        if (this.myGraph.get(key) == null) return null;
        return this.myGraph.get(key);
    }

    @Override
    public boolean hasEdge(int node1, int node2) {
        if (neighbors_weight.get(node1) == null || neighbors_weight.get(node2) == null) return false;
        return neighbors_weight.get(node1).containsKey(node2);
    }

    @Override
    public double getEdge(int node1, int node2) {
        if (!hasEdge(node1, node2)) return -1;
        return neighbors_weight.get(node1).get(node2);
    }

    @Override
    public void addNode(int key) {
        if (this.myGraph.containsKey(key)) return;
        HashMap<Integer, Double> neiW = new HashMap<Integer, Double>();
        neighbors_weight.put(key, neiW);
        NodeInfo newNode = new NodeInfo(key);
        this.myGraph.put(newNode.getKey(), newNode);
        nodeSize++;
        MC++;
    }

    @Override
    public void connect(int node1, int node2, double w) {
        if (w < 0) return;
        if (!this.myGraph.containsKey(node1) || !this.myGraph.containsKey(node2)) return;
        if (node1 == node2) return;
        if (!hasEdge(node1, node2)) {
//            neighbors.get(node1).put(node2, myGraph.get(node2));
//            neighbors.get(node2).put(node1, myGraph.get(node1));
            edgeSize++;
            MC++;
        } else if (this.getEdge(node1, node2) == w) return;
        neighbors_weight.get(node1).put(node2, w);
        neighbors_weight.get(node2).put(node1, w);

    }

    @Override
    public Collection<node_info> getV() {
        return this.myGraph.values();
    }

    @Override
    public Collection<node_info> getV(int node_id) {
       Set<Integer>  setNei=neighbors_weight.get(node_id).keySet();
        HashMap<Integer,node_info> neighbors=new HashMap<Integer,node_info>();
        for (Integer i:setNei) {
           neighbors.put(i,this.getNode(i));
        }


       return neighbors.values();



    }

    @Override
    public node_info removeNode(int key) {
        if (!this.myGraph.containsKey(key)) return null;
        node_info deleteNode = getNode(key);
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

    public void setMC(Integer MC) {
        this.MC = MC;
    }


    /**
     * A static function resets the Tag for all nodes.
     * The way the function works is:
     *   Increase the resetTag and now the tag of all vertices is in place
     *   personalResetTag <resetTag
     *   And so we will know that they were updated before "resetTag" was updated
     */
    public static void reset() {
        resetTag++;
    }

    public String toString() {
        StringBuilder str=new StringBuilder();
        for (node_info node:this.getV()) {
            str.append(node+"\n");
        }
        str.append("nodSize ="+this.nodeSize+"\n");
        str.append("edgSize ="+this.edgeSize()+"\n");
        str.append("MC ="+this.getMC());
        return String.valueOf(str);
    }
}