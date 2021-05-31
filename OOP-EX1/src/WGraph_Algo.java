package ex1.src;

import ex1.src.WGraph_DS;
import ex1.src.node_info;
import ex1.src.weighted_graph;
import ex1.src.weighted_graph_algorithms;

import java.io.*;
import java.util.*;

public class WGraph_Algo implements weighted_graph_algorithms, Comparator<node_info> {
    final double EPS = 0.00001;

    private weighted_graph myWGraphAlgo;
    private int connectedSize = 0;

    public WGraph_Algo() {
        this.myWGraphAlgo  = new WGraph_DS();;
    }



    @Override
    public void init(weighted_graph g) {
        this.myWGraphAlgo = g;
    }



    @Override
    public weighted_graph getGraph() {
        return this.myWGraphAlgo;
    }


    /**
     * Creates a new graph g
     * Runs on all nodes in the old graph
     * And for each node creates a new node with the same data
     * And adds it to the new graph
     * Then passes over all its neighbors from the old graph
     * And adds in the new graph g side between them and the new vertex
     * @return g
     */
    @Override
    public weighted_graph copy() {
        if (this.myWGraphAlgo == null) return null;
        WGraph_DS g = new WGraph_DS();
        for (node_info oldNode : this.myWGraphAlgo.getV()) {
            int key = oldNode.getKey();
            g.addNode(key);
            g.getNode(key).setInfo(oldNode.getInfo());
            g.getNode(key).setTag(oldNode.getTag());
            for (node_info neighborOfOld : this.myWGraphAlgo.getV(key)) {
                int keyNeighborOfOld = neighborOfOld.getKey();
                double edgeWeight = this.myWGraphAlgo.getEdge(key, keyNeighborOfOld);
                g.connect(key, keyNeighborOfOld, edgeWeight);
            }
        }


        g.setMC(this.myWGraphAlgo.getMC());
        return g;
    }

    /**
     * run the initConnectedSizeAndTag function
     * That will update connectedSize
     * To the number of nodes connected to the first node.
     * Then if the graph is connected
     * We will get the number of nodes connected to the first node
     * Equal to the number of nodes in the graph
     * @return
     */


    @Override
    public boolean isConnected() {
        if (this.myWGraphAlgo.nodeSize() == 0) return true;
        initConnectedSizeAndTag();
        boolean ask = (connectedSize == this.myWGraphAlgo.nodeSize());
        connectedSize = 0;
        WGraph_DS.reset();
        return ask;
    }


    /**
     * The function is initialized at each node the tag:
     * Tag = distance to the first junction
     * And initializes connectedSize:
     * connectedSize = The number of members that managed to reach from the first including the first
     */
    private void initConnectedSizeAndTag() {      // O(e)
        node_info first = getFirstNode(this.myWGraphAlgo);
        initConnectedSizeAndTag(first.getKey());
    }

    /**
     * @param g
     * @return The first node in the graph g
     */
    private node_info getFirstNode(weighted_graph g) {
        Iterator it = this.myWGraphAlgo.getV().iterator();
        return (node_info) it.next();
    }

   /*
     We will put the "current" node into the queue
      The function is initialized at each node the tag:
     Tag = distance to the "current" junction
      And initializes connectedSize:
      connectedSize = The number of members that managed to reach from the "current" including the "current"
    */

    private void initConnectedSizeAndTag(int current) {  // O(e)
        Queue<node_info> q1 = new LinkedList<node_info>();
        Queue<node_info> q2 = new LinkedList<node_info>();
        node_info first = this.myWGraphAlgo.getNode(current);
        first.setTag(0);
        connectedSize++;
        q1.add(first);
        initConnectedSizeAndTag(q1, q2);           // O(e)
    }

    /*
    Method:
    We will go through all the nodes by distance levels from the first node
    At each vertex at the distance level i we will go over all its neighbors and update the tag to
            (i + 1)
    So also we will know that we have already been there because
            (Tag! = -1)
    And we will add them to "Q2"
    When in "Q1" the nodes are at a distance i from the first node and in "Q2"
    Distance (i + 1)
    Then we will reactivate the function and switch between the queues
    And so we get that each round represents a distance level from the first node.
    And we go all the way.
    When we reach a state where both queues are empty it means that we have passed all the nodes at distance i
    And no nodes were added at a distance (i + 1)
    So we went through all the connected components
     */
    /**
     * Gets two queues Q1, Q2
     * So that in Q1 there is an "current" junction
     * The function is initialized at each node the tag:
     * Tag = distance to the "current" junction
     * And the function is initialized in "connectedSize"
     * The number of nodes connected to "current" including "current"
     *
     * @param q1
     * @param q2
     */

    private void initConnectedSizeAndTag(Queue<node_info> q1, Queue<node_info> q2) {   //Complications: O(e) e = edgeSize
        while (!q1.isEmpty()) {
            node_info current = q1.poll();
            double TagNew = current.getTag() + 1;
//            int TagNew = current.getTag() + 1;
            for (node_info node : this.myWGraphAlgo.getV(current.getKey())) {
                if (node.getTag() == -1) {
                    node.setTag(TagNew);
                    q2.add(node);
                    connectedSize++;
                }
            }
        }
        if (q2.isEmpty())
            return;                                             //There are no nodes at a distance (i + 1) The function will end
        initConnectedSizeAndTag(q2, q1);
        return;
    }


    /**
     * We will update the "tag" of each node = node distance from the node with key = src
     * We return the tag of "dest"
     * because it is equal to the distance of "dest"  From src
     * @param src - start node
     * @param dest - end (target) node
     * @return
     */

    @Override
    public double shortestPathDist(int src, int dest) {
        if (this.myWGraphAlgo.getNode(src) == null || this.myWGraphAlgo.getNode(dest) == null) return -1;
        if (dest == src) return 0;
        initTag(src);
        double ans = this.myWGraphAlgo.getNode(dest).getTag();
        WGraph_DS.reset();
        return ans;
    }

    /**
     * Using the initTag function (PriorityQueue <node_info> pq)
     * We will update the value of the tag for each vertex
     * so that the value of the tag is equal to the lowest weight of a trajectory from it to the initial vertex
     * @param src
     */
    private void initTag(int src) {
        Comparator<node_info> comparator = new WGraph_Algo();
        PriorityQueue<node_info> pq = new PriorityQueue<node_info>(comparator);
        this.myWGraphAlgo.getNode(src).setTag(0);
        pq.add(this.myWGraphAlgo.getNode(src));
        initTag(pq);
    }


    /*
            Updates the tag to all vertices
        so that for each vertex the tag is equal to the lowest weight of a trajectory to the current vertex

        The algorithm:
        Let's start with the first vertex whose distance from itself is 0
        We will run on all its neighbors
        and update them the tag that will be equal to the tag of node  the  current + the weight of the side that connects them.
        We will then put all of his neighbors in the priority queue (lowest weight).
        And run until the line is over
        So in each iteration we will perform the same action on the vertex at the top of the priority queue
        when we go over its neighbors and for that only in case the tag value has not yet been updated or the new value is lower than the old one
        we will perform the tag update and re-insert queue
     */

    private void initTag(PriorityQueue<node_info> pq) {
        while (!pq.isEmpty()) {
            int current = pq.poll().getKey();
            double tagOfCurrent = this.myWGraphAlgo.getNode(current).getTag();
            for (node_info neighbor : this.myWGraphAlgo.getV(current)) {
                double weight = this.myWGraphAlgo.getEdge(current, neighbor.getKey());
                double distance = weight + tagOfCurrent;
                if (distance < neighbor.getTag() || neighbor.getTag() == -1) {
                    neighbor.setTag(distance);
                    pq.add(neighbor);
                }
            }
        }
    }



    /*
     * We will update the "tag" of each node
     * Tag = node distance from the node with key dest
     * We do this using the  initTag(dest) function
     * Then
     * In case the "tag" of node.get (src) = -1
     * This means that the nodes are not connected
     * Therefore there is no trajectory in their sons
     * and we will return null
     * Otherwise we will create a Linklist and add the first node to it
     * Then using the shortestPath(int src) function we will add all the other nodes of the route to the list
     * Then to finish
     * We will reset the tag of all nodes using the NodeData.reset function
     * And return the received list
     *
     * @param src  - start node
     * @param dest - end (target) node
     * @return
     */

    @Override
    public List<node_info> shortestPath(int src, int dest) {
        if (this.myWGraphAlgo.getNode(src) == null || this.myWGraphAlgo.getNode(dest) == null) return null;
        initTag(dest);
        if (this.myWGraphAlgo.getNode(src).getTag() == -1) return null;
        LinkedList<node_info> ans = shortestPath2(src,dest);
        ans = shortestPath2(src, ans,dest);
        WGraph_DS.reset();
        return ans;
    }





    private LinkedList<node_info> shortestPath2(int src,int dest) {
        LinkedList<node_info> ans = new LinkedList<node_info>();
        ans.add(this.myWGraphAlgo.getNode(src));
        ans = shortestPath2(src, ans,dest);
        return ans;
    }

/*
Now that each vertex tag is the distance from the vertex to the Dest,
  We will use the following method:
We'll start from the first vertex and add it to the record,
Then we will look for which of its neighbors has a distance from Dest as the distance of the current vertex less the rib in them
And so we will know that this is the direction we need to go,
We will continue in the same way only this time the current vertex is the vertex to which we have progressed.
The algorithm will stop when we reach the vertex Dest
 */

    private LinkedList<node_info> shortestPath2(int current, LinkedList<node_info> ans,int dest) {
        double tagOfCurrent = this.myWGraphAlgo.getNode(current).getTag();
        for (node_info neighborOfCurrent : this.myWGraphAlgo.getV(current)) {
            double edge = this.myWGraphAlgo.getEdge(current, neighborOfCurrent.getKey());
            double difference = tagOfCurrent - neighborOfCurrent.getTag();
            if (Math.abs(difference - edge) <= EPS && !ans.contains(neighborOfCurrent)) {
                ans.add(neighborOfCurrent);
                if (neighborOfCurrent.getKey()==dest) return ans;
                ans = shortestPath2(neighborOfCurrent.getKey(), ans,dest);
                break;
            }
        }
        return ans;
    }






    @Override
    public boolean save(String file)  {
        try {
            PrintWriter pw = new PrintWriter(new File(file));
            pw.println("my graph:\n");
            for (node_info node : this.myWGraphAlgo.getV()) {
                pw.print(node + ",\n\n");
            }
            pw.println("node size = " + this.myWGraphAlgo.nodeSize());
            pw.println("edge size = " + this.myWGraphAlgo.edgeSize());
            pw.println("MC  = " + this.myWGraphAlgo.getMC());
            pw.close();
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }







/*
    I went through all the text in the same order I saved it in the save function
    Then this time I loaded all the data into the new graph
 */

    @Override
    public boolean load(String file) {
        WGraph_DS g1 = new WGraph_DS();
        String line = "";
        try {

            BufferedReader br = new BufferedReader(new FileReader(file));
            br.readLine();
            br.readLine();
            while ((line = br.readLine()) != null) {
                if (line.charAt(0) == 'n') {
                    br.readLine();
                    line = br.readLine();
                    line = line.substring(6);
                    g1.setMC(Integer.valueOf(line));
                    break;
                }
                int endKey = line.indexOf(" ", 5);
                String stringKey = line.substring(5, endKey);          //key
                int key = Integer.valueOf(stringKey);
                int indexStartInfo = line.indexOf("info:") + 6;                 //info
                int indexEndInfo = line.indexOf(" ", indexStartInfo);
                String info = line.substring(indexStartInfo, indexEndInfo);
                int indexStartTag = line.indexOf("tag:") + 5;          //tag
                String stringTag = line.substring(indexStartTag);
                double tag = Double.valueOf(stringTag);
                g1.addNode(key);
                node_info current = g1.getNode(key);
                current.setInfo(info);
                current.setTag(tag);
                String line2;
                line2 = br.readLine();      // edge:
                int indexStartEdge = line2.indexOf('{') + 1;
                line2 = line2.substring(indexStartEdge, line2.indexOf('}'));
                while (!line2.isEmpty()) {
                    String stringKeyNeighbor = line2.substring(0, line2.indexOf("="));
                    int keyNeighbor = Integer.valueOf(stringKeyNeighbor);
                    int indexOfEndEdge = line2.length();
                    if (line2.indexOf(',') != -1) indexOfEndEdge = line2.indexOf(',');
                    String stringEdgeWeight = line2.substring(line2.indexOf("=") + 1, indexOfEndEdge);
                    double edgeWeight = Double.valueOf(stringEdgeWeight);
                    g1.connect(key, keyNeighbor, edgeWeight);
                    line2 = (line2.indexOf(',') == -1) ? line2.substring(indexOfEndEdge) : line2.substring(indexOfEndEdge + 2);
                }
                br.readLine();
                br.readLine();//"   "
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        this.init(g1);
        return true;
    }



 /*
    //        @Override
//    public boolean save(String file) throws FileNotFoundException {
//
//        try {
//            FileOutputStream fileOutputStream = new FileOutputStream(file);
//            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
//            objectOutputStream.writeObject((WGraph_DS) this.myWGraphAlgo);
//            fileOutputStream.close();
//            objectOutputStream.close();
//
//        } catch (IOException e) {
//            e.printStackTrace();
//            return false;
//        }
//
//        return true;
//
//    }


//    @Override
//    public boolean load(String file) {
//        try {
//            FileInputStream  fileInputStream=new FileInputStream(file);
//            ObjectInputStream objectInputStream=new ObjectInputStream(fileInputStream);
//            this.myWGraphAlgo =(WGraph_DS) objectInputStream.readObject();
//            fileInputStream.close();
//            objectInputStream.close();
//
//        } catch (IOException | ClassNotFoundException e) {
//            e.printStackTrace();
//            return false;
//        }
//
//        return true;
//    }
*/


    /**
     * Performs a comparison between the two vertices according to their Tag:
     * In case the Tag of the first node is greater than what the Tag of the second node returns 1
     * In case the Tag of the first node is smaller than the Tag of the second node returns 1-
     * Returns 0 in case they are equal
     * @param n1
     * @param n2
     */

    @Override
    public int compare(node_info n1, node_info n2) {
        if (n1.getTag() < n2.getTag()) {
            return -1;
        }
        if (n1.getTag() > n2.getTag()) {
            return 1;
        }
        return 0;
    }


}