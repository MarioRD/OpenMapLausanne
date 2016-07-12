/**
 * Abstract class that represents a Polyline.
 *
 * @author Mario Robert D'Ambrosio (249757)
 * @author Nicodème Stalder (234584)
 * 
 */
package ch.epfl.imhof.geometry;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

 /**
     * Abstract class that represents a Polyline which is represented by a sequence of points..
     * 
     * @param points which is a list of Point objects used to represent the vertexes of the Polyline.
     * @throws IllegalArgumentException if the parameters of the constructor is null or empty.
     * 
     */
public abstract class PolyLine {
   
    private final List<Point> points;

    public PolyLine(List<Point> points)
    {
    
        if (points==null)
        {
            throw new IllegalArgumentException();
        }
        if (points.equals(Collections.emptyList()))
        {
            throw new IllegalArgumentException();
        }
        final List<Point> pointsSave = (new ArrayList<Point>(points));
        this.points=Collections.unmodifiableList(pointsSave);
    
    }

    /**
     * @return true if the class represent a ClosedPolyLine, false if the class don't represent a ClosedPolyLine
     */
    public abstract boolean isClosed();

    /**
     * @return the attribute of the class called points which is a list of of Point objects.
     */
    public List<Point> points()
    {
        return points;
    }

    public Point firstPoint()
    {
        return points.get(0);
    }


    /**
     * Builder of the PolyLine class: you can use this class to build a PolyLine in various steps.
     */
    public static final class Builder{

        /**
         * @method 'addPoint': let's the user add a point to the points list 
         * @param newPoint the point which the user wants to add to the 'points' list.
         */
        private final List<Point> pointsSave= new ArrayList<Point>() ;
        
        public void addPoint(Point newPoint){
            pointsSave.add(newPoint);
        }


        /**
         * @method 'buildOpen()' creates a new instance of an open Polyline 
         * with the up-to-last-item 'points' list
         *@return the builded open Polyline
         */
        public OpenPolyLine buildOpen() {
            return new OpenPolyLine(Collections.unmodifiableList(pointsSave));
        }


        /**
         * @method 'buildClosed()' creates a new instance of a closed Polyline 
         * with the up-to-last-item 'points' list
         * @return the builded closed Polyline
         */
        public ClosedPolyLine buildClosed(){
            return new ClosedPolyLine(Collections.unmodifiableList(pointsSave));
        }

    }


}