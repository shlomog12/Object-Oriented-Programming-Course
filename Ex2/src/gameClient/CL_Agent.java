
package gameClient;

import api.directed_weighted_graph;
import api.edge_data;
import api.geo_location;
import api.node_data;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import gameClient.util.Point3D;
import org.json.JSONObject;

import java.util.PriorityQueue;

/**
 * This method represents an agent in the game moving from node the node throw edges
 * in a graph, aiming to catch as much as pokemons as he can
 * @Author Gilad Shotland
 * @author  Shlomo Glick
 */
public class CL_Agent {
    /********* Initialization Variables ***********/
    private int _id;
    private geo_location _pos;
    private double _speed;
    private edge_data _curr_edge;
    private node_data _curr_node;
    private directed_weighted_graph _gg;
    private CL_Pokemon _curr_fruit;
    private int next;
    private double _value;


    /*********** Constructors ************/
    public CL_Agent(directed_weighted_graph g, int start_node) {
        _gg = g;
        setMoney(0);
        this._curr_node = _gg.getNode(start_node);
        _pos = _curr_node.getLocation();
        _id = -1;
        setSpeed(0);
        this.next=-1;
    }
    /*********** Getters **************/
    /**
     * getter for the id of the node which the agent stands on
     * @return
     */
    public int getSrcNode() {
        return this._curr_node.getKey();
    }

    /**
     * getter for the location of the agnet
     * @return geo_location
     */
    public geo_location getLocation() {
        return _pos;
    }
    /**
     * getter for the id of the agent
     * @return int
     */
    public int getID() {
        return this._id;
    }


    /**
     * getter for the points the agent earned
     * @return double
     */
    public double getValue() {
        return this._value;
    }

    /**
     * getter for the speed of the agent
     * @return double
     */
    public double getSpeed() {
        return this._speed;
    }
    /**
     * getter for the current edge which the agent stands on
     * @return edge_data
     */
    public edge_data get_curr_edge() {
        return this._curr_edge;
    }

    /**
     * get the id of the next node which the agent need to go to
     * @return int
     */
    public int getNext() {
        return next;
    }

    /**
     * get the current pokemon which the agent chases after
     * @return CL_Pokemon
     */
    public CL_Pokemon get_curr_fruit() {
        return _curr_fruit;
    }



    /************** Setters *****************/

    /**
     * set points the agent earned
     * @param v
     */
    private void setMoney(double v) {
        _value = v;
    }

    /**
     * setter for the next move for the agent
     * the method returns true if and only if the destination was well set
     * @param dest id of the next node that the agent will go to
     * @return boolean
     */
    public boolean setNextNode(int dest) {
        boolean ans = false;
        int src = this._curr_node.getKey();
        this._curr_edge = _gg.getEdge(src, dest);
        if (isMoving()) ans = true;
        return ans;
    }

    /**
     * set the node which the agent stands on
     * @param src
     */
    public void setCurrNode(int src) {
        this._curr_node = _gg.getNode(src);
    }

    /**
     * setter for the speed of the agent
     * @param v double
     */
    public void setSpeed(double v) {
        this._speed = v;
    }
    /**
     * setter for the pokemon to chase afer
     * @param curr_fruit CL_Pokemon
     */
    public void set_curr_fruit(CL_Pokemon curr_fruit) {
        this._curr_fruit = curr_fruit;
    }


    /**
     * set the next node for the agent
     * @param next
     */
    public void setNext(int next) {
        this.next = next;
    }




    /**
     * update the data of this agent from json file
     *
     * @param json String
     */
    public void update(String json) {
        JSONObject line;
        try {
            line = new JSONObject(json);
            JSONObject ttt = line.getJSONObject("Agent");
            int id = ttt.getInt("id");
            if (this.getID() == -1) _id = id;
            double speed = ttt.getDouble("speed");
            String p = ttt.getString("pos");
            Point3D pp = new Point3D(p);
            int src = ttt.getInt("src");
            int dest = ttt.getInt("dest");
            double value = ttt.getDouble("value");
            this._pos = pp;
            this.setCurrNode(src);
            this.setSpeed(speed);
            this.setNextNode(dest);
            this.setMoney(value);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    /**
     * format the agent to a Json String
     * @return
     */
    public String toJSON() {
        Gson gson = new Gson();
        JsonObject date = new JsonObject();
        date.add("id", gson.toJsonTree(this.getID()));
        date.add("value", gson.toJsonTree(this.getValue()));
        date.add("src", gson.toJsonTree(this.getSrcNode()));
        date.add("dest", gson.toJsonTree(this.getNext()));
        date.add("speed", gson.toJsonTree(this.getSpeed()));
        date.add("pos", gson.toJsonTree(this.getLocation().toString()));
        JsonObject agent = new JsonObject();
        agent.add("Agent", date);
        return agent.toString();
    }


    /**
     * method for checking if the agent moving?
     * @return boolean
     */
    public boolean isMoving() {
        return this._curr_edge != null;
    }

    public String toString() {
        return toJSON();
    }









}