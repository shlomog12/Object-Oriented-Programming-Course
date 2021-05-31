package api;

import org.junit.jupiter.api.Test;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

class DWGraph_DSTest {

    @Test
    void getNode() {
        DWGraph_DS g = build23();
        node_data n1 = g.getNode(0);
        assertTrue(n1.getKey() == 0, "Error in getNode function: The function does not return a node with the requested key");
        assertTrue(n1.getInfo().equals("v0"), "Error in getNode function: The function does not return a node with the requested key");
        node_data n2 = g.getNode(25);
        assertTrue(n2 == null, "Error in getNode function: The function returns a value other than null for a key that does not exist in the graph");

    }




    @Test
    void getEdge() {
        DWGraph_DS g = build23();
        edge_data e1 = g.getEdge(20, 0);
        assertEquals(e1, null);
        edge_data e2 = g.getEdge(20, 23);
        assertEquals(e2.getWeight(), 150);
        edge_data e3 = g.getEdge(23, 20);
        assertEquals(e3, null);
        edge_data e4 = g.getEdge(13, 18);
        assertEquals(e4.getWeight(), 20);
        assertEquals(e4.getDest(), 18);
        assertEquals(e4.getSrc(), 13);
    }

    @Test
    void addNode() {
        DWGraph_DS g = new DWGraph_DS();
        assertEquals(g.getNode(13), null, "before");
        g.addNode(new nodeDate(13));
        assertNotEquals(g.getNode(13), null, "after");
    }

    @Test
    void connect() {
        DWGraph_DS g = buildGraph6();
        assertEquals(g.getEdge(1, 2), null);
        g.connect(1, 2, 4.5);
        assertEquals(g.getEdge(1, 2).getWeight(), 4.5, "Error in connect function: The function does not add a side between the two requested vertices");
        g.connect(1, 2, 4.6);
        assertEquals(g.getEdge(1, 2).getWeight(), 4.6, "test2");
        g.connect(2, 1, 0.4);
        assertNotEquals(g.getEdge(1, 2).getWeight(), 0.4, "test3");
        g.connect(3, 7, 55.5);
        assertEquals(g.getEdge(7, 3), null, "test4");

    }

    @Test
    void getV() {
        DWGraph_DS g = buildGraph6();
        Collection<node_data> c = g.getV();
        for (int i = 0; i < 6; i++) {
            assertTrue(c.contains(g.getNode(i)));
        }
        node_data n = new nodeDate(7);
        assertFalse(c.contains(n));
    }

    @Test
    void getE() {
        DWGraph_DS g = buildGraph6();
        g.connect(0, 1, 0.8);
        g.connect(0, 2, 0.5);
        g.connect(0, 3, 1.2);
        g.connect(0, 4, 3.6);
        int i = 1;
        for (edge_data edge : g.getE(0)) {
            assertEquals(edge, g.getEdge(0, i));
            i++;
        }


    }

    @Test
    void removeNode() {
        DWGraph_DS g = buildGraph6();
        assertNotEquals(g.getNode(0), null, "test1");
        g.removeNode(0);
        assertEquals(g.getNode(0), null, "test2");
        g.connect(2, 3, 4);
        g.connect(2, 1, 50);
        g.connect(2, 4, 0.9);
        assertNotEquals(g.getEdge(2, 3), null, "test3");
        int nodeSize = g.nodeSize();
        int edgeSize = g.edgeSize();
        node_data deleted = g.removeNode(2);
        assertEquals(g.getEdge(2, 3), null, "test4");
        assertEquals(g.nodeSize() + 1, nodeSize, "nodesize");
        assertEquals(g.edgeSize() + 3, edgeSize, "edgesize");
        assertEquals(deleted.getKey(), 2);
        assertEquals(deleted.getInfo(), "v2");
        node_data deleted2 = g.removeNode(30);
        assertEquals(deleted2, null);
    }

    @Test
    void removeEdge() {
            DWGraph_DS g = buildGraph6();
            g.connect(0,1,22);
            g.connect(1,5,34);
            assertNotEquals(g.getEdge(1,5),null);
            int edgeSize=g.edgeSize();
            g.removeEdge(1,5);
            assertEquals(g.getEdge(1,5),null);
            assertEquals(edgeSize-1,g.edgeSize());
    }


    @Test
    void nodeSize() {
            DWGraph_DS g = build23();
            int nodeSize = g.nodeSize();
            g.removeNode(30);
            assertEquals(nodeSize,g.nodeSize());
            g.removeNode(2);
            assertEquals(nodeSize-1,g.nodeSize());
            g.addNode(new nodeDate(3));
            assertEquals(nodeSize-1,g.nodeSize());
            g.addNode(new nodeDate(25));
            assertEquals(nodeSize,g.nodeSize());
    }

    @Test
    void edgeSize() {
            DWGraph_DS g = build23();
            int edgeSize = g.edgeSize();
            g.connect(2,10,1.2);
            assertEquals(edgeSize+1,g.edgeSize());
            g.removeEdge(0,6);
            assertEquals(edgeSize,g.edgeSize());
            g.removeEdge(0,50);
            assertEquals(edgeSize,g.edgeSize());
            g.removeNode(8);
            assertEquals(edgeSize-8,g.edgeSize());
    }

    @Test
    void getMC() {
            DWGraph_DS g = buildGraph6();
            assertEquals(g.getMC(),6);
            g.connect(1,2,4.5);
            assertEquals(g.getMC(),7);
            g.connect(1,2,4.5);
            assertEquals(g.getMC(),7);
            g.connect(30,4,6.7);
            assertEquals(g.getMC(),7);
            g.removeEdge(1,2);
            assertEquals(g.getMC(),8);
            g.removeEdge(1,2);
            assertEquals(g.getMC(),8);
            g.removeEdge(30,4);
            assertEquals(g.getMC(),8);
            g.connect(0,1,0.1);
            g.connect(0,2,0.2);
            g.connect(0,3,0.3);
            g.connect(0,4,0.4);
            assertEquals(g.getMC(),12);
            g.removeNode(0);
            assertEquals(g.getMC(),17);
    }




    private DWGraph_DS buildGraph6() {
        DWGraph_DS graph6 = new DWGraph_DS();
        for (int i = 0; i < 6; i++) {
            graph6.addNode(new nodeDate(i));
        }
        return graph6;
    }



    public static DWGraph_DS build23() {
        DWGraph_DS g1 = new DWGraph_DS();

        for (int i = 0; i < 24; i++) {
            g1.addNode(new nodeDate(i));
        }


        g1.connect(0, 6, 3.4);
        g1.connect(1, 2, 12.1);
        g1.connect(2, 18, 180);
        g1.connect(3, 5, 42);
        g1.connect(3, 4, 55);
        g1.connect(3, 14, 83);
        g1.connect(4, 17, 82);
        g1.connect(5, 8, 27);
        g1.connect(6, 7, 5.8);
        g1.connect(7, 0, 7.1);
        g1.connect(7, 9, 10);
        g1.connect(8, 9, 13.1);
        g1.connect(8, 10, 0.01);
        g1.connect(8, 3, 61);
        g1.connect(8, 14, 2.8);
        g1.connect(8, 0, 9.999);
        g1.connect(9, 10, 4.2);
        g1.connect(10, 1, 12.3);
        g1.connect(10, 5, 29);
        g1.connect(10, 8, 10.5);
        g1.connect(11, 1, 14.5);
        g1.connect(11, 12, 300);
        g1.connect(11, 10, 2.3);
        g1.connect(12, 13, 6.2);
        g1.connect(12, 16, 74);
        g1.connect(13, 11, 1.3);
        g1.connect(13, 18, 20);
        g1.connect(13, 19, 99);
        g1.connect(14, 8, 2.7);
        g1.connect(14, 15, 42.5);
        g1.connect(15, 20, 104.55);
        g1.connect(16, 10, 13.6);
        g1.connect(17, 2, 5 / 4);
        g1.connect(17, 4, 73.465);
        g1.connect(18, 1, 33);
        g1.connect(18, 21, 52);
        g1.connect(19, 12, 102);
        g1.connect(20, 16, 0.001);
        g1.connect(20, 23, 150);


        g1.connect(21, 13, 64.2);
        g1.connect(21, 19, 15);
        g1.connect(21, 22, 25);
        g1.connect(22, 2, 77);
        g1.connect(23, 21, 12.5);

        return g1;

    }
}