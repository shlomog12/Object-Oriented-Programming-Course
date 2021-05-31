package api;

import java.util.Objects;


/**
 * This Class represents the set of operations applicable on a
 * directional edge(src,dest) in a (directional) weighted graph.
 * This class implements the edge_data interface
 * @author Gilad Shotland
 * @author Shlomo Glick
 *
 */
public class edgeData implements edge_data {

    /********* Initialization Variables *********/
    private int src; //id of the node which this edge is going out from
    private int dest;//id of the node which this edge is going in to
    private double weight;//weight of the node
    private String info;
    private int tag;//tag of the edge

    /************** Constructor **************/
    public edgeData(int src, int dest, double weight) {
        this.src = src;
        this.dest = dest;
        this.weight = weight;
        this.info=src+"_"+dest;
        this.tag=-1;
    }

    /************** Getters **************/
    /**
     * This method returns the id of the source node of this edge
     * Source node of directed edge is the node which the edge is going out from
     * @return int
     */
    @Override
    public int getSrc() {
        return this.src;
    }

    /**
     * This method returns the id of the destination node of this edge
     * Destination node of directed edge is the node which the edge is going in to
     * @return int
     */
    @Override
    public int getDest() {
        return dest;
    }
    /**
     * This method returns the weight of this edge (positive value)
     * @return double
     */
    @Override
    public double getWeight() {
        return this.weight;
    }
    /**
     * This method returns the remark (meta data) associated with this edge.
     * @return String
     */
    @Override
    public String getInfo() {
        return this.info;
    }
    /**
     * This method returns the temporal data (aka color: e,g, white, gray, black)
     * which can be used on algorithms
     * @return int
     */
    @Override
    public int getTag() {
        return this.tag;
    }


    /************** Setters **************/

    /**
     * This method allows changing the remark (meta data) associated with this edge.
     * @param s is the new info
     */
    @Override
    public void setInfo(String s) {
        this.info = s;
    }

    /**
     * This method allows setting the "tag" value for temporal marking an edge - common
     * practice for marking by algorithms.
     * @param t - the new value of the tag
     */

    @Override
    public void setTag(int t) {
        this.tag = t;
    }

    /**
     * Equals method between this edge and a given edge
     * The method return true if and only if the the source and destination nodes and weight are the same in both edges
     * @param o
     * @return boolean
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        edgeData edgeData = (edgeData) o;
        return src == edgeData.src &&
                dest == edgeData.dest &&
                Double.compare(edgeData.weight, weight) == 0 &&
                tag == edgeData.tag &&
                Objects.equals(info, edgeData.info);
    }
}
