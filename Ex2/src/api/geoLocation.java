package api;
import java.util.Objects;
/**
 * This Class represents a geo location <x,y,z>, aka Point3D , and implements geo_location interface
 * The class also includes a method to calculate distance between two points
 * @author Shlomo Glick
 * @author Gilad Shotland
 */


public class geoLocation implements geo_location {
    /********** Initialization Variables **********/
    public double x;
    public double y;
    public double z;
    /*********** Constructors *********/
    /**
     * Constructor for 3D Point
     * @param parseDouble x value
     * @param parseDouble1 y value
     * @param parseDouble2 z value
     */
    public geoLocation(double parseDouble, double parseDouble1, double parseDouble2)  {
        this.x=parseDouble;
        this.y=parseDouble1;
        this.z=parseDouble2;
    }
    /**
     * Constructor for 2D Point
     * @param parseDouble x value
     * @param parseDouble1 y value
     * z value is zero
     */
    public geoLocation(double parseDouble, double parseDouble1)  {
        this.x=parseDouble;
        this.y=parseDouble1;
        this.z=0;
    }
    /********** Getters **********/
    /**
     * This method returns the x value of the point
     * @return double
     */
    @Override
    public double x() {
        return this.x;
    }
    /**
     * This method returns the y value of the point
     * @return double
     */
    @Override
    public double y() {
        return this.y;
    }
    /**
     * This method returns the z value of the point
     * @return double
     */
    @Override
    public double z() {
        return this.z;
    }

    /**
     * This method calculates distance between this location and a given location
     * @param g location given
     * @return double
     */
    @Override
    public double distance(geo_location g) {
        //Calculating using the Analytical Geometry Formula for distance between points
        double dx = this.x() - g.x();
        double dy = this.y() - g.y();
        double dz = this.z() - g.z();
        double t = (dx*dx+dy*dy+dz*dz);
        return Math.sqrt(t);

    }

    /**
     * Equals method between this point and a given point
     * return true if and only if the x , y  and z values are equal
     * @param o given point
     * @return boolean
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        geoLocation that = (geoLocation) o;
        return Double.compare(that.x, x) == 0 &&
                Double.compare(that.y, y) == 0 &&
                Double.compare(that.z, z) == 0;
    }


}
