

package gameClient;

import Server.Game_Server_Ex2;
import api.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.json.JSONException;
import org.json.JSONObject;

import javax.swing.*;
import java.util.*;

import static gameClient.Arena.DisassemblyIntoComponents;

/**
 *This is the main clss in the game project
 *This class starts a game communicating the serve package , initialize it and run in using a thread
 */
public class Ex2 implements Runnable {
    /*********** Initialization Variables  ***********/
    private static MyFrame _win;
    private static Arena _ar;
    private directed_weighted_graph graph;
    private static int scenario_num;
    private static int id;
    private static int move;
    private static long allTime;


    /**
     * main method, inits a thread and starts it
     * @param args level and id from the command line, see: wiki and readme of the project at github
     */
    public static void main(String[] args) {
        scenario_num = -1;
        id = -1;
        Thread client = new Thread(new Ex2());
        client.start();
        if (args != null) {
            int length = args.length;
            if (length > 0) scenario_num = value(args[0]);
            if (length > 1) id = value(args[1]);
        }

    }

    /**
     * run method of the thread, the method that actually starts a game by getting
     * number of scenario from the user, getting scenario data from the server, initialize the scenario and
     * run it while calling algorithms for navigating the agents
     */
    @Override
    public void run() {
        if (scenario_num == -1) scenario_num = choseNumber("Enter a number game [0,23]");
        game_service game = Game_Server_Ex2.getServer(scenario_num); // you have [0,23] games
        if (game == null) {
            JOptionPane.showMessageDialog(null, "The selected step does not exist thanks and goodbye");
            System.exit(0);
        }
        if (id == -1) id = choseNumber("Enter your ID number");
        game.login(id);

        String string_graph = game.getGraph();
        this.graph = getGraph(string_graph);
        init(game);
        game.startGame();
        allTime = game.timeToEnd();
        _win.setTitle("Game number: " + scenario_num + " id:" + id);
        long dt = 100;
        while (game.isRunning()) {
            _ar.setTime(game.timeToEnd());
            dt = moveAgants(game, graph);
            this.move++;
            _ar.setMove(move);
            try {
                Thread.sleep(dt);
                _win.repaint();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        String res = game.toString();
        System.out.println(res);
        System.exit(0);
    }

    /**
     * initializing method for the scenario - placing agents in good places
     * @param game scenario from the service
     */
    private void init(game_service game) {
        String string_pokemons = game.getPokemons();
        _ar = new Arena();
        if (this.graph.getV().size() == 0) System.exit(0);
        _ar.setGraph(this.graph);
        _ar.setPokemons(Arena.json2Pokemons(string_pokemons));
        _win = new MyFrame("test Ex2");
        _win.setSize(1000, 700);
        _win.update(_ar);
        _win.setVisible(true);       //  show
        String info = game.toString();
        JSONObject line;
        try {
            line = new JSONObject(info);
            JSONObject ttt = line.getJSONObject("GameServer");
            int rs = ttt.getInt("agents");
            ArrayList<CL_Pokemon> cl_fs = Arena.json2Pokemons(game.getPokemons());
            for (int a = 0; a < cl_fs.size(); a++) {
                Arena.updateEdge(cl_fs.get(a), this.graph);
            }
            DWGraph_Algo algo = new DWGraph_Algo();
            algo.init(_ar.getGraph());
            if (!algo.isConnected()) DisassemblyIntoComponents(rs, game, _ar.getGraph());
            else {
                LinkedList<Integer> agentStart = _ar.start(cl_fs, rs);
                for (int start : agentStart) {
                    game.addAgent(start);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method deserialize a graph from a JSON String using dwJsonDeserializer (package api)
     * @param str JSON String
     * @return directed_weighted_graph
     */
    public static directed_weighted_graph getGraph(String str) {
        try {
            GsonBuilder builder = new GsonBuilder();
            builder.registerTypeAdapter(directed_weighted_graph.class, new dwJsonDeserializer());
            Gson gson = builder.create();
            directed_weighted_graph ans = gson.fromJson(str, directed_weighted_graph.class);
            return ans;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    /**
     * This is the main logical method - gets the graph of the scenario and a scenario
     * and applies update each agent's next move and calculates the right sleeping time for the game thread -
     * getting low amount of steps but high score
     * the time for sleep returned to the run method
     * @param game the scenario
     * @param gg the graph
     * @return long
     */
    private static long moveAgants(game_service game, directed_weighted_graph gg) {
        String lg = game.move();
        ArrayList<CL_Agent> log = (ArrayList) Arena.getAgents(lg, gg);
        _ar.setAgents(log);
        String fs = game.getPokemons();
        List<CL_Pokemon> ffs = Arena.json2Pokemons(fs);
        _ar.setPokemons(ffs);
        for (CL_Pokemon pokemon : ffs) {
            Arena.updateEdge(pokemon, gg);
        }
        _ar.AgentNextUpdate();
        double timeOfAll = 0;
        boolean onEdgeOfPokimon = false;
        for (int i = 0; i < log.size(); i++) {
            CL_Agent agent = log.get(i);
            double W = gg.getEdge(agent.getSrcNode(), agent.getNext()).getWeight();
            double time = W / agent.getSpeed();
            timeOfAll += time;
            if (agent.getNext() != -1) {
                if (agent.getNext() == agent.get_curr_fruit().get_edge().getDest()) onEdgeOfPokimon = true;
            }
            game.chooseNextEdge(agent.getID(), agent.getNext());
        }
        timeOfAll *= 250 / log.size();
        long timePassed =( allTime - game.timeToEnd())/102;
        if (timePassed < move) {
            timeOfAll += 130;
        }
        timeOfAll = (!onEdgeOfPokimon) ? timeOfAll : timeOfAll / 2.5;
        return (long) timeOfAll;
    }

    /**
     * method for chosing number of scenario
     * @param message String with the number of scenario
     * @return int
     */
    private static int choseNumber(String message) {
        boolean flag = true;
        String num_game = "";
        while (flag) {
            num_game = JOptionPane.showInputDialog(null, message);
            if (num_game == null) {
                JOptionPane.showMessageDialog(null, "No number selected,\n thanks and goodbye");
                System.exit(0);
            }
            JOptionPane.showMessageDialog(null, "The number selected is:\n                     " + num_game);
            flag = false;

            int num = value(num_game);
            if (num == -1) flag = true;
        }
        int num = Integer.valueOf(num_game);
        return num;
    }


    private static int value(String str) {
        if (str == null) return -1;
        if (str == "" || str.isBlank()) return -1;
        for (int i = 0; i < str.length(); i++) {
            char cur = str.charAt(i);
            if (cur > '9' || cur < '0') return -1;
        }
        return Integer.valueOf(str);
    }

}



































