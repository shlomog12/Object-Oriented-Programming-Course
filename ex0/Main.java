package ex0;

public class Main {


    public static Graph_DS build20() {
        node_data n0 = new NodeData("c0");
        node_data n1 = new NodeData("c1");
        node_data n2 = new NodeData("c2");
        node_data n3 = new NodeData("c3");
        node_data n4 = new NodeData("c4");
        node_data n5 = new NodeData("c5");
        node_data n6 = new NodeData("c6");
        node_data n7 = new NodeData("c7");
        node_data n8 = new NodeData("c8");
        node_data n9 = new NodeData("c9");
        node_data n10 = new NodeData("c10");
        node_data n11 = new NodeData("c11");
        node_data n12 = new NodeData("c12");
        node_data n13 = new NodeData("c13");
        node_data n14 = new NodeData("c14");
        node_data n15 = new NodeData("c15");
        node_data n16 = new NodeData("c16");
        node_data n17 = new NodeData("c17");
        node_data n18 = new NodeData("c18");
        node_data n19 = new NodeData("c19");
        node_data n20 = new NodeData("c20");

        Graph_DS g1 = new Graph_DS();
        g1.addNode(n0);
        g1.addNode(n1);
        g1.addNode(n2);
        g1.addNode(n3);
        g1.addNode(n4);
        g1.addNode(n5);
        g1.addNode(n6);
        g1.addNode(n7);
        g1.addNode(n8);
        g1.addNode(n9);
        g1.addNode(n10);
        g1.addNode(n11);
        g1.addNode(n12);
        g1.addNode(n13);
        g1.addNode(n14);
        g1.addNode(n15);
        g1.addNode(n16);
        g1.addNode(n17);
        g1.addNode(n18);
        g1.addNode(n19);
        g1.addNode(n20);


        g1.connect(0, 1);
        g1.connect(0, 20);
        g1.connect(0, 3);
        g1.connect(0, 2);
        g1.connect(1, 4);
        g1.connect(1, 10);
        g1.connect(1, 9);
        g1.connect(2, 6);
        g1.connect(2, 5);
        g1.connect(3, 5);
        g1.connect(6, 10);
        g1.connect(6, 9);
        g1.connect(6, 7);
        g1.connect(7, 15);
        g1.connect(9, 11);
        g1.connect(11, 13);
        g1.connect(12, 17);
        g1.connect(13, 14);
        g1.connect(14, 15);
        g1.connect(14, 16);
        g1.connect(17, 19);


        g1.connect(8, 12);
        g1.connect(8, 5);
        g1.connect(18, 5);

//        g1.removeEdge(17, 19);
//        g1.connect(17, 19);
        return g1;

    }


    public static void main(String[] args) {

        Graph_DS g1 = new Graph_DS();
        g1=build20();
        Graph_Algo al=new Graph_Algo();
        al.init(g1);

        Graph_DS g2= (Graph_DS) al.copy();


//        g1.removeNode(0);

        g1.printALL();
        System.out.println("*****************************************");
        g2.printALL();


//        g2 = (Graph_DS) al.copy();
//        g2.printALL();





//        System.out.println("dddd");
//        Graph_DS g1 = new Graph_DS();
//        g1=build20();
//
//
////        System.out.println(g1.getNode(30));
//        Graph_Algo al=new Graph_Algo();
//        al.init(g1);
//        int s=al.shortestPathDist(20,20);
//        LinkedList L1= new LinkedList();
//
//
////        al.initConnectedSizeAndTag();
//        for (int i =0; i<21 ;i++){
//            System.out.println(i+" TAG= "+g1.getNode(i).getTag());
//
//        }
//
//
//        boolean bo=al.isConnected();
//        L1= (LinkedList) al.shortestPath(4,18);
//        System.out.println(L1);




    }


}
