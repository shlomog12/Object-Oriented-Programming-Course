
package gameClient;

import api.*;
import gameClient.util.Point3D;
import gameClient.util.Range;
import gameClient.util.Range2D;
import gameClient.util.Range2Range;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.*;

/**
 * This class represents the arena of a scenario in the game, using a directed weighted graph, and lists of agents and pokemons.
 * The class can check for each pokemon which edge is the one that the pokemon stands on,
 * update pokemons as targets to the agents and create pokemon and agent lists from JSON files.
 * @author Gilad Shotland
 * @author Shlomo Glick
 * @author boaz.benmoshe
 */
public class Arena {
    public static final double EPS = 0.000001;
    /*********** Initialization Variables ************/
    private directed_weighted_graph graph;
    private List<CL_Agent> _agents;
    private List<CL_Pokemon> _pokemons;
    private List<String> _info;
    private static Point3D MIN = new Point3D(0, 100, 0);
    private static Point3D MAX = new Point3D(0, 100, 0);
    private long time;
    private long move;

    /********* Constructors ***********/
    public Arena() {
        _info = new ArrayList<String>();
    }

    private Arena(directed_weighted_graph g, List<CL_Agent> r, List<CL_Pokemon> p) {
        graph = g;
        this.setAgents(r);
        this.setPokemons(p);
    }

    /******** Getters ************/

    /**
     * getter for the agent at the scenario
     * @return List<CL_Agent>
     */
    public List<CL_Agent> getAgents() {
        return _agents;
    }

    /**
     * getter for the pokemons at the scenario
     * @return List<CL_Pokemon>
     */
    public List<CL_Pokemon> getPokemons() {
        return _pokemons;
    }

    /**
     * getter for the graph of the scenrio
     * @return directed_weighted_graph
     */
    public directed_weighted_graph getGraph() {
        return graph;
    }

    /**
     * getting a list of agents from a JSON String
     * @param aa JSON String of the game
     * @param gg graph of the scenario
     * @return List<CL_Agent>
     */
    public static List<CL_Agent> getAgents(String aa, directed_weighted_graph gg) {
        ArrayList<CL_Agent> ans = new ArrayList<CL_Agent>();
        try {
            JSONObject ttt = new JSONObject(aa);
            JSONArray ags = ttt.getJSONArray("Agents");
            for (int i = 0; i < ags.length(); i++) {
                CL_Agent c = new CL_Agent(gg, 0);
                c.update(ags.get(i).toString());
                ans.add(c);
            }
            //= getJSONArray("Agents");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return ans;
    }

    /**
     * getter for time remaining in the scenario
     * @return
     */
    public long getTime() {
        return time;
    }

    /**
     * getter for the 'move', the action of the game
     * @return long
     */
    public long getMove() {
        return move;
    }

    /******** Setters *********/

    /**
     * setter for the pokemons at the scenario
     * @param f List<CL_Pokemons> for the scenario
     */
    public void setPokemons(List<CL_Pokemon> f) {
        this._pokemons = f;
    }

    /**
     * setter for the agents at the scenario
     * @param f List<CL_Agent> for the scenario
     */
    public void setAgents(List<CL_Agent> f) {
        this._agents = f;
    }

    /**
     * setter for the graph of the scenario
     * @param g graph
     */
    public void setGraph(directed_weighted_graph g) {
        this.graph = g;
    }

    /**
     * set time remaining
     * @param timeToEnd long
     */
    public void setTime(long timeToEnd) {
        this.time = timeToEnd;

    }

    /**
     * setter for move
     * @param move long
     */
    public void setMove(long move) {
        this.move = move;
    }


    /**
     * get ArrayList<CL_Pokemon> from JSON
     * @param fs JSON String of the game scenario
     * @return ArrayList<CL_Pokemon>
     */
    public static ArrayList<CL_Pokemon> json2Pokemons(String fs) {
        ArrayList<CL_Pokemon> ans = new ArrayList<CL_Pokemon>();
        try {
            JSONObject ttt = new JSONObject(fs);
            JSONArray ags = ttt.getJSONArray("Pokemons");
            for (int i = 0; i < ags.length(); i++) {
                JSONObject pp = ags.getJSONObject(i);
                JSONObject pk = pp.getJSONObject("Pokemon");
                int t = pk.getInt("type");
                double v = pk.getDouble("value");
                String p = pk.getString("pos");
                CL_Pokemon f = new CL_Pokemon(new Point3D(p), t, v, 0, null);
                ans.add(f);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return ans;
    }


    /**
     * find for pokemon the edge which it stands on
     * the method tries all the edges in the graph
     * @param fr pokemon
     * @param g the graph the scneario
     * we chose to use the method of the lecturer
     * @author Boaz Ben Moshe
     */
    public static void updateEdge(CL_Pokemon fr, directed_weighted_graph g) {
        for (node_data v : g.getV()) {
            for (edge_data e : g.getE(v.getKey())) {
                boolean f = isOnEdge(fr.getLocation(), e, fr.getType(), g);
                if (f) {
                    fr.set_edge(e);
                }
            }
        }


    }


    /**
     * this method converts the edge we want to test for pokemon to source and destination keys
     * the method returns true if and only if this is the edge of the pokemon
     * @param p geo_location of pokemon
     * @param e edge_data edge
     * @param type int type of the pokemon
     * @param g directed_weighted_graph the graph of the scenario
     * @return boolean
     */
    private static boolean isOnEdge(geo_location p, edge_data e, int type, directed_weighted_graph g) {
        int src = g.getNode(e.getSrc()).getKey();
        int dest = g.getNode(e.getDest()).getKey();
        if (type < 0 && dest > src) {
            return false;
        }
        if (type > 0 && src > dest) {
            return false;
        }
        return isOnEdge(p, src, dest, g);
    }

    /**
     * this method converts the keys of the nodes to locations of the nodes
     * the method returns true if and only if this is the edge of the node
     * @param p geo_location of the pokemon
     * @param s int id of the source node
     * @param d int id of the destination node
     * @param g directed_weighted_graph graph of the scenario
     * @return boolean
     */
    private static boolean isOnEdge(geo_location p, int s, int d, directed_weighted_graph g) {
        geo_location src = g.getNode(s).getLocation();
        geo_location dest = g.getNode(d).getLocation();
        return isOnEdge(p, src, dest);
    }

    /**
     * this is the method that makes the logical check for the edge
     * this method returns true if and only if this is the edge of the pokemon
     * @param p geo_location of the pokemon
     * @param src geo_location of the source node of the edge
     * @param dest geo_location of the destination node of the edge
     * @return boolean
     */
    private static boolean isOnEdge(geo_location p, geo_location src, geo_location dest) {

        boolean ans = false;
        double dist = src.distance(dest);
        double d1 = src.distance(p) + p.distance(dest);
        if (dist > d1 - EPS) {
            ans = true;
        }
        return ans;
    }

    /**
     * this method
     * @param g
     * @return
     */
    private static Range2D GraphRange(directed_weighted_graph g) {
        Iterator<node_data> itr = g.getV().iterator();
        double x0 = 0, x1 = 0, y0 = 0, y1 = 0;
        boolean first = true;
        while (itr.hasNext()) {
            geo_location p = itr.next().getLocation();
            if (first) {
                x0 = p.x();
                x1 = x0;
                y0 = p.y();
                y1 = y0;
                first = false;
            } else {
                if (p.x() < x0) {
                    x0 = p.x();
                }
                if (p.x() > x1) {
                    x1 = p.x();
                }
                if (p.y() < y0) {
                    y0 = p.y();
                }
                if (p.y() > y1) {
                    y1 = p.y();
                }
            }
        }
        Range xr = new Range(x0, x1);
        Range yr = new Range(y0, y1);
        return new Range2D(xr, yr);
    }

    /**
     * this method returns and updated status of the graph
     * @param g directed_weighted_graph
     * @param frame Range2D current frame
     * @return Range2Range
     */
    public static Range2Range w2f(directed_weighted_graph g, Range2D frame) {
        Range2D world = GraphRange(g);
        Range2Range ans = new Range2Range(world, frame);
        return ans;
    }









    /**
     * this method updates for each agent a pokemon target to chase after
     */
    public void AgentNextUpdate() {
        matchPokemonToAgent();
        UpdateSteps();
    }


    /**
     * Updates steps for each agent towards the given fruit using the graph theory algorithm at the api directory
     *
     */
    private void UpdateSteps() {
        DWGraph_Algo algo = new DWGraph_Algo();
        algo.init(this.graph);
        for (CL_Agent agent : _agents) {
            if (agent.get_curr_fruit() != null) {
                int src = agent.getSrcNode();
                CL_Pokemon pokemon = agent.get_curr_fruit();
                edge_data edge = pokemon.get_edge();
                if (edge.getSrc() == src) agent.setNext(edge.getDest());
                else {
                    LinkedList<node_data> l1 = (LinkedList<node_data>) algo.shortestPath(agent.getSrcNode(), edge.getSrc());
                    if (l1 != null) agent.setNext(l1.get(1).getKey());
                }
            }


        }


    }

    /**
     * match for each pokemon the best target possible
     */
    private void matchPokemonToAgent() {
        PriorityQueue<match> q = MatchmakingQueue();
        int i = 0;
        while (q.size() > 0 && i < this._agents.size()) {
            match m = q.poll();
            CL_Agent agent = m.getAgent();
            CL_Pokemon pokemon = m.getPokemon();
            if (agent.get_curr_fruit() == null && pokemon.getKeyAgent_id() == -1) {
                agent.set_curr_fruit(pokemon);
                pokemon.setKeyAgent_id(agent.getID());
                i++;
            }
        }
    }


    /**
     * @return PriorityQueue <match> of matchmaking sorted by Wage
     * wage = value/(time for getting the pokemon)
     */
    private PriorityQueue<match> MatchmakingQueue() {
        PriorityQueue<match> q = new PriorityQueue<match>(new match(0));
        for (CL_Agent agent : _agents) {
            for (CL_Pokemon pokemon : _pokemons) {
                pokemon.setKeyAgent_id(-1);
                agent.set_curr_fruit(null);
                match m = new match(agent, pokemon, graph);
                q.add(m);
            }

        }


        return q;


    }

    /**
     * if the graph isn't strongly conncted this method calculates a collection with 'first' node of each connectivity component
     * rational - if a node is at a component and we put agent there - the agent will never get out of the strong connectivity component
     * @param graph directed_weighted_graph graph of the game
     * @return LinkedList<Integer> list of keys of nodes in differnet strong connectivity components
     */
    private static LinkedList<Integer> DisassemblyIntoComponents(directed_weighted_graph graph) {
        DWGraph_Algo algo = new DWGraph_Algo();
        algo.init(graph);
        LinkedList<Integer> link = new LinkedList<Integer>();
        Iterator<node_data> iterator = graph.getV().iterator();
        node_data node = iterator.next();

        link.add(node.getKey());
        for (node_data node2 : graph.getV()) {
            boolean search = true;
            for (int i = 0; i < link.size() && search; i++) {
                if (algo.shortestPathDist(link.get(i), node2.getKey()) != -1 && algo.shortestPathDist(node2.getKey(),link.get(i)) != -1) search = false;
            }
            if (search) link.add(node2.getKey());
        }
        return link;
    }

    /**
     * if the graph is not strongly connected, we need to send agents as much as Connectivity components we can
     * this method makes sure that every component has at least one agent in it
     * @param rs int number of agents
     * @param game game_service scenario of the game
     * @param graph directed_weighted_graph graph of the game
     */
    public static void DisassemblyIntoComponents(int rs, game_service game, directed_weighted_graph graph) {
        LinkedList<Integer> link = DisassemblyIntoComponents(graph);
        for (int i = 0; i < rs; i++) {
            int j=i%link.size();
            game.addAgent(link.get(j));

        }
        return;
    }

    /**
     * this method is a duplicate of the 'init' method in Ex2 class, but its made for testing the initializing -
     * beacuse we don't have access to the levels of the game at the server, so we can't be sure if we're managing the simulation on the right way
     * @param pokemons ArrayList<CL_Pokemon>
     * @param agentSize int number of agents
     * @return List<Integer> keys of nodes to put agents in for start
     */
    public LinkedList<Integer> start(ArrayList<CL_Pokemon> pokemons, int agentSize) {
        LinkedList<Integer> agentStart = start1(pokemons, agentSize);
        int size = agentStart.size();
        if (agentStart.size() < agentSize) {
            Iterator<node_data> iterator = this.getGraph().getV().iterator();
            node_data node = iterator.next();
            int src = node.getKey();
            for (int a = size; a < agentSize; a++) {
                agentStart.add(src);
            }
        }
        return agentStart;
    }

    /**
     * help method for start method, made for testing as explained at start
     * this method actually place the agents
     * @param pokemons ArrayList<CL_Pokemon>
     * @param agentSize int
     * @return LinkedList<Integer> of keys of nodes which leading to a pokemon (sorted by values of pokemons)
     */
    public LinkedList<Integer> start1(ArrayList<CL_Pokemon> pokemons, int agentSize) {

        PriorityQueue<CL_Pokemon> priorityQueue = new PriorityQueue<CL_Pokemon>(new Comparator<CL_Pokemon>() {
            @Override
            public int compare(CL_Pokemon o1, CL_Pokemon o2) {
                if (o1.getValue() < o2.getValue()) return 1;
                if (o1.getValue() > o2.getValue()) return -1;
                return 0;
            }
        });



        for (int i = 0; i < pokemons.size(); i++) {
            priorityQueue.add(pokemons.get(i));
        }
        LinkedList<Integer> agentStart = new LinkedList<Integer>();
        for (int i = 0; i < agentSize; i++) {
            if (priorityQueue.isEmpty()) return agentStart;
            CL_Pokemon c = priorityQueue.poll();
            int start = c.get_edge().getSrc();
            agentStart.add(start);
        }
        return agentStart;
    }


    /**
     * same as first disassemblyintocomponents , but for testing
     * @param rs number of agents allowed
     * @param graph directed_weighted_graph graph of the game
     * @return LinkedList<Integer>keys of nodes in different connectivity components
     */
    public static LinkedList<Integer> DisassemblyIntoComponents(int rs, directed_weighted_graph graph) {
        LinkedList<Integer> ans = new LinkedList<Integer>();
        LinkedList<Integer> link = DisassemblyIntoComponents(graph);
        for (int i = 0; i < rs; i++) {
            if (i < link.size()) {
                ans.add(link.get(i));
            } else {
                int j=i%link.size();
                ans.add(link.get(j));
            }
        }
        return ans;
    }





}

