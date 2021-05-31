

package gameClient;

import api.DWGraph_Algo;
import api.directed_weighted_graph;
import api.edge_data;

import java.util.Comparator;

/**
 * This Class represents a Match between a Pokemon and an Agent that chases after the Pokemon
 * The class was created for an algorithm that matches between agents and pokemons in an efficient way
 * The class calculate time for agent getting to a pokemon, and check how worth it comparing to chasing another pokemon
 * @author Gilad Shotland
 * @author Shlomo Glick
 */
public class match implements Comparator<match> {
    /********** Initialization Variables ***********/
    private CL_Agent agent;
    private CL_Pokemon pokemon;
    private double time;
    private double dist;
    private double wage;
    private directed_weighted_graph graph;
    private DWGraph_Algo algo;

    /************ Constructors ***************/
    public match(int key) {
        this.dist=key;

    }

    public match(CL_Agent agent, CL_Pokemon pokemon,directed_weighted_graph graph) {
        this.graph = graph;
        this.algo = new DWGraph_Algo();
        algo.init(this.graph);
        this.agent = agent;
        this.pokemon = pokemon;
        updateWage();
    }

    /************* Getters *************/

    /**
     * This method returns the wage of the match
     * wage of the match = (value of the pokemon)/ (time for the agent getting the pokemon)
     * @return double
     */
    public double getWage() {
        return wage;
    }

    /**
     * This method return the agent in the match
     * @return CL_Agent
     */
    public CL_Agent getAgent() {
        return agent;
    }

    /**
     * This method return the pokemon in the match
     * @return CL_Pokemon
     */
    public CL_Pokemon getPokemon() {
        return pokemon;
    }

    /**
     * This method updates the wage between the pokemon and the agent (as explained)
     */
    private void updateWage() {
        edge_data edge = this.pokemon.get_edge();
        this.dist = algo.shortestPathDist(this.agent.getSrcNode(), edge.getSrc());
        if (dist==-1) dist=Double.MAX_VALUE;
        else dist+= edge.getWeight();
        double speed=agent.getSpeed();
        if (speed==0) speed+=0.0000001;
        this.time = dist / speed;
        if (time==0) time+=0.0000001;
        this.wage = this.pokemon.getValue() / this.time;
    }

    /**
     * compare by wage
     * @param o1
     * @param o2
     * @return
     */
    @Override
    public int compare(match o1, match o2) {
        if (o1.getWage() < o2.getWage()) return 1;
        if (o1.getWage() > o2.getWage()) return -1;
        return 0;
    }

    /**
     * This method returns the time for the agent getting the Pokemon
     * @return double
     */
    public double getTime() {
        return time;
    }
}




