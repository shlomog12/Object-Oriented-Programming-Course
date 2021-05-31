package ex0;


import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class Graph_Algo implements graph_algorithms {
    private graph myGraphAlgo = new Graph_DS();
    private int connectedSize = 0;

    public Graph_Algo() {
        this.myGraphAlgo = null;
    }


    @Override
    public void init(graph g) {
        this.myGraphAlgo = g;
    }

    /**
     * Creates a new graph
     * Runs on all nodes in the old graph
     * And for each node creates a new node with the same data
     * And adds it to the new graph
     * Then goes over all its neighbors from the old graph
     * And if they have already been copied to the new graph adds them to the neighbors list of the new node
     *
     * @return g
     */
    @Override
    public graph copy() {   //    Complications  ->  O(1)*n*deg(Vi)=total deg(g)*O(1)=2edgeSize*O(1)=2k*O(1)=    O(k)   (So that k = edgeSize)
        if (this.myGraphAlgo==null) return null;
        Graph_DS g = new Graph_DS();
        for (node_data oldNode : this.myGraphAlgo.getV()) {
            NodeData newNode = new NodeData(oldNode.getKey(), oldNode.getInfo());
            newNode.setTag(oldNode.getTag());
            g.addNode(newNode);
            for (node_data neighbor : oldNode.getNi()) {
                if (g.getNode(neighbor.getKey()) != null) {
                    g.connect(neighbor.getKey(), newNode.getKey());
                }
            }
        }
        g.setMC(this.myGraphAlgo.getMC());
        return g;
    }



    /**
     * run the initConnectedSize function
     * That will update connectedSize
     * To the number of nodes connected to the first node.
     * Then if the graph is connected
     * We will get the number of nodes connected to the first node
     * Equal to the number of nodes in the graph
     *
     * @return
     */
    @Override
    public boolean isConnected() {                                   // O(e) e=edgeSize
        if (this.myGraphAlgo.nodeSize() == 0) return true;
        initConnectedSizeAndTag();
        boolean ask = (connectedSize == this.myGraphAlgo.nodeSize());
        connectedSize = 0;
        NodeData.reset();                                                //Reset the tag at all nodes in the graph for the next test
        return ask;
    }

    /**
     * The function is initialized at each node the tag:
     * Tag = distance to the first junction
     * And initializes connectedSize:
     * connectedSize = The number of members that managed to reach from the first including the first
     */
    private void initConnectedSizeAndTag() {      // O(e)
        node_data first = getFirstNode(this.myGraphAlgo);
        initConnectedSizeAndTag(first.getKey());
    }

    /**
     * @param g
     * @return The first node in the graph g
     */
    private node_data getFirstNode(graph g) {
        Iterator it = this.myGraphAlgo.getV().iterator();
        return (node_data) it.next();
    }


    /**
     * We will put the "current" node into the queue
     * The function is initialized at each node the tag:
     * Tag = distance to the "current" junction
     * And initializes connectedSize:
     * connectedSize = The number of members that managed to reach from the "current" including the "current"
     */

    private void initConnectedSizeAndTag(int current) {  // O(e)
        Queue<node_data> q1 = new LinkedList<node_data>();
        Queue<node_data> q2 = new LinkedList<node_data>();
        node_data first = this.myGraphAlgo.getNode(current);
        first.setTag(0);
        connectedSize++;
        q1.add(first);
        initConnectedSizeAndTag(q1, q2);           // O(e)
    }


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
    private void initConnectedSizeAndTag(Queue<node_data> q1, Queue<node_data> q2) {   //Complications: O(e) e = edgeSize
        while (!q1.isEmpty()) {
            node_data current = q1.poll();
            int TagNew = current.getTag() + 1;
            for (node_data node : current.getNi()) {
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
     */

    @Override
    public int shortestPathDist(int src, int dest) {
        initConnectedSizeAndTag(src);
        int tag = this.myGraphAlgo.getNode(dest).getTag();
        NodeData.reset();
        return tag;
    }

    /**
     * We will update the "tag" of each node
     * Tag = node distance from the node with key dest
     * We do this using the initConnectedSizeAndTag(dest) function
     * Then
     * In case the "tag" of node.get (src) = -1
     * This means that the nodes are not connected
     * Therefore there is no trajectory in their sons
     * and we will return null
     * Otherwise we will create a Linklist and add the first node to it
     * Then using the AddNodesToTheList function we will add all the other nodes of the route to the list
     * Then to finish
     * We will reset the tag of all nodes using the NodeData.reset function
     * And return the received list
     *
     * @param src  - start node
     * @param dest - end (target) node
     * @return
     */

    @Override
    public List<node_data> shortestPath(int src, int dest) {
        initConnectedSizeAndTag(dest);
        if (this.myGraphAlgo.getNode(src).getTag() == -1) return null;
        List<node_data> ask = new LinkedList<node_data>();
        ask.add(myGraphAlgo.getNode(src));
        AddNodesToTheList(ask, myGraphAlgo.getNode(src));
        NodeData.reset();
        return ask;
    }

    /**
     * We will start with "src"
     * and then look for its neighbor with a smaller "Tag"
     * and then that means it is closer to dest
     * we will move to it we will add it to LinkedList and do exactly the same action
     * only this time closer to dest in step 1 so we repeat the action until we find "dest" in neighbors collection
     * We will put it in the list
     * And we will stop
     *
     * @param ask
     * @param current
     */
    private void AddNodesToTheList(List<node_data> ask, node_data current) {
        if (current.getTag() == 0) {
            return;
        }
        for (node_data node : current.getNi()) {
            if (node.getTag() < current.getTag() && node.getTag() != -1) {
                ask.add(node);
                AddNodesToTheList(ask, node);
                break;
            }
        }
        return;
    }
}






