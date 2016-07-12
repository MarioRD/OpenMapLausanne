package ch.epfl.imhof.geometry;

import java.util.function.Function;

/**
 * --The coordinates of a user-defined plane-like geographical mapping (on Earth of course) and, eventually, prints it out if needed.-- 
 * 
 * @author Mario Robert D'Ambrosio (249757)
 * @author Nicodème Stalder (234584)
 *
 *
 */

public final class Point {

    /**
     * This class saves the coordinates of a user-defined plane-like geographical mapping (on Earth of course) and, eventually, prints it out if needed.
     * 
     * @param x: the given x-coordinate of the point
     * @param y: the given y-coordinate of the point
     */

    private final double x; 
    private final double y;



    public Point(double x, double y){
        this.x=x; this.y=y;
    }

    /**
     * Method: x()
     * @return x-coordinate of the Point Object 
     */
    public final double x() {
        return x;
    }
    
    /**
     * Method: y()
     * @return y-coordinate of the Point Object
     */
    public final double y() {
        return y;
    }
    
    /**
     * 
     * @param a1 point from the first coordinate system.
     * @param b1 point from the second coordinate system.
     * @param a2 point from the first coordinate system.
     * @param b2 b1 point from the second coordinate system.
     * @return an object of type Function<Point,Point> which is an instance of the functional interface Function from java.util.function.Function .
     * this instance is useful to transform a point from a coordinate system into a point from another coordinate system.
     * the relation between the first coordinate system and the second is a space dilatation and a translation (both dilatation and translation can be independently 0)
     * we know that a1 and a2 are from the first coordinate system and b1 and b2 from the second.
     * @throws IllegalArgumentExceptionwhen "x" or "y" of at least two points are equal
     */
    public static Function<Point,Point> alignedCoordinateChange(Point a1,Point b1,Point a2,Point b2) throws IllegalArgumentException
    {
    	double a1_x = a1.x, a1_y=a1.y , b1_x=b1.x , b1_y = b1.y , 
    			a2_x = a2.x, a2_y=a2.y , b2_x=b2.x , b2_y = b2.y; 
    	
    	if(a1_x == b1_x || a1_x == b2_x || a1_x == a2_x  || a1_x == b2_x) 
    		throw new IllegalArgumentException(" Equal x parameters! ");
    	
    	if(a1_y == b1_y || a1_y == b2_y || a1_y == a2_y  || a1_y == b2_y) 
    		throw new IllegalArgumentException(" Equal y parameters! ");
    	
    	double a_x = (b1.x()-b2.x())/(a1.x()-a2.x());
    	double b_x = b1.x()-a_x*a1.x();
    	
    	double a_y = (b1.y()-b2.y())/(a1.y()-a2.y());
    	double b_y = b1.y()-a_y*a1.y();
    		
    		return p1 ->new Point(a_x*p1.x()+b_x,a_y*p1.y()+b_y);
    }
}
