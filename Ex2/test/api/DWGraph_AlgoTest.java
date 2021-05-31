package api;


import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import static api.DWGraph_DSTest.build23;
import static org.junit.jupiter.api.Assertions.*;


class DWGraph_AlgoTest {
    final double EPS = 0.000001;


    @org.junit.jupiter.api.Test
    void init_getGraph() {
        DWGraph_DS g = new DWGraph_DS();
        DWGraph_Algo algo = new DWGraph_Algo();
        algo.init(g);
        assertEquals(algo.getGraph(), g);
    }


    @org.junit.jupiter.api.Test
    void copy() {
        DWGraph_DS g = build23();
        DWGraph_Algo algo = new DWGraph_Algo();
        algo.init(g);
        DWGraph_DS g2 = (DWGraph_DS) algo.copy();
        int nodSize = g2.nodeSize();
        int edgeSize = g2.edgeSize();
        assertEquals(nodSize, g.nodeSize());
        assertEquals(edgeSize, g.edgeSize());
        assertEquals(g.getMC(), g2.getMC());
        g.removeNode(0);
        assertEquals(g2.nodeSize(), g.nodeSize() + 1);
        assertEquals(g2.edgeSize(), g.edgeSize() + 3);
        assertEquals(g.getMC(), g2.getMC() + 4);
    }

    @org.junit.jupiter.api.Test
    void isConnected() {
        DWGraph_DS g1 = build23();
        DWGraph_Algo al = new DWGraph_Algo();
        al.init(g1);
        DWGraph_DS g = new DWGraph_DS();
        DWGraph_Algo algo = new DWGraph_Algo();
        algo.init(g);
        assertTrue(algo.isConnected());
        g.addNode(new nodeDate(0));
        assertTrue(algo.isConnected());
        g.addNode(new nodeDate(1));
        assertFalse(algo.isConnected());
        g.connect(0, 1, 0.5);
        assertFalse(algo.isConnected());
        g.connect(1, 0, 0.5);
        assertTrue(algo.isConnected());
        DWGraph_DS g2 = build23();
        algo.init(g2);
        assertTrue(al.isConnected());
        g2.removeEdge(23, 21);
        assertFalse(algo.isConnected());
    }

    @org.junit.jupiter.api.Test
    void shortestPathDist() {
        DWGraph_DS g = build23();
        DWGraph_Algo al = new DWGraph_Algo();
        al.init(g);
        ArrayList<Double> correctAnswer = correctAnswerForBuild23();
        List<node_data> l2 = al.shortestPath(21, 23);
        for (int i = 15; i < 24; i++) {
            double a = al.shortestPathDist(0, i);
            double b = correctAnswer.get(i);
            double ans = Math.abs(a - b);
            assertTrue(ans <= EPS);
        }
        int src = 21;
        int dest = 23;
        double x = al.shortestPathDist(src, dest);
        assertTrue(Math.abs(x - 378.15) < EPS);
        g.addNode(new nodeDate(24));
        assertEquals(al.shortestPathDist(6, 24), -1);
    }

    @org.junit.jupiter.api.Test
    void shortestPath() {
        DWGraph_Algo algo = new DWGraph_Algo();
        DWGraph_DS graph = new DWGraph_DS();
        algo.init(graph);
        assertEquals(null, algo.shortestPath(1, 2));
        DWGraph_DS graph2 = build23();
        algo.init(graph2);
        assertEquals(null, algo.shortestPath(1, 35));
        assertNotEquals(null, algo.shortestPath(1, 18));
        assertTrue(tr3_0(algo));
        assertTrue(tr6_0(algo));
        assertTrue(tr10_6(algo));
        assertTrue(tr14_9(algo));
        assertTrue(tr14_16(algo));
        assertTrue(tr1_1(algo));
    }

    @org.junit.jupiter.api.Test
    void save_load() throws FileNotFoundException {

        String file23 = "myGraphTest21";
        DWGraph_DS g23 = build23();
        DWGraph_Algo algo = new DWGraph_Algo();
        algo.init(g23);
        boolean test1 = algo.save(file23);
        assertTrue(test1);
        assertEquals(algo.getGraph().nodeSize(), 24);
        DWGraph_DS g0 = new DWGraph_DS();
        algo.init(g0);
        String file0 = "myGraphTest0";
        boolean test2 = algo.save(file0);
        assertTrue(test2);
        assertEquals(algo.getGraph().nodeSize(), 0);
        boolean test3 = algo.load(file23);
        assertTrue(test3);
        assertEquals(algo.getGraph().nodeSize(), 24);
        assertEquals(g23.getMC(), algo.getGraph().getMC());
        assertEquals(g23.nodeSize(), algo.getGraph().nodeSize());
        assertEquals(g23.edgeSize(), algo.getGraph().edgeSize());
        Iterator<node_data> g21_it = g23.getV().iterator();
        Iterator gLoad_it = algo.getGraph().getV().iterator();
        while (g21_it.hasNext()) {
            node_data n21 = (node_data) g21_it.next();
            node_data nLoad = (node_data) gLoad_it.next();
            assertEquals(n21.getInfo(), nLoad.getInfo());
            assertEquals(n21.getTag(), nLoad.getTag());
            assertEquals(n21.getKey(), nLoad.getKey());
            Iterator<edge_data> g21Neighbor_it = g23.getE(n21.getKey()).iterator();
            Iterator<edge_data> gLoadNeighbor_it = algo.getGraph().getE(nLoad.getKey()).iterator();
            while (g21Neighbor_it.hasNext()) {
                edge_data neighbor_n23 = g21Neighbor_it.next();
                edge_data neighbor_nLoad = (edge_data) gLoadNeighbor_it.next();
                assertEquals(neighbor_n23.getInfo(), neighbor_nLoad.getInfo());
                assertEquals(neighbor_n23.getTag(), neighbor_nLoad.getTag());
                assertEquals(neighbor_n23.getSrc(), neighbor_nLoad.getSrc());
                assertEquals(neighbor_n23.getDest(), neighbor_nLoad.getDest());
                assertEquals(neighbor_n23.getWeight(), neighbor_nLoad.getWeight());
            }

        }
        boolean test4 = algo.load(file0);
        assertTrue(test4);
        assertEquals(algo.getGraph().nodeSize(), 0);
        assertEquals(algo.getGraph().edgeSize(), 0);

    }


    @Test
    void runTime() throws FileNotFoundException {
        long startMillion = new Date().getTime();
        DWGraph_DS gMillion = new DWGraph_DS();
        gMillion = buildMillion();
        long endMillion = new Date().getTime();
        double lengthTimeM = (endMillion - startMillion) / 1000.0;
        System.out.println("time for 10 million = " + lengthTimeM);

        assertTrue(lengthTimeM < 10, "a long time for init 10 milion nodes: " + lengthTimeM + ">10");
        gMillion = null;
        long start = new Date().getTime();
        DWGraph_DS graph  = new DWGraph_DS();
        graph  = build(100000, 1000000);
        DWGraph_Algo algo = new DWGraph_Algo();
        algo.init(graph);
        DWGraph_DS gGet = (DWGraph_DS) algo.getGraph();
        DWGraph_DS gCopy = (DWGraph_DS) algo.copy();
        boolean bo = algo.isConnected();
//        System.out.println("bo = "+bo);
        double path1 = algo.shortestPathDist(3, 10000);
        double path2 = algo.shortestPathDist(55555, 86443);
//        System.out.println("path1 = " + path1);
//        System.out.println("valuePath2 = " + path2);
        List<node_data> NodesPath2 = algo.shortestPath(55555, 86443);
        String file = "fileTestRanTime";
        algo.save(file);
        algo.load(file);
        long end = new Date().getTime();
        double lengthTime = (end - start) / 1000.0;
        assertTrue(lengthTimeM < 10, "a long time algo  : " + lengthTimeM + ">10");
        System.out.println("time  = " + lengthTime);
    }


    private static DWGraph_DS build(int nodeSize, int edgeSize) {
        DWGraph_DS g = new DWGraph_DS();
        for (int i = 0; i < nodeSize; i++) {
            g.addNode(new nodeDate(i));
        }

        for (int i = 101; i < 100000 && g.edgeSize() <= edgeSize; i++) {
            for (int j = 0; j < 100; j++) {
                g.connect(i, j, 0);
            }
        }
        return g;
    }





    private static DWGraph_DS buildMillion() {
        int nodeSize = (int) Math.pow(10, 5);
        int edgeSize = (int) Math.pow(10, 6);
        DWGraph_DS g = new DWGraph_DS();
        for (int i = 0; i < nodeSize; i++) {
            g.addNode(new nodeDate(i));
        }
        for (int i = 51; g.edgeSize() < edgeSize; i++) {
            for (int j = 0; j < 50; j++) {
                g.connect(j, i, 0.36 * j);
            }
        }


        return g;
    }







    public static ArrayList<Double> correctAnswerForBuild23() {
        ArrayList<Double> correctAnswer = new ArrayList<Double>();


        correctAnswer.add(0.0);
        correctAnswer.add(35.7);
        correctAnswer.add(47.8);
        correctAnswer.add(94.9);
        correctAnswer.add(149.9);
        correctAnswer.add(52.4);    //5
        correctAnswer.add(3.4);
        correctAnswer.add(9.2);
        correctAnswer.add(33.9);
        correctAnswer.add(19.2);
        correctAnswer.add(23.4);  //10
        correctAnswer.add(345.3);
        correctAnswer.add(396.8);
        correctAnswer.add(344.0);
        correctAnswer.add(36.7);
        correctAnswer.add(79.2);   //15
        correctAnswer.add(183.751);
        correctAnswer.add(231.9);
        correctAnswer.add(227.8);
        correctAnswer.add(294.8);
        correctAnswer.add(183.75);  //20
        correctAnswer.add(279.8);
        correctAnswer.add(304.8);
        correctAnswer.add(333.75);
        return correctAnswer;
    }


    //    //3->0
    public static ArrayList<Integer> TrackFrom3_to_0() {
        ArrayList<Integer> tr3_0 = new ArrayList<Integer>();
        tr3_0.add(3);
        tr3_0.add(5);
        tr3_0.add(8);
        tr3_0.add(0);
        return tr3_0;
    }

    public static boolean tr3_0(DWGraph_Algo al) {
        ArrayList<Integer> tr3_0 = TrackFrom3_to_0();
        List<node_data> stp3_0 = al.shortestPath(3, 0);
        for (int i = 0; i < tr3_0.size(); i++) {
            if (tr3_0.get(i) != stp3_0.get(i).getKey()) return false;
        }
        return true;
    }


    //6->0
    public static ArrayList<Integer> TrackFrom6_to_0() {
        ArrayList<Integer> tr6_0 = new ArrayList<Integer>();
        tr6_0.add(6);
        tr6_0.add(7);
        tr6_0.add(0);
        return tr6_0;
    }

    public static boolean tr6_0(DWGraph_Algo al) {
        ArrayList<Integer> tr6_0 = TrackFrom6_to_0();
        List<node_data> stp6_0 = al.shortestPath(6, 0);
        for (int i = 0; i < tr6_0.size(); i++) {
            if (tr6_0.get(i) != stp6_0.get(i).getKey()) return false;
        }
        return true;
    }


    //14->9
    public static ArrayList<Integer> TrackFrom14_to_9() {
        ArrayList<Integer> tr14_9 = new ArrayList<Integer>();
        tr14_9.add(14);
        tr14_9.add(8);
        tr14_9.add(9);
        return tr14_9;
    }

    public static boolean tr14_9(DWGraph_Algo al) {
        ArrayList<Integer> tr14_9 = TrackFrom14_to_9();
        List<node_data> stp14_9 = al.shortestPath(14, 9);
        for (int i = 0; i < tr14_9.size(); i++) {
            if (tr14_9.get(i) != stp14_9.get(i).getKey()) return false;
        }
        return true;
    }

    //14->16
    public static ArrayList<Integer> TrackFrom14_to_16() {
        ArrayList<Integer> tr14_16 = new ArrayList<Integer>();
        tr14_16.add(14);
        tr14_16.add(15);
        tr14_16.add(20);
        tr14_16.add(16);
        return tr14_16;
    }

    public static boolean tr14_16(DWGraph_Algo al) {
        ArrayList<Integer> tr14_16 = TrackFrom14_to_16();
        List<node_data> stp14_16 = al.shortestPath(14, 16);
        for (int i = 0; i < tr14_16.size(); i++) {
            if (tr14_16.get(i) != stp14_16.get(i).getKey()) return false;
        }
        return true;
    }

    //10->6
    public static ArrayList<Integer> TrackFrom10_to_6() {
        ArrayList<Integer> tr10_6 = new ArrayList<Integer>();
        tr10_6.add(10);
        tr10_6.add(8);
        tr10_6.add(0);
        tr10_6.add(6);
        return tr10_6;
    }

    public static boolean tr10_6(DWGraph_Algo al) {
        ArrayList<Integer> tr10_6 = TrackFrom10_to_6();
        List<node_data> stp10_6 = al.shortestPath(10, 6);
        for (int i = 0; i < tr10_6.size(); i++) {
            if (tr10_6.get(i) != stp10_6.get(i).getKey()) return false;
        }
        return true;
    }

    //1->1
    public static ArrayList<Integer> TrackFrom1_to_1() {
        ArrayList<Integer> tr1_1 = new ArrayList<Integer>();
        tr1_1.add(1);
        return tr1_1;
    }

    public static boolean tr1_1(DWGraph_Algo al) {
        ArrayList<Integer> tr1_1 = TrackFrom1_to_1();
        List<node_data> stp1_1 = al.shortestPath(1, 1);
        for (int i = 0; i < tr1_1.size(); i++) {
            if (tr1_1.get(i) != stp1_1.get(i).getKey()) return false;
        }
        return true;
    }




}