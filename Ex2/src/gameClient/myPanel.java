
package gameClient;

import api.*;
import gameClient.util.Point3D;
import gameClient.util.Range;
import gameClient.util.Range2D;
import gameClient.util.Range2Range;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.List;


/**
 * This is the panel of the graphic window where we will draw things on the window,
 * Helps convert points from the order of the graph to the order of the window by the object _w2f
 * And loads the game data from arena object _ar
 * @author Shlomo GLick
 * @author Gilad Shotland
 */

public class myPanel extends JPanel {

    private final ImageIcon pakmen;
    private final ImageIcon pokimon;
    private final ImageIcon upside;
    private gameClient.util.Range2Range _w2f;
    private Arena _ar;



    public myPanel() {

        this.setBackground(Color.white);
        String cwd = new File("").getAbsolutePath();
        cwd += "\\src\\icons\\pak.png";
        this.pakmen = new ImageIcon(cwd);




        String pathFile =  "\\src\\icons\\pok.png";
        cwd = new File("").getAbsolutePath();
        cwd += pathFile;
        this.pokimon = new ImageIcon(cwd);

        pathFile = "\\src\\icons\\pok_upside.png";
        cwd = new File("").getAbsolutePath();
        cwd += pathFile;
        this.upside = new ImageIcon(cwd);
    }






    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        int w = getWidth();
        int h = getHeight();
        g.clearRect(0, 0, w, h);
        updateFrame();
        drawGraph(g);
        drawPokemons(g);
        drawAgants(g);
        drawInfo(g);
    }


    /**
     * Writes at the top of the window,
     * The following data-
     * Time left for the end of the game
     * Speed and value of each agent
     * Total current score
     * Number of moves
     * @param g
     */
    private void drawInfo(Graphics g) {
        int h = this.getSize().height;
        int w = this.getSize().width;
        double min = (w < h) ? w : h;
        double x = w * 3 / 8;
        Font font_Corrent = g.getFont();
        Font font = new Font("font", 1, (int) (min / 11));
        g.setFont(font);
        String time = String.valueOf(_ar.getTime());
        String show = "";
        if(time.length() == 5){
            show  = " TIME: " + time.substring(0,2) +"." +time.substring(2);
        }
        else{
            if(time.length()==4){show = " TIME: " + time.charAt(0)+"."+time.substring(1);}
            else{show = " TIME: " + "0." +time;}
        }

        g.drawString(show , (int) x, 50);
        font = new Font("font", 1, (int) (min / 40));
        g.setFont(font);
        g.setColor(Color.BLACK);
        double totalVal=0;
        for (CL_Agent agent : _ar.getAgents()) {
            double val=agent.getValue();
            edge_data edge=agent.get_curr_edge();
            String Sedge="";
            if (edge!=null) Sedge="edge: ("+edge.getSrc()+","+edge.getDest()+")";
            String Sagent="id: "+agent.getID()+" value: "+val;
            String speed="speed: "+agent.getSpeed();
            String str=Sagent+"  "+Sedge+"  "+speed;
            g.drawString(str, (int) x+15,  65+agent.getID()*15);
            totalVal+=val;
        }
        int a=_ar.getAgents().size();
        g.drawString("Total value:"+totalVal+" move:"+_ar.getMove(), (int) x+15,  65+a*15);
        g.setFont(font_Corrent);

    }


    /**
     * Draws the vertices of the graph
     * @param n node
     * @param r radius
     * @param g Graphics
     */

    private void drawNode(node_data n, int r, Graphics g) {
        geo_location pos = n.getLocation();
        geo_location fp = this._w2f.world2frame(pos);
//        String cwd = new File("").getAbsolutePath();
        g.fillOval((int) fp.x() - r, (int) fp.y() - r, 2 * r, 2 * r);
        g.drawString("" + n.getKey(), (int) fp.x(), (int) fp.y() - 4 * r);
    }


    /**
     * Draws the sides of the graph with one-way arrows from src to dest
     * @param e edge
     * @param g Graphics
     */
    private void drawEdge(edge_data e, Graphics g) {
        directed_weighted_graph gg = _ar.getGraph();
        geo_location s = gg.getNode(e.getSrc()).getLocation();
        geo_location d = gg.getNode(e.getDest()).getLocation();
        geo_location s0 = this._w2f.world2frame(s);
        geo_location d0 = this._w2f.world2frame(d);
        g.setColor(Color.black);
        drawArrowLine(g, (int) s0.x(), (int) s0.y(), (int) d0.x(), (int) d0.y(), 20, 3);
    }


    /**
     * Draws the agents in the form of Pacman in black.
     * When above each of them he will write his value
     * @param g
     */
    private void drawAgants(Graphics g) {
        List<CL_Agent> rs = this.get_ar().getAgents();
        g.setColor(Color.red);
        if (rs == null) return;


        for (int i = 0; i < rs.size(); i++) {
            CL_Agent agent = rs.get(i);
            int id = agent.getID();
            double val = agent.getValue();
            geo_location c = agent.getLocation();
            int r = 8;
            if (c != null) {
                geo_location fp = this.get_w2f().world2frame(c);
//                String cwd = new File("").getAbsolutePath();
//                cwd += "\\src\\icons\\pak.png";
//                ImageIcon pakmen = new ImageIcon(cwd);
                this.pakmen.paintIcon(this, g, (int) fp.x() - r, (int) fp.y() - r);
                Font font = new Font("font", 22, 15);
                font = font.deriveFont(30);
                g.setFont(font);
                g.drawString("" + id + " val:" + val, (int) fp.x(), (int) fp.y() - 4 * r);
            }
        }
    }


    /**
     * Draws the Pokemon in their place on the graph
     * Above each Pokemon write its value
     * In addition to help us discern on which direction of the rib exactly the Pokemon is sitting
     * We will draw the inverted Pokemon when it is on a descending side (src.key> dest.key)
     * And draw it standing straight when it is at an ascending junction (src.key <dest.key)
     * @param g
     */


    private void drawPokemons(Graphics g) {
        List<CL_Pokemon> fs = this.get_ar().getPokemons();
        if (fs == null) return;
        for (CL_Pokemon f : fs) {
            Point3D c = f.getLocation();
            int r = 10;


            if (c != null) {
                geo_location fp = this.get_w2f().world2frame(c);
                if (f.getType() > 0) pokimon.paintIcon(this, g, (int) fp.x() - r, (int) fp.y() - r);
                else upside.paintIcon(this, g, (int) fp.x() - r, (int) fp.y() - r);
                String str="val:"+f.getValue();
                g.setColor(Color.MAGENTA);
                g.drawString(str,(int) fp.x(),(int) fp.y()-15);



            }
        }

    }


    /**
     * Draws the graph using the "drawNode" and "drawEdge" functions
     * @param g
     */
    private void drawGraph(Graphics g) {
        directed_weighted_graph gg = this.get_ar().getGraph();

        for (node_data n : gg.getV()) {
            g.setColor(Color.blue);
            drawNode(n, 5, g);

            for (edge_data e : gg.getE(n.getKey())) {
                g.setColor(Color.gray);
                drawEdge(e, g);
            }
        }
    }


    /**
     * Initializes the dimensions of the graphics board and the arena
     * @param ar
     */

    public void update(Arena ar) {
        set_ar(ar);
        updateFrame();
    }



    private void updateFrame() {
        Range rx = new Range(20, this.getWidth() - 20);
        Range ry = new Range(this.getHeight() - 10, 150);
        Range2D frame = new Range2D(rx, ry);
        directed_weighted_graph g = this.get_ar().getGraph();

        _w2f = Arena.w2f(g, frame);

        this.set_w2f(_w2f);


    }

    public void paint(Graphics g) {
        paintComponent(g);
    }


    public Arena get_ar() {
        return _ar;
    }

    public void set_ar(Arena _ar) {
        this._ar = _ar;
    }

    public void set_w2f(Range2Range _w2f) {
        this._w2f = _w2f;
    }

    public Range2Range get_w2f() {
        return _w2f;
    }



    /**
     * I took the code from the "stack overflow" website
     * <p>
     * <p>
     * Draw an arrow line between two points.
     *
     * @param g  the graphics component.
     * @param x1 x-position of first point.
     * @param y1 y-position of first point.
     * @param x2 x-position of second point.
     * @param y2 y-position of second point.
     * @param d  the width of the arrow.
     * @param h  the height of the arrow.
     */
    private static void drawArrowLine(Graphics g, int x1, int y1, int x2, int y2, int d, int h) {
        int dx = x2 - x1, dy = y2 - y1;
        double D = Math.sqrt(dx * dx + dy * dy);
        double xm = D - d, xn = xm, ym = h, yn = -h, x;
        double sin = dy / D, cos = dx / D;
        x = xm * cos - ym * sin + x1;
        ym = xm * sin + ym * cos + y1;
        xm = x;
        x = xn * cos - yn * sin + x1;
        yn = xn * sin + yn * cos + y1;
        xn = x;
        int[] xpoints = {x2, (int) xm, (int) xn};
        int[] ypoints = {y2, (int) ym, (int) yn};
        g.drawLine(x1, y1, x2, y2);
        g.fillPolygon(xpoints, ypoints, 3);
    }

}
