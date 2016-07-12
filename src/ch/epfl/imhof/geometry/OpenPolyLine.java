/**
 * 
 * @about --Class that constructs a chain of open segment lines--
 * 
 * @author Mario Robert D'Ambrosio (249757)
 * @author Nicodème Stalder (234584)
 *
 *
 */

package ch.epfl.imhof.geometry;

import java.util.List;

/**
 * class that inherit from PolyLine
 */
public final class OpenPolyLine extends PolyLine{

	/**
	 * @param points is a List of objects Point
	 * @throws IllegalArgumentException if the list is null or empty.
	 */
	OpenPolyLine(List<Point> points)

	{
		super(points);
	}

	/**
	 * @return true or false answering the question is the polyline a closed polyline?
	 */
	public boolean isClosed()
	{
		return false;
	}

}