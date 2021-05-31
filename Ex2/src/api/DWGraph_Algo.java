
package api;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.Map.Entry;
import java.io.*;
import java.util.*;

/**
 * This class represents set of algorithms on a directed weighted graph, and some important actions on graph,including:
 * 0.clone() (create copy of a graph)
 * 1.init (graph)
 * 2.isConnected (checking strongly connectivity of a graph)
 * 3. double shortestPathDist(int src, int dest);
 * 4. List<node_data> shortestPath(int src, int dest);
 * 5. Save(file); // save graph as JSON file
 * 6. Load(file); // load graph from aJSON file
 *
 * @author Gilad Shotland
 * @author Shlomo GLick
 */


public class DWGraph_Algo implements dw_graph_algorithms, Comparator<node_data> {
    //variables we need to erase - wait for Shlomo confirmation
    final double EPS = 0.00001;
    private int connectedSize = 0;
    /********** Initialization Variable **********/
    private directed_weighted_graph myWGraphAlgo;//The graph which the algorithms and actions is applied on


    /*********** Getter ***********/

    /**
     * This method return the underlying graph of which this class works
     * This method is useful for working with the 'load' method
     * @return directed_weighted_graph
     */
    @Override
    public directed_weighted_graph getGraph() {
        return this.myWGraphAlgo;
    }

    /*********Constructor and Initializing Method*********/

    /**
     * Constructor
     */
    public DWGraph_Algo() {
        this.myWGraphAlgo = new DWGraph_DS();
    }

    /**
     * Init the graph on which this set of algorithms operates on.
     *
     * @param g is the graph which the class will operate on
     */
    @Override
    public void init(directed_weighted_graph g) {
        this.myWGraphAlgo = g;
    }


    /**
     * This method compute and construct a deep copy of this weighted graph.
     *
     * @return directed_weighted_graph
     */
    @Override
    public directed_weighted_graph copy() {
        if (this.getGraph() == null) return null;
        DWGraph_DS g = new DWGraph_DS(); //new graph object
        for (node_data oldNode : this.getGraph().getV()) {//copying nodes
            int key = oldNode.getKey();
            g.addNode(oldNode);
            g.getNode(key).setInfo(oldNode.getInfo());
            g.getNode(key).setTag(oldNode.getTag());
        }
        for (node_data oldNode : this.getGraph().getV()) {
            int key=oldNode.getKey();
            for (edge_data edgeOfOld : this.getGraph().getE(key)) {
                int dest = edgeOfOld.getDest();
                g.connect(edgeOfOld.getSrc(), dest, edgeOfOld.getWeight());
                g.getEdge(key, dest).setInfo(edgeOfOld.getInfo());
                g.getEdge(key, dest).setTag(edgeOfOld.getTag());
            }
        }
        g.setMC(this.getGraph().getMC());
        return g;
    }











    /**
     * This method saves the graph to a given file name in Json format
     * @param file - the file name (may include a relative path).
     * @return true if and only if the file was successfuly saved
     */
    @Override
    public boolean save(String file)  {
        try {
            Gson gson = new Gson();
            JsonObject obj = new JsonObject();
            JsonArray edgesArray = new JsonArray();
            JsonArray nodesArray = new JsonArray();
            Collection<node_data> vertices = this.getGraph().getV();
            for (node_data node : vertices) {
                Collection<edge_data> edges = this.getGraph().getE(node.getKey()); //adding all the edges
                for (edge_data edge : edges) {
                    JsonObject tempedge = new JsonObject();
                    tempedge.add("src", gson.toJsonTree(edge.getSrc()));
                    tempedge.add("w", gson.toJsonTree(edge.getWeight()));
                    tempedge.add("dest", gson.toJsonTree(edge.getDest()));
                    edgesArray.add(tempedge);
                }
                JsonObject tempnode = new JsonObject();  //adding the node
                geo_location location = node.getLocation();
                String locate = location.x() + "," + location.y() + "," + location.z();
                tempnode.add("pos", gson.toJsonTree(locate));
                tempnode.add("id", gson.toJsonTree(node.getKey()));
                nodesArray.add(tempnode);
            }

            obj.add("Edges", edgesArray);
            obj.add("Nodes", nodesArray);

            PrintWriter pw = new PrintWriter(new File(file));
            pw.println(obj.toString());
            pw.close();
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }


    /**
     * This method load a graph to this graph algorithm.
     * if the file was successfully loaded - the underlying graph
     * of this class will be changed (to the loaded one), in case the
     * graph was not loaded the original graph should remain "as is".
     * @param file - file name of JSON file
     * @return true - iff the graph was successfully loaded.
     */
    @Override
    public boolean load(String file) {
        try {
            GsonBuilder builder = new GsonBuilder();
            builder.registerTypeAdapter(directed_weighted_graph.class, new dwJsonDeserializer());
            Gson gson = builder.create();
            FileReader reader = new FileReader(file);
            directed_weighted_graph ans = gson.fromJson(reader, directed_weighted_graph.class);
            init(ans);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }




    /********* Graph Theory Algorithms *********/
    /**
     * This method checks if there is a valid path from each node to each other node
     *
     * @return true if and only if there are valid paths as explained
     * see also: https://en.wikipedia.org/wiki/Strongly_connected_component (Wikipedia on Strong Connectivity in graph theory)
     * This method uses Kosaraju's Algorithm for checking Strong Connectivity of a graph.
     */
    @Override
    public boolean isConnected() {
        if (this.getGraph().nodeSize() == 0 || this.getGraph().nodeSize() == 1) return true;
        Iterator<node_data> iterator = this.getGraph().getV().iterator();
        nodeDate firstNode = (nodeDate) iterator.next();
        return kosarajusAlgo(firstNode); //apply Kosaraju's Algorithm
        //      int firstKey = firstNode.getKey();
//        boolean ans = kosarajusAlgo(firstNode); //apply the algorithm - stage one
//        if (ans == false) return ans;
//        DWGraph_DS graph = (DWGraph_DS) this.getGraph();
//        DWGraph_DS reversgraph = reversGraph(graph);
//        this.init(reversgraph);//apply the algorithm - stage two
//        ans = (conecct(firstKey));
//        if (ans == false) return false;
//        this.init(graph);
//        return true;
    }

    /**
     * This method is an implementation for Kosaraju's Algorithm for checking strong connectivity of a graph
     * The algorithm has two steps:
     * Step 1:The algorithm checks for one node if there is a valid path from it to all the other node
     * Step 2:The algorithm checks for the same node if there is a valid path from it to all the other node at the reveresed version of the graph
     * If both of the steps were completed successfully - the graph is strongly connected
     * This implementation uses the Dijkstra's Algorithm for finding shortest path between two nodes - just for making sure if there is a valid path
     * @param source is the node the implementation is going to use
     * @return true if and only if the graph is strongly connected
     * see also: https://en.wikipedia.org/wiki/Kosaraju%27s_algorithm
     */
    private boolean kosarajusAlgo(node_data source) {
        for (node_data node : getGraph().getV()) {//Step 1
            if (shortestPathDist(source.getKey(), node.getKey()) < 0) { //checking if there is a valid path
                return false;
            }
        }
        directed_weighted_graph saveit = getGraph();
        directed_weighted_graph reversedGraph = reversGraph(getGraph()); //compute a reversed graph for Step 2
        dw_graph_algorithms algo = new DWGraph_Algo();
        algo.init(reversedGraph);
        for (node_data node : reversedGraph.getV()) {//Step 2
            if (algo.shortestPathDist(source.getKey(), node.getKey()) < 0) { //checking if there is a valid path
                return false;
            }
        }
        init(saveit); //make sure working on the original graph
        return true;
    }

    /**
     * this method computes a reversed graph for a graph
     * reversed graph of a graph has exactly the same nodes, but the edges were flipped.
     * This is a helping method for Kosaraju's Algorithm implementation
     * @param graph
     * @return directed_weighted_graph
     */
    private DWGraph_DS reversGraph(directed_weighted_graph graph) {
        DWGraph_DS graphRevers = new DWGraph_DS();
        for (node_data node : graph.getV()) {
            graphRevers.addNode(node);
        }
        for (node_data node : graph.getV()) {
            for (edge_data edge : graph.getE(node.getKey())) {//flip all the edges

                int src = edge.getDest();
                int dest = edge.getSrc();
                double w = edge.getWeight();
                graphRevers.connect(src, dest, w);
            }
        }
        return graphRevers;
    }



    /**
     * This method computes and return the length of the shortest path between src to dest
     * Note: if no such path --> returns -1
     * @param src - start node
     * @param dest - end (target) node
     * @return double
     */
    @Override
    public double shortestPathDist(int src, int dest) {
        if (this.getGraph().getNode(src) == null || this.getGraph().getNode(dest) == null) return -1;
        if (dest == src) return 0;
        //initWeight(src);
        dijkstrasAlgo(getGraph().getNode(src));//Apply Dijkstra's Algorithm - it updates the weight of the nodes.
        double ans = this.getGraph().getNode(dest).getWeight();//Check if there is a valid path.
        nodeDate.reset();//Equate to zero all the nodes' weights
        return ans;
    }

    /**
     * This method computes and return a List of node_data represents the shortest path
     * between two nodes
     * @param src - start node of the traversal
     * @param dest - end (target) node
     * @return List<node_data>
     */
    @Override
    public List<node_data> shortestPath(int src, int dest) {
        if (this.getGraph() == null) {
            return null;
        }
        if (getGraph().getNode(src) != null && getGraph().getNode(dest) != null) {
            //this implementation of dijkstra's algorithm return a HashMap with all the different fathers in the graph
            HashMap<Integer, node_data> predecessor = dijkstrasAlgo(getGraph().getNode(src));
            //the method starts from the destination node, so we need to reverse the path we're getting - stack is useful for that
            Stack<node_data> temp = new Stack();
            node_data node = getGraph().getNode(dest);
            temp.add(node);
            while ( node.getKey() != src) {
                //go to the father of this node
                node = predecessor.get(node.getKey());
                if(node == null){return null;}
                temp.add(node);
            }
            if(temp.isEmpty()){return null;}
            List<node_data> ans = new LinkedList<node_data>();
            while (!temp.isEmpty()) {
                ans.add(temp.pop());
            }
            return ans;

        }
        return null;
    }

    private void getPath(int src, int dest, Stack<Integer> stack) {
        stack.add(dest);
        if (dest != src) getPath(src, this.getGraph().getNode(dest).getTag(), stack);
        return;
    }

    private HashMap<Integer, node_data> dijkstrasAlgo(node_data source) {
        HashMap<Integer, Boolean> visited = new HashMap<>();
        for (node_data node : getGraph().getV()) { //mark all the nodes as not visited
            visited.put(node.getKey(), false);
            node.setWeight(-1);
        }
        HashMap<Integer, node_data> predecessors = new HashMap<>(); //hashmap for every node's predecessor
        PriorityQueue<node_data> queue = new PriorityQueue<>(new nodeCompare());
        queue.add(source);
        source.setWeight(0);
        while (!queue.isEmpty()) {
            node_data current = queue.poll();
            if (!visited.get(current.getKey())) {
                for (edge_data edge : getGraph().getE(current.getKey())) {
                    node_data neighbor = getGraph().getNode(edge.getDest());
                    double dist = current.getWeight() + edge.getWeight();
                    if (neighbor.getWeight() == -1 || dist < neighbor.getWeight()) { //this is the first time the node is visited or shorter path
                        neighbor.setWeight(dist);
                        predecessors.put(neighbor.getKey(), current);
                        queue.add(neighbor);
                    }

                }
                visited.put(current.getKey(), true);
            }

        }
        return predecessors;
    }


    @Override
    public int compare(node_data n1, node_data n2) {
        if (n1.getWeight() < n2.getWeight()) return -1;
        if (n1.getWeight() > n2.getWeight()) return 1;
        return 0;
    }


    /**
     * This class is a comparing class of two nodes.
     */
    private class nodeCompare implements Comparator<node_data> {

        @Override
        public int compare(node_data o1, node_data o2) {
            if (o1.getWeight() > o2.getWeight()) {
                return 1;
            }
            if (o1.getWeight() < o2.getWeight()) {
                return -1;
            } else return 0;
        }
    }


}


