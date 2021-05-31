package ex1;

import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class WGraph_AlgoTest {
    final double eps = 0.001;




    @Test
    void init() {
        WGraph_DS g = new WGraph_DS();
        WGraph_Algo algo = new WGraph_Algo();
        algo.init(g);
        assertTrue(algo.getGraph().equals(g));
    }

    @Test
    void getGraph() {
        WGraph_DS g = new WGraph_DS();
        WGraph_Algo algo = new WGraph_Algo();
        algo.init(g);
        assertTrue(algo.getGraph().equals(g));
    }

    @Test
    void copy() {
        WGraph_DS g = buildGraph21();
        WGraph_Algo algo = new WGraph_Algo();
        algo.init(g);
        WGraph_DS g2 = (WGraph_DS) algo.copy();
        int nodSize = g2.nodeSize();
        int edgeSize = g2.edgeSize();
        assertTrue(nodSize == g.nodeSize());
        assertTrue(edgeSize == g.edgeSize());
        assertTrue(g.getMC() == g2.getMC());
        int neighbors_0 = g.getV(0).size();
        g.removeNode(0);
        assertTrue(g2.nodeSize() == g.nodeSize() + 1);
        assertTrue(g2.edgeSize() == g.edgeSize() + neighbors_0);
        assertTrue(g.getMC() == g2.getMC() + neighbors_0 + 1);
    }

    @Test
    void isConnected() {
        WGraph_DS g = new WGraph_DS();
        WGraph_Algo algo = new WGraph_Algo();
        algo.init(g);
        assertTrue(algo.isConnected());
        g.addNode(0);
        assertTrue(algo.isConnected());
        g.addNode(1);
        assertTrue(!algo.isConnected());
        g.connect(0, 1, 0.5);
        assertTrue(algo.isConnected());
        WGraph_DS g2 = buildGraph21();
        algo.init(g2);
        assertTrue(!algo.isConnected());
        g2.connect(18, 5, 3.2);
        assertTrue(algo.isConnected());


    }

    @Test
    void shortestPathDist() {
        WGraph_DS g = buildGraph21();
        WGraph_Algo al = new WGraph_Algo();
        al.init(g);
        assertTrue(al.shortestPathDist(6, 18) == -1);
        g.connect(5, 18, 3.2);
        ArrayList<Double> correctAnswerForBuild20 = correctAnswerForBuild20();
        for (int i = 0; i < 21; i++) {
            double a = al.shortestPathDist(6, i);
            double b = correctAnswerForBuild20.get(i);
            double ans = Math.abs(a - b);
            assertTrue(ans <= eps);
        }
    }

    @Test
    void shortestPath() {
        WGraph_Algo al = new WGraph_Algo();
        WGraph_DS g1 = new WGraph_DS();
        al.init(g1);
        assertTrue(null == al.shortestPath(1, 2));
        WGraph_DS g2 = buildGraph21();
        al.init(g2);
        assertTrue(null == al.shortestPath(1, 35));
        assertTrue(null == al.shortestPath(1, 18));
        assertTrue(tr3_0(al));
        assertTrue(tr6_0(al));
        assertTrue(tr10_6(al));
        assertTrue(tr14_9(al));
        assertTrue(tr14_16(al));
        assertTrue(tr1_1(al));
    }

    @Test
    void save_load() throws FileNotFoundException {

        String file21 = "myGraphTest21";
        WGraph_DS g21 = buildGraph21();
        WGraph_Algo al = new WGraph_Algo();
        al.init(g21);
        boolean test1 = al.save(file21);
        assertTrue(test1);
        assertTrue(al.getGraph().nodeSize() == 21);
        WGraph_DS g0 = new WGraph_DS();
        al.init(g0);
        String file0 = "myGraphTest0";
        boolean test2 = al.save(file0);
        assertTrue(test2);
        assertTrue(al.getGraph().nodeSize() == 0);
        boolean test3 = al.load(file21);
        assertTrue(test3);
        assertTrue(al.getGraph().nodeSize() == 21);
        assertTrue(g21.getMC() == al.getGraph().getMC());
        assertTrue(g21.nodeSize() == al.getGraph().nodeSize());
        assertTrue(g21.edgeSize() == al.getGraph().edgeSize());

        Iterator g21_it = g21.getV().iterator();
        Iterator gLoad_it = al.getGraph().getV().iterator();
        while (g21_it.hasNext()) {
            node_info n21 = (node_info) g21_it.next();
            node_info nLoad = (node_info) gLoad_it.next();
            assertTrue(n21.getInfo().equals(nLoad.getInfo()));
            assertTrue(n21.getTag() == nLoad.getTag());
            assertTrue(n21.getKey() == nLoad.getKey());
            Iterator g21Neighbor_it = g21.getV(n21.getKey()).iterator();
            Iterator gLoadNeighbor_it = al.getGraph().getV(nLoad.getKey()).iterator();
            while (g21Neighbor_it.hasNext()) {
                node_info neighbor_n21 = (node_info) g21Neighbor_it.next();
                node_info neighbor_nLoad = (node_info) gLoadNeighbor_it.next();
                assertTrue(neighbor_n21.getInfo().equals(neighbor_nLoad.getInfo()));
                assertTrue(neighbor_n21.getTag() == neighbor_nLoad.getTag());
                assertTrue(neighbor_n21.getKey() == neighbor_nLoad.getKey());
            }


        }
        boolean test4 = al.load(file0);
        assertTrue(test4);
        assertTrue(al.getGraph().nodeSize() == 0);
        assertTrue(al.getGraph().edgeSize() == 0);

    }





    @Test
    void runTime() throws FileNotFoundException {
        long start10Million = new Date().getTime();

        WGraph_DS g10Million=new WGraph_DS();
        g10Million=build(1000000,10000000);
        long end10Million = new Date().getTime();
        double lengthTime10 = (end10Million-start10Million)/1000.0;
        System.out.println("time for 10 million = "+lengthTime10);
        assertTrue(lengthTime10<70,"a long time for init 10 milion nodes: "+lengthTime10+">60");
        g10Million=null;
        long startMillion = new Date().getTime();
        WGraph_DS gMillion=new WGraph_DS();
        gMillion=build(1000000,1000000);
        WGraph_Algo al=new WGraph_Algo();
        al.init( gMillion);
        WGraph_DS gGet= (WGraph_DS) al.getGraph();
        WGraph_DS gCopy=(WGraph_DS) al.copy();
        boolean bo=al.isConnected();
//        System.out.println("bo = "+bo);
        double path1 =al.shortestPathDist(3,10000);
        double path2 =al.shortestPathDist(55555,86443);
        System.out.println("path1 = "+ path1);
        System.out.println("valuePath2 = "+ path2);
        List<node_info> NodesPath2=al.shortestPath(55555,86443);
        String file="fileTestRanTime";
        al.save(file);
        al.load(file);
        long endMillion = new Date().getTime();
        double lengthTimeMillion = (endMillion-startMillion)/1000.0;
        System.out.println("time for million = "+lengthTimeMillion);
    }




    private static WGraph_DS build(int nodeSize, int edgeSize) {
        WGraph_DS g = new WGraph_DS();
        for (int i = 0; i < nodeSize; i++) {
            g.addNode(i);
        }
        Random rndNeighbor1 = new Random(1);
        Random rndNeighbor2 = new Random(5);
        Random rndWight = new Random(2);

        for (int i = 0; i < edgeSize; i++) {
            int Size = g.edgeSize();
            while (Size == g.edgeSize()){
                double x=rndNeighbor1.nextDouble();
                double y=rndNeighbor2.nextDouble();
                int neighbor1 = (int) (x * nodeSize);
                int neighbor2 = (int) (y * nodeSize);
                double wight = rndWight.nextDouble() * 100;
                g.connect(neighbor1, neighbor2, wight);
            }
        }
        return g;
    }

    public static WGraph_DS buildGraph21() {
        WGraph_DS g1 = new WGraph_DS();

        for (int i = 0; i < 21; i++) {
            g1.addNode(i);
        }


        g1.connect(0, 1, 2.5);
        g1.connect(0, 20, 1.3);
        g1.connect(0, 3, 999999.999);
        g1.connect(0, 2, 74.6);
        g1.connect(1, 4, 0.25);
        g1.connect(1, 10, 33);
        g1.connect(1, 9, 12.89);
        g1.connect(2, 6, 74.6);
        g1.connect(2, 5, 0.1);
        g1.connect(3, 5, 0.9);
        g1.connect(6, 10, 6656);
        g1.connect(6, 9, 9.6);
        g1.connect(6, 7, 23.45);
        g1.connect(7, 15, 85 / 62);
        g1.connect(9, 11, 11 / 9);
        g1.connect(11, 13, 28);
        g1.connect(12, 17, 1.2);
        g1.connect(13, 14, 0.0001);
        g1.connect(14, 15, 34.5);
        g1.connect(14, 16, 1994.26);
        g1.connect(17, 19, 18.2);


        g1.connect(8, 12, 95.25);
        g1.connect(8, 5, 1.6);
//        g1.connect(18, 5, 3.2);

//        g1.removeEdge(17, 19);
//        g1.connect(17, 19);
        return g1;

    }

    public static ArrayList<Double> correctAnswerForBuild20() {
        ArrayList<Double> correctAnswerForBuild20 = new ArrayList<Double>();
        correctAnswerForBuild20.add(24.99);
        correctAnswerForBuild20.add(22.49);
        correctAnswerForBuild20.add(74.6);
        correctAnswerForBuild20.add(75.6);
        correctAnswerForBuild20.add(22.74);
        correctAnswerForBuild20.add(74.7);  //5
        correctAnswerForBuild20.add(0.0);
        correctAnswerForBuild20.add(23.45);
        correctAnswerForBuild20.add(76.3);
        correctAnswerForBuild20.add(9.6);
        correctAnswerForBuild20.add(55.49);  //10
        correctAnswerForBuild20.add(10.6);
        correctAnswerForBuild20.add(171.55);
        correctAnswerForBuild20.add(38.6);
        correctAnswerForBuild20.add(38.6001);
        correctAnswerForBuild20.add(24.45);   //15
        correctAnswerForBuild20.add(2032.8601);
        correctAnswerForBuild20.add(172.75);
        correctAnswerForBuild20.add(77.9);
        correctAnswerForBuild20.add(190.95);
        correctAnswerForBuild20.add(26.29);
        return correctAnswerForBuild20;
    }


    //3->0
    public static ArrayList<Integer> TrackFrom3_to_0() {
        ArrayList<Integer> tr3_0 = new ArrayList<Integer>();
        tr3_0.add(3);
        tr3_0.add(5);
        tr3_0.add(2);
        tr3_0.add(0);
        return tr3_0;
    }

    public static boolean tr3_0(WGraph_Algo al) {
        ArrayList<Integer> tr3_0 = TrackFrom3_to_0();
        List<node_info> stp3_0 = al.shortestPath(3, 0);
        for (int i = 0; i < tr3_0.size(); i++) {
            if (tr3_0.get(i) != stp3_0.get(i).getKey()) return false;
        }
        return true;
    }


    //6->0
    public static ArrayList<Integer> TrackFrom6_to_0() {
        ArrayList<Integer> tr6_0 = new ArrayList<Integer>();
        tr6_0.add(6);
        tr6_0.add(9);
        tr6_0.add(1);
        tr6_0.add(0);
        return tr6_0;
    }

    public static boolean tr6_0(WGraph_Algo al) {
        ArrayList<Integer> tr6_0 = TrackFrom6_to_0();
        List<node_info> stp6_0 = al.shortestPath(6, 0);
        for (int i = 0; i < tr6_0.size(); i++) {
            if (tr6_0.get(i) != stp6_0.get(i).getKey()) return false;
        }
        return true;
    }


    //14->9
    public static ArrayList<Integer> TrackFrom14_to_9() {
        ArrayList<Integer> tr14_9 = new ArrayList<Integer>();
        tr14_9.add(14);
        tr14_9.add(13);
        tr14_9.add(11);
        tr14_9.add(9);
        return tr14_9;
    }

    public static boolean tr14_9(WGraph_Algo al) {
        ArrayList<Integer> tr14_9 = TrackFrom14_to_9();
        List<node_info> stp14_9 = al.shortestPath(14, 9);
        for (int i = 0; i < tr14_9.size(); i++) {
            if (tr14_9.get(i) != stp14_9.get(i).getKey()) return false;
        }
        return true;
    }

    //14->16
    public static ArrayList<Integer> TrackFrom14_to_16() {
        ArrayList<Integer> tr14_16 = new ArrayList<Integer>();
        tr14_16.add(14);
        tr14_16.add(16);
        return tr14_16;
    }

    public static boolean tr14_16(WGraph_Algo al) {
        ArrayList<Integer> tr14_16 = TrackFrom14_to_16();
        List<node_info> stp14_16 = al.shortestPath(14, 16);
        for (int i = 0; i < tr14_16.size(); i++) {
            if (tr14_16.get(i) != stp14_16.get(i).getKey()) return false;
        }
        return true;
    }

    //10->6
    public static ArrayList<Integer> TrackFrom10_to_6() {
        ArrayList<Integer> tr10_6 = new ArrayList<Integer>();
        tr10_6.add(10);
        tr10_6.add(1);
        tr10_6.add(9);
        tr10_6.add(6);
        return tr10_6;
    }

    public static boolean tr10_6(WGraph_Algo al) {
        ArrayList<Integer> tr10_6 = TrackFrom10_to_6();
        List<node_info> stp10_6 = al.shortestPath(10, 6);
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

    public static boolean tr1_1(WGraph_Algo al) {
        ArrayList<Integer> tr1_1 = TrackFrom1_to_1();
        List<node_info> stp1_1 = al.shortestPath(1, 1);
        for (int i = 0; i < tr1_1.size(); i++) {
            if (tr1_1.get(i) != stp1_1.get(i).getKey()) return false;
        }
        return true;
    }

}