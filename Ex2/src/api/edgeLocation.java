package api;


public class edgeLocation implements edge_location {
    private edge_data current;
    private double retio;

    public edgeLocation(){
        this.current=null;
        this.retio=0;
    }


    @Override
    public edge_data getEdge() {
        return this.current;
    }

    @Override
    public double getRatio() {
        return this.retio;
    }
}
