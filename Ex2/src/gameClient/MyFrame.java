
package gameClient;

import api.directed_weighted_graph;
import api.edge_data;
import api.geo_location;
import api.node_data;
import gameClient.util.Point3D;
import gameClient.util.Range;
import gameClient.util.Range2D;

import javax.swing.*;
import java.awt.*;
import java.util.Iterator;
import java.util.List;

/**
 * This class represents a display GUI class
 * Of a game on a graph uses a panel from the MyPanel class
 * @author Shlomo Glick
 * @author Gilad Shotland
 *
 */
public class MyFrame extends JFrame { ;
    myPanel mypanel;


    MyFrame(String a) {
        super(a);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        initPanel();
    }

    public void update(Arena ar) {
        this.mypanel.update(ar);
    }



    public void paint(Graphics g) {
        mypanel.repaint();
    }


    private void initPanel() {
        this.mypanel = new myPanel();
        this.add(this.mypanel);

    }

}