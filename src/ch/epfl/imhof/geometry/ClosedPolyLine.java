/**
 * Class that is used to represent a Closed PolyLine.
 * 
 * @author Mario Robert D'Ambrosio (249757)
 * @author Nicodème Stalder (234584)
 */

package ch.epfl.imhof.geometry;

import java.util.List;


/**
 * class that inherit from PolyLine
 */
public final class ClosedPolyLine extends PolyLine{

	/**
	 * @param points is a List of objects Point
	 * @throws IllegalArgumentException if the list is null or empty.
	 */
	public ClosedPolyLine(List<Point> points)

	{
		super(points);
	}
	
	/**
	 * @return true or false answering the question is the polyline a closed polyline?
	 */
	public boolean isClosed()
	{
		return true;
	}
	
	/**
	 * @return the aera of the polyline represented by the points of the list this.points
	 */
	public double area()
	
	{
		double aera=0;
		for(int i=0; i<points().size(); ++i)
		{
			double xi = vertexOfGeneralisedIndex(i).x();
			double yiPlusOne = vertexOfGeneralisedIndex(i+1).y();
			double yiMinusOne = vertexOfGeneralisedIndex(i-1).y();
			aera += xi*(yiPlusOne - yiMinusOne);
		}
		aera /= 2;

		return Math.abs((double)aera);
	}
	
	/**
	 * @param index is an index that is not normalized to be used to access the elements of the List this.points.
	 * @return an index that is normalized to be used to access the elements of the List this.points.
	 */
	private Point vertexOfGeneralisedIndex(int index)
	
	{
		return points().get(Math.floorMod(index, points().size()));
	}
	
	/**
	 * @param p is a Point object
	 * @return true if the point is into the polyline represented by the list this.points
	 */
	public boolean containsPoint(Point p)
	
	{
		Point p1;
		Point p2;
		int windingNumber=0;

		for(int i=0; i<points().size(); ++i)
		{
			p1 = vertexOfGeneralisedIndex(i);
			p2 = vertexOfGeneralisedIndex(i+1);

			if (p1.y()<=p.y())
			{
				if(p2.y()>p.y() && isAtLeft (p, p1, p2))
				{
					++windingNumber;
				}
			}
			else
			{
				if(p2.y()<=p.y() && isAtLeft (p, p2, p1))
				{
					--windingNumber;
				}
			}

		}

		return (windingNumber!=0);

	}
	
	/**
	 * @param p, p1 and p2 are points
	 * @return true if the p point is at the left of the line that start from p1 and go to p2
	 */
	private boolean isAtLeft (Point p, Point p1, Point p2)
	
	{
		if ((p1.x()-p.x())*(p2.y()-p.y())>(p2.x()-p.x())*(p1.y()-p.y()))
		{
			return true;
		}
		return false;
	}


}