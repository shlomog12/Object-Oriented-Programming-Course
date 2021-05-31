package ex1;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class WGraph_DSTest {

    @Test
    void getNode() {
        WGraph_DS g = buildGraph20();
        node_info n1 = g.getNode(0);
        assertTrue(n1.getKey() == 0, "Error in getNode function: The function does not return a node with the requested key");
        assertTrue(n1.getInfo().equals("v0"), "Error in the getNode function: The function does not return a node with the requested value");
        node_info n2 = g.getNode(21);
        assertTrue(n2 == null, "Error in getNode function: The function returns a value other than null for a key that does not exist in the graph");
    }

    @Test
    void hasEdge() {
        WGraph_DS g = new WGraph_DS();
        assertTrue(!g.hasEdge(0, 2), "Error in hasEdge function: The function returns true for a side between two vertices that do not exist in the graph");
        g = buildGraph20();
        assertTrue(g.hasEdge(2, 5));
        assertTrue(!g.hasEdge(3, 4), "Error in hasEdge function: The function returns true for a non-existent side");
        assertTrue(!g.hasEdge(25, 4), "Error in hasEdge function: The function returns true for a side between two vertices when one of them does not exist in the graph");
    }

    @Test
    void getEdge() {
        WGraph_DS g = new WGraph_DS();
        g = buildGraph20();
        assertTrue(g.getEdge(20, 0) == 1.3);
        assertTrue(g.getEdge(14, 16) == 1994.26);
        assertTrue(g.getEdge(26, 16) == -1);
        assertTrue(g.getEdge(-5, 0) == -1);

    }



    @Test
    void addNode() {
        WGraph_DS g = new WGraph_DS();
        assertTrue((g.getNode(13) == null), "after");
        g.addNode(13);
        assertTrue(!(g.getNode(13) == null), "before");
    }

    @Test
    void connect() {
        WGraph_DS g = new WGraph_DS();
        for (int i = 0; i < 6; i++) {
            g.addNode(i);
        }
        assertTrue(!g.hasEdge(1, 2));
        g.connect(1, 2, 4.5);
        assertTrue(g.getEdge(1, 2) == 4.5, "Error in connect function: The function does not add a side between the two requested vertices");
        g.connect(1, 2, 4.6);
        assertTrue(g.getEdge(1, 2) == 4.6, "test2");
        g.connect(2, 1, 0.4);
        assertTrue(g.getEdge(1, 2) == 0.4, "test3");
        g.connect(3, 7, 55.5);
        assertTrue(g.getEdge(3, 7) == -1, "test4");
        g.connect(0, 1, -10);
        assertTrue(g.getEdge(1, 0) == -1, "test5");

    }

    @Test
    void getV() {
        WGraph_DS g = new WGraph_DS();
        for (int i = 0; i < 6; i++) {
            g.addNode(i);
        }
        System.out.println(g.getV());
    }

    @Test
    void testGetV() {
        WGraph_DS g = new WGraph_DS();
        for (int i = 0; i < 6; i++) {
            g.addNode(i);
        }
        g.connect(1, 2, 0.5);
        g.connect(1, 0, 0.8);
        g.connect(1, 3, 1.2);
        g.connect(1, 4, 3.6);
        System.out.println(g.getV(1));

    }

    @Test
    void removeNode() {
        WGraph_DS g = buildGraph6();
        assertTrue(g.getNode(0) != null, "test1");
        g.removeNode(0);
        assertTrue(g.getNode(0) == null, "test2");
        g.connect(2, 3, 4);
        g.connect(2, 1, 50);
        g.connect(2, 4, 0.9);
        assertTrue(g.hasEdge(3, 2),"test3");
        int nodeSize = g.nodeSize();
        int edgeSize = g.edgeSize();
        node_info deleted= g.removeNode(2);
        assertTrue(!g.hasEdge(3, 2),"test4");
        assertTrue(g.nodeSize()+1==nodeSize,"nodesize");
        assertTrue(g.edgeSize()+3==edgeSize,"edgesize");

        assertTrue(deleted.getKey()==2);
        assertTrue(deleted.getInfo().equals("v2"));

        node_info deleted2= g.removeNode(30);
        assertTrue(deleted2==null);
    }

    @Test
    void removeEdge() {
        WGraph_DS g = buildGraph6();
        g.connect(0,1,22);
        g.connect(1,5,34);
        assertTrue(g.hasEdge(1,5));
        int edgeSize=g.edgeSize();
        g.removeEdge(1,5);
        assertTrue(!g.hasEdge(1,5));
        assertTrue(edgeSize-1==g.edgeSize());


    }

    @Test
    void nodeSize() {
        WGraph_DS g = buildGraph20();
        int nodeSize = g.nodeSize();
        g.removeNode(30);
        assertTrue(nodeSize==g.nodeSize());
        g.removeNode(2);
        assertTrue(nodeSize-1==g.nodeSize());
        g.addNode(3);
        assertTrue(nodeSize-1==g.nodeSize());
        g.addNode(22);
        assertTrue(nodeSize==g.nodeSize());

    }

    @Test
    void edgeSize() {
        WGraph_DS g = buildGraph20();
        int edgeSize = g.edgeSize();
        g.connect(2,10,1.2);
        assertTrue(edgeSize+1==g.edgeSize());
        g.removeEdge(0,3);
        assertTrue(edgeSize==g.edgeSize());
        g.removeEdge(0,50);
        assertTrue(edgeSize==g.edgeSize());
        int neighbors_6 =g.getV(6).size();
        g.removeNode(6);
        assertTrue(edgeSize-neighbors_6==g.edgeSize());
    }

    @Test
    void getMC() {
        WGraph_DS g = buildGraph6();
        assertTrue(g.getMC()==6);
        g.connect(1,2,4.5);
        assertTrue(g.getMC()==7);
        g.connect(1,2,4.5);
        assertTrue(g.getMC()==7);
        g.connect(30,4,6.7);
        assertTrue(g.getMC()==7);
        g.removeEdge(1,2);
        assertTrue(g.getMC()==8);
        g.removeEdge(1,2);
        assertTrue(g.getMC()==8);
        g.removeEdge(30,4);
        assertTrue(g.getMC()==8);
        g.connect(0,1,0.1);
        g.connect(0,2,0.2);
        g.connect(0,3,0.3);
        g.connect(0,4,0.4);
        assertTrue(g.getMC()==12);
        g.removeNode(0);
        assertTrue(g.getMC()==17);

    }



    public static WGraph_DS buildGraph20() {
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
        g1.connect(18, 5, 3.2);

//        g1.removeEdge(17, 19);
//        g1.connect(17, 19);


        return g1;

    }
    public static WGraph_DS buildGraph6(){
        WGraph_DS g1 = new WGraph_DS();
        for (int i = 0; i < 6; i++) {
            g1.addNode(i);
        }
        return g1;
    }

}