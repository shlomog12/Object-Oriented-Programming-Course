package ex1;

import java.io.*;
import java.util.*;

public class WGraph_Algo implements weighted_graph_algorithms, Comparator<node_info> {
    final double EPS = 0.001;

    private weighted_graph myWGraphAlgo = new WGraph_DS();
    private int connectedSize = 0;

    public WGraph_Algo() {
        this.myWGraphAlgo = null;
    }

    @Override
    public void init(weighted_graph g) {
        this.myWGraphAlgo = g;
    }

    @Override
    public weighted_graph getGraph() {
        return this.myWGraphAlgo;
    }

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

    @Override
    public boolean isConnected() {
        if (this.myWGraphAlgo.nodeSize() == 0) return true;
        initConnectedSizeAndTag();
        boolean ask = (connectedSize == this.myWGraphAlgo.nodeSize());
        connectedSize = 0;
        WGraph_DS.reset();                                               //Reset the tag at all nodes in the graph for the next test
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

    private void initConnectedSizeAndTag(int current) {  // O(e)
        Queue<node_info> q1 = new LinkedList<node_info>();
        Queue<node_info> q2 = new LinkedList<node_info>();
        node_info first = this.myWGraphAlgo.getNode(current);
        first.setTag(0);
        connectedSize++;
        q1.add(first);
        initConnectedSizeAndTag(q1, q2);           // O(e)
    }

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


    @Override
    public double shortestPathDist(int src, int dest) {
        if (this.myWGraphAlgo.getNode(src) == null || this.myWGraphAlgo.getNode(dest) == null) return -1;
        if (dest == src) return 0;
        initTag(src);
        double ans = this.myWGraphAlgo.getNode(dest).getTag();
        WGraph_DS.reset();
        return ans;
    }

    private void initTag(int src) {
        Comparator<node_info> comparator = new WGraph_Algo();
        PriorityQueue<node_info> pq = new PriorityQueue<node_info>(comparator);
        this.myWGraphAlgo.getNode(src).setTag(0);
        pq.add(this.myWGraphAlgo.getNode(src));
        initTag(pq);
    }

    private void initTag(PriorityQueue<node_info> pq) {
        while (!pq.isEmpty()) {
            int current = pq.poll().getKey();
            double tagOfCurrent = this.myWGraphAlgo.getNode(current).getTag();
            for (node_info neighbor : this.myWGraphAlgo.getV(current)) {
                double weight = this.myWGraphAlgo.getEdge(current, neighbor.getKey());
                double distance = weight + tagOfCurrent;
                if (distance <= neighbor.getTag() || neighbor.getTag() == -1) {
                    neighbor.setTag(distance);
                    pq.add(neighbor);
                }
            }
        }
    }


    @Override
    public List<node_info> shortestPath(int src, int dest) {
        if (this.myWGraphAlgo.getNode(src) == null || this.myWGraphAlgo.getNode(dest) == null) return null;
        initTag(dest);
        if (this.myWGraphAlgo.getNode(src).getTag() == -1) return null;
        LinkedList<node_info> ans = shortestPath(src);
        WGraph_DS.reset();
        return ans;
    }

    private LinkedList<node_info> shortestPath(int src) {
        LinkedList<node_info> ans = new LinkedList<node_info>();
        ans.add(this.myWGraphAlgo.getNode(src));
        ans = shortestPath(src, ans);
        return ans;
    }

    private LinkedList<node_info> shortestPath(int current, LinkedList<node_info> ans) {
        double tagOfCurrent = this.myWGraphAlgo.getNode(current).getTag();
        for (node_info neighborOfCurrent : this.myWGraphAlgo.getV(current)) {
            double edge = this.myWGraphAlgo.getEdge(current, neighborOfCurrent.getKey());
            double difference = tagOfCurrent - neighborOfCurrent.getTag();
            if (Math.abs(difference - edge) <= EPS) {
                ans.add(neighborOfCurrent);
                ans = shortestPath(neighborOfCurrent.getKey(), ans);
                break;
            }
        }
        return ans;
    }


    @Override
    public boolean save(String file) throws FileNotFoundException {
        try {
            PrintWriter pw = new PrintWriter(new File(file + ".txt"));
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

    @Override
    public boolean load(String file) {
        if (file.indexOf(".txt")!=file.length()-4){
            file=file+".txt";
        }
        WGraph_DS g1 = new WGraph_DS();
        String line = "";
        try {

            BufferedReader br = new BufferedReader(new FileReader(file));                //"C:\\Users\\shlom\\IdeaProjects\\untitled\\" + file + ".txt"
            br.readLine();
            br.readLine();
            while ((line = br.readLine()) != null) {
                if (line.charAt(0)=='n'){
                    br.readLine();
                    line= br.readLine();
                    line=line.substring(6);
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
                    line2 = (line2.indexOf(',') == -1) ? line2.substring(indexOfEndEdge) : line2.substring(indexOfEndEdge+2);
                }
                br.readLine();                               //"   "
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        this.init(g1);
        return true;
    }


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