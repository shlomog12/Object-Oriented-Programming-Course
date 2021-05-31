package gameClient;

import api.*;
import gameClient.util.Point3D;
import org.junit.jupiter.api.Test;

import javax.naming.NamingEnumeration;
import java.util.ArrayList;
import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.assertEquals;


class ArenaTest {
    Arena ar = new Arena();


    @Test
    void updateEdge() {
        DWGraph_DS g = build8();


        ArrayList<CL_Pokemon> pokemons = buildPokimons();


        for (int i = 0; i < pokemons.size(); i++) {
            CL_Pokemon c = pokemons.get(i);
            Arena.updateEdge(c, g);
        }
        ArrayList<edge_data> edges = answerEdge(g);
        for (int i = 0; i < pokemons.size(); i++) {
            edge_data e1 = pokemons.get(i).get_edge();
            edge_data e2 = edges.get(i);
            assertEquals(e1, e2);
        }


    }





    @Test
    void agentNextUpdate() {
        ar = new Arena();
        DWGraph_DS graph = build20();
        ar.setGraph(graph);
        ArrayList<CL_Pokemon> pokemons = buildPokimons();
        pokemons.remove(4);
        for (int i = 0; i < pokemons.size(); i++) {
            Arena.updateEdge(pokemons.get(i), graph);
        }
        ar.setPokemons(pokemons);
        CL_Agent agent1 = new CL_Agent(graph, 0);
        CL_Agent agent2 = new CL_Agent(graph, 1);
        CL_Agent agent3 = new CL_Agent(graph, 2);
        agent1.setSpeed(1);
        agent2.setSpeed(2);
        agent2.setSpeed(4);
        ArrayList<CL_Agent> agents = new ArrayList<CL_Agent>();
        agents.add(agent1);
        agents.add(agent2);
        agents.add(agent3);
        ar.setAgents(agents);
        ar.AgentNextUpdate();
        agent1.setCurrNode(agent1.getNext());
        agent2.setCurrNode(agent2.getNext());
        agent3.setCurrNode(agent3.getNext());
        LinkedList<Integer> ans = ansUpNext();
        assertEquals(agent1.getNext(), ans.get(0));
        assertEquals(agent2.getNext(), ans.get(1));
        assertEquals(agent3.getNext(), ans.get(2));
        ar.AgentNextUpdate();
        agent1.setCurrNode(agent1.getNext());
        agent2.setCurrNode(agent2.getNext());
        agent3.setCurrNode(agent3.getNext());
        assertEquals(agent1.getNext(), ans.get(3));
        assertEquals(agent2.getNext(), ans.get(4));
        assertEquals(agent3.getNext(), ans.get(5));
        ar.AgentNextUpdate();
        assertEquals(agent1.getNext(), ans.get(6));
        assertEquals(agent2.getNext(), ans.get(7));
        assertEquals(agent3.getNext(), ans.get(8));
    }



    @Test
    void start() {

        DWGraph_DS g = build8();
        ar.setGraph(g);
        edgeData edge1 = new edgeData(0, 1, 10);
        edgeData edge2 = new edgeData(1, 2, 10);
        edgeData edge3 = new edgeData(2, 3, 10);
        edgeData edge4 = new edgeData(4, 5, 10);
        edgeData edge5 = new edgeData(7, 2, 10);

        CL_Pokemon p1 = new CL_Pokemon(new Point3D(1, 1, 1), 1, 10, 0, edge1);
        CL_Pokemon p2 = new CL_Pokemon(new Point3D(1, 1, 1), 1, 1.5, 0, edge2);
        CL_Pokemon p3 = new CL_Pokemon(new Point3D(1, 1, 1), 1, 15, 0, edge3);
        CL_Pokemon p4 = new CL_Pokemon(new Point3D(1, 1, 1), 1, 0.5, 0, edge4);
        CL_Pokemon p5 = new CL_Pokemon(new Point3D(1, 1, 1), 1, 30, 0, edge5);
        CL_Pokemon p6 = new CL_Pokemon(new Point3D(1, 1, 1), 1, 2, 0, edge5);
        ArrayList<CL_Pokemon> lp1 = new ArrayList<CL_Pokemon>();
        lp1.add(p1);
        lp1.add(p2);
        lp1.add(p3);
        lp1.add(p4);
        lp1.add(p5);
        lp1.add(p6);
        LinkedList<Integer> l1 = ar.start(lp1, 2);
        ArrayList<Integer> answer1 = answer1();
        assertEquals(l1.size(), answer1.size());
        for (int i = 0; i < l1.size(); i++) {
            assertEquals(l1.get(i), answer1.get(i));
        }
        LinkedList<Integer> l2 = ar.start(lp1, 0);
        assertEquals(l2.size(), 0);
        ArrayList<CL_Pokemon> lp2 = new ArrayList<CL_Pokemon>();
        LinkedList<Integer> l3 = ar.start(lp2, 3);            //Fewer agents than Pokemon
        LinkedList<Integer> l4 = ar.start(lp1, 9);           // Fewer Pokemon than Agents
        assertEquals(l3.size(), 3);
        assertEquals(l4.size(), 9);

        LinkedList<Integer> l5 = ar.start(lp1, 6);
        ArrayList<Integer> answer5 = answer5();
        assertEquals(l5.size(), answer5.size());
        for (int i = 0; i < l5.size(); i++) {
            assertEquals(answer5.get(i), l5.get(i));
        }
    }


    @Test
    void DisassemblyIntoComponents() {
        directed_weighted_graph graph=build3_C();
       LinkedList<Integer> l1= Arena.DisassemblyIntoComponents(7,graph);
       LinkedList<Integer> ans=ansDIC();
       assertEquals(l1.size(),ans.size());
        for (int i = 0; i < ans.size(); i++) {
            assertEquals(ans.get(i),l1.get(i));
        }
    }



    private ArrayList<CL_Pokemon> buildPokimons() {
        CL_Pokemon p1 = new CL_Pokemon(new Point3D(3, 6, 0), 1, 10, 0, null);
        CL_Pokemon p2 = new CL_Pokemon(new Point3D(1, 1, 0), 1, 1.5, 0, null);
        CL_Pokemon p3 = new CL_Pokemon(new Point3D(5, 5, 0), 1, 15, 0, null);
        CL_Pokemon p4 = new CL_Pokemon(new Point3D(0, 5, 0), 1, 10, 0, null);
        CL_Pokemon p5 = new CL_Pokemon(new Point3D(15, 15, 0), 1, 15, 0, null);
        CL_Pokemon p6 = new CL_Pokemon(new Point3D(3, 0, 0), 1, 1.5, 0, null);
        CL_Pokemon p7 = new CL_Pokemon(new Point3D(6, 8, 0), 1, 15, 0, null);
        CL_Pokemon p8 = new CL_Pokemon(new Point3D(5, 7, 0), 1, 15, 0, null);


        ArrayList<CL_Pokemon> pokemons = new ArrayList<CL_Pokemon>();
        pokemons.add(p1);
        pokemons.add(p2);
        pokemons.add(p3);
        pokemons.add(p4);
        pokemons.add(p5);
        pokemons.add(p6);
        pokemons.add(p7);
        pokemons.add(p8);
        return pokemons;
    }

    private ArrayList<edge_data> answerEdge(DWGraph_DS g) {
        ArrayList<edge_data> answerEdge = new ArrayList<edge_data>();
        answerEdge.add(g.getEdge(0, 5));
        answerEdge.add(g.getEdge(1, 2));
        answerEdge.add(g.getEdge(2, 5));
        answerEdge.add(g.getEdge(1, 3));
        answerEdge.add(null);
        answerEdge.add(g.getEdge(1, 4));
        answerEdge.add(g.getEdge(5, 6));
        answerEdge.add(g.getEdge(3, 7));
        return answerEdge;
    }

    private ArrayList<Integer> answer5() {
        ArrayList<Integer> answer = new ArrayList<Integer>();
        answer.add(7);
        answer.add(2);
        answer.add(0);
        answer.add(7);
        answer.add(1);
        answer.add(4);
        return answer;
    }

    private ArrayList<Integer> answer1() {
        ArrayList<Integer> answer = new ArrayList<Integer>();
        answer.add(7);
        answer.add(2);
        return answer;
    }

    private DWGraph_DS build8() {

        nodeDate n0 = new nodeDate(0);
        nodeDate n1 = new nodeDate(1);
        nodeDate n2 = new nodeDate(2);
        nodeDate n3 = new nodeDate(3);
        nodeDate n4 = new nodeDate(4);
        nodeDate n5 = new nodeDate(5);
        nodeDate n6 = new nodeDate(6);
        nodeDate n7 = new nodeDate(7);

        DWGraph_DS g = new DWGraph_DS();
        n0.setLocation(new geoLocation(2, 6, 0));
        n1.setLocation(new geoLocation(0, 0, 0));
        n2.setLocation(new geoLocation(4, 4, 0));
        n3.setLocation(new geoLocation(0, 7, 0));
        n4.setLocation(new geoLocation(7, 0, 0));
        n5.setLocation(new geoLocation(6, 6, 0));
        n6.setLocation(new geoLocation(6, 9, 0));
        n7.setLocation(new geoLocation(10, 7, 0));

        g.addNode(n0);
        g.addNode(n1);
        g.addNode(n2);
        g.addNode(n3);
        g.addNode(n4);
        g.addNode(n5);
        g.addNode(n6);
        g.addNode(n7);

        g.connect(0, 5, 100);
        g.connect(1, 2, 15);
        g.connect(2, 5, 7);
        g.connect(1, 3, 45);
        g.connect(1, 4, 22);
        g.connect(5, 6, 76);
        g.connect(3, 7, 2);


        return g;

    }

    private DWGraph_DS build20() {
        DWGraph_DS g = build8();
        g.addNode(new nodeDate(9));
        g.addNode(new nodeDate(10));
        g.addNode(new nodeDate(11));
        g.addNode(new nodeDate(12));
        g.addNode(new nodeDate(13));
        g.addNode(new nodeDate(14));
        g.addNode(new nodeDate(15));
        g.addNode(new nodeDate(16));
        g.addNode(new nodeDate(17));
        g.addNode(new nodeDate(18));
        g.addNode(new nodeDate(19));
        g.addNode(new nodeDate(20));

        g.connect(5, 11, 1);
        g.connect(4, 13, 22);
        g.connect(0, 1, 2);
        g.connect(1, 2, 100);
        g.connect(5, 6, 34);
        g.connect(4, 0, 22);
        g.connect(5, 7, 1);
        g.connect(17, 19, 442);
        g.connect(15, 4, 0.5);
        g.connect(20, 13, 24);
        g.connect(7, 6, 1.5);
        g.connect(10, 13, 99);
        g.connect(18, 11, 67);
        g.connect(19, 18, 22);
        g.connect(11, 4, 7);
        g.connect(15, 13, 6);

        return g;
    }

    private LinkedList<Integer> ansUpNext() {
        LinkedList<Integer> ans = new LinkedList<Integer>();
        ans.add(1);
        ans.add(3);
        ans.add(5);
        /////
        ans.add(3);
        ans.add(7);
        ans.add(6);
        ///
        ans.add(7);
        ans.add(7);
        ans.add(6);
        return ans;
    }


    private directed_weighted_graph build3_C() {
        directed_weighted_graph graph=new DWGraph_DS();
        for (int i = 0; i <60 ; i++) {
            graph.addNode(new nodeDate(i));
        }
        for (int i = 0; i < 59; i++) {
            graph.connect(i,i+1,10*i);
            graph.connect(i+1,i,i*10);
        }
        graph.removeEdge(19,20);
        graph.removeEdge(20,19);
        graph.removeEdge(39,40);
        graph.removeEdge(40,39);
        return graph;
    }

    private LinkedList<Integer> ansDIC(){
        LinkedList<Integer> ans= new LinkedList<Integer>();
        ans.add(0);
        ans.add(20);
        ans.add(40);
        ans.add(0);
        ans.add(20);
        ans.add(40);
        ans.add(0);

        return ans;
    }

}