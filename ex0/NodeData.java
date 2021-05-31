package ex0;


import java.util.Collection;
import java.util.HashMap;

public class NodeData implements node_data {

    private int key;
    private HashMap<Integer, node_data> Ni;
    private String info;
    private int tag;
    private static int countKey;
    private static int resetTag;
    private int personalResetTag;


    public NodeData() {
        this.key = countKey++;
        this.info = "";
        this.Ni = new HashMap<Integer, node_data>();
        this.tag = -1;
    }

    public NodeData(String info) {
        this.key = countKey++;
        this.info = info;
        this.Ni = new HashMap<Integer, node_data>();
        this.tag = -1;
    }

    public NodeData(Integer key, String info) {
        this.key = key;
        this.info = info;
        this.Ni = new HashMap<Integer, node_data>();
        this.tag = -1;
    }

    @Override
    public int getKey() {
        return this.key;
    }

    @Override
    public Collection<node_data> getNi() {
        return this.Ni.values();
    }

    @Override
    public boolean hasNi(int key) {
        return Ni.containsKey(key);
    }


    /**
        In case both "t" and this are different from null
        And yet t is not in the neighbors' collection of "this":
        Adds t to neighbors list of "this"
     */
    @Override
    public void addNi(node_data t) {
        if (this == null || t == null) return;
        if (this.hasNi(t.getKey())) return;
        this.Ni.put(t.getKey(), t);
        t.addNi(this);
    }



    /**
    In case they are already neighbors
    Delete "Node" from the "It" Neighbors Collection
    Then run the function again to remove "it" from the "Junction" neighbors collection
     */

    @Override
    public void removeNode(node_data node) {
        if (!this.hasNi(node.getKey())) return;
        this.Ni.remove(node.getKey());
        node.removeNode(this);
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
    public int getTag() {
        if (personalResetTag < resetTag) this.tag = -1;
        return this.tag;
    }

    /*
    resetTag is like a last date to reset a tag for all nodes.
    In each reset we will promote it
    And if it is larger than personalResetTag of the current node
    This means that the current Tag has been updated before resetting,
    So we will reset and then return the value
     */

    @Override
    public void setTag(int t) {
        personalResetTag = resetTag;     //We will compare personalResetTag to resetTag so that later We will know that the Tag has been updated after the last resetTag update
        this.tag = t;
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
        return this.getInfo();
    }
}