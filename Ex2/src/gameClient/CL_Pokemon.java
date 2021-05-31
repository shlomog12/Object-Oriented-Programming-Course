
package gameClient;

import api.edge_data;
import gameClient.util.Point3D;
import org.json.JSONObject;

import java.util.Comparator;

/**
 *  This class represents This Class represents a Pokemon in the Pokemon Catching Simulation
 *  Every object represents a Pokemon in one the simulation scenario which represented on a directed weighted graph
 *  Every Pokemon has an id, speed, position, graph of the scenario, current edge that the pokemon 'stands' on, current agent which chases after the Pokemon
 *  in this class we built a comparator to compare between two pokemons by their value.
 */
public class CL_Pokemon {
    /************ Initialization Variables ************/
    private edge_data _edge;
    private double _value;
    private int _type;
    private Point3D _pos;
    private int agent_id;
    double _speed;

    /***************** Constructors ****************/
    public CL_Pokemon(Point3D p, int t, double v, double s, edge_data e) {
        _type = t;
        _speed = s;
        _value = v;
        set_edge(e);
        _pos = p;
        this.agent_id = -1;
    }

    /************ Getters **************/
    /**
     * This method  returns the edge which the pokemon stands on
     * @return edge_data
     */
    public edge_data get_edge() {
        return _edge;
    }


    /**
     * This method returns the 3D location of the pokemon
     * @return Point3D
     */
    public Point3D getLocation() {
        return _pos;
    }

    /**
     * This method returns the type of the pokemon
     * the method returns 1 if the pokemons stands streight (on the edge between the node with the small id (as src) and the node with the big id
     * return -1 if the pokemon stands on the edge between the node with the big id and the node with the small id
     * @return int
     */
    public int getType() {
        return _type;
    }

    /**
     * This method returns the value of the pokemon - how much points the pokemon worth
     * @return
     */
    public double getValue() {
        return _value;
    }

    /**
     * This method returns the id of the agent that chases after the pokemon
     * @return double
     */
    public double getKeyAgent_id() {
        return agent_id;
    }



    /********** Setters **********/

    /**
     * This method is a setter for the agent that chases after the pokemon
     * @param agent_id int
     */
    public void setKeyAgent_id(int agent_id) {
        this.agent_id = agent_id;
    }

    /**
     * setter for the edge which the pokemons stands on
     * @param _edge edge_data
     */
    public void set_edge(edge_data _edge) {
        this._edge = _edge;
    }

    /**
     * toString method for printing
     * @return String
     */
    public String toString() {
        return "F:{v=" + _value + ", t=" + _type + "}";
    }


}
