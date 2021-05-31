package api;
/**
 * This class represents the set of operations applicable on a
 * node (vertex) in a directional weighted graph.
 * This Class implements the node_data interface
 * @author Gilad Shotland
 * @author Shlomo Glick
 *
 */


import java.io.FileNotFoundException;
import java.util.Comparator;
import java.util.Objects;


public class nodeDate implements node_data {

    /*******Initialization Variables*******/
    private double weight;
    private int key;
    private String info;
    private int tag;
    private int timeChang;
    private static int countKey;
    private static int reset;
    private geoLocation locetion;

    /********** Constructors **********/

    /**
     * default constructor
     */
    public nodeDate() {
        this.key = countKey++;
        this.info = 'v' + String.valueOf(this.key);
        this.tag = -1;
        this.weight=-1;
        locetion =  new geoLocation(0,0,0);                    //new geoLocation(0,0,0);
    }

    /**
     * Constructor that receives String info
     * and creates a vertex whose data is info
     *
     * @param info
     */
    public nodeDate(String info) {
        this.key = countKey++;
        this.info = info;
        this.tag = -1;
        this.weight=-1;
        locetion = new geoLocation(0,0,0);           //new geoLocation(0,0,0);
    }

    /**
     * constructor that gets String info and int "key"
     * and creates a vertex whose data is info and whose key is "key"
     *
     * @param key
     * @param info
     */

    public nodeDate(Integer key, String info) {
        this.key = key;
        this.info = info;
        this.tag = -1;
        this.weight=-1;
        locetion = new geoLocation(0,0,0);          //new geoLocation(0,0,0);
    }

    /**
     * Constructor that gets int "key" , String "info" , int "tag"  and double "weight"
     * and creates a vertex with these parameters.
     * @param key
     * @param info
     * @param tag
     * @param weight
     */
    public nodeDate(Integer key, String info,int tag, double weight) {
        this.key = key;
        this.info = info;
        this.tag = tag;
        this.weight=weight;
        locetion = new geoLocation(0,0,0);          //new geoLocation(0,0,0);
    }


    /**
     * Constructor that gets int "key" and creates a vertex with this key
     * @param key
     */
    public nodeDate(Integer key) {
        this.key = key;
        this.info = 'v' + String.valueOf(key);
        this.tag = -1;
        this.weight=-1;
        locetion = new geoLocation(key*10,key*10,0);                 //new geoLocation(0,0,0);
    }

    /***********Getters***********/


    /**
     *This method returns the key (id) associated with this node.
     * @return int
     */
    @Override
    public int getKey() {
        return this.key;
    }

    /** Returns the location of this node, if
     * there's no location for the node return null.
     *
     * @return geo_location
     */
    @Override
    public geo_location getLocation() {
        return this.locetion;
    }
    /**
     * This method returns the weight associated with this node.
     * @return double
     */
    @Override
    public double getWeight() {
        if (timeChang < reset) this.weight = -1;
        return this.weight;
    }
    /**
     * This method returns the remark (meta data) associated with this node.
     * @return String
     */
    @Override
    public String getInfo() {
        return this.info;
    }

    /**
     * In case the tag is updated after the reset
     * returns the tag value
     * otherwise resets the tag value to - 1 and returns -1
     *
     * @return int
     */
    @Override
    public int getTag() {
        if (timeChang < reset) this.tag = -1;
        return this.tag;
    }


    /*********Setters*********/


    /**
     * This method allows changing this node's location.
     * @param p -  new location  (position) of this node.
     */
    @Override
    public void setLocation(geo_location p) {
        this.locetion = (geoLocation) p;
    }



    /**
     * This method allows changing this node's weight.
     * @param w - the new weight
     */
    @Override
    public void setWeight(double w) {
        this.weight = w;
        timeChang = reset;
    }


    /**
     * This method allows changing the remark (meta data) associated with this node.
     * @param s
     */

    @Override
    public void setInfo(String s) {
        this.info = s;
    }



    /**
     * This method allows setting the "tag" value for temporal marking an node - common
     * practice for marking by algorithms.
     * @param t - the new value of the tag
     */
    @Override
    public void setTag(int t) {
        this.tag = t;
        timeChang = reset;
    }



    //do we need it?

    /**
     * This method increase the reset f
     */
    public static void reset() {
        reset++;
    }

    /**
     * toString method
     * @return String
     */
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append(this.getInfo());
        return String.valueOf(str);
    }

    /**
     * This method is equals method between this node and a given node
     * @param o is the node the method compare to this node
     * @return boolean
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        api.nodeDate nodeDate = (api.nodeDate) o;
        return Double.compare(nodeDate.weight, weight) == 0 &&
                key == nodeDate.key &&
                tag == nodeDate.tag &&
                timeChang == nodeDate.timeChang &&
                Objects.equals(info, nodeDate.info) &&
                Objects.equals(locetion, nodeDate.locetion);
    }
    /**
     * This class is a comparing class of two nodes implements Comparator interface
     * This comparator compares nodes by their weight
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
