package ch.epfl.imhof.projection;

/**
 * Class that is used to convert PointGeo into Point using the equirectangular projection and the contrary.
 *
 * @author Mario Robert D'Ambrosio (249757)
 * @author Nicodème Stalder (234584)
 */

import ch.epfl.imhof.geometry.Point;
import ch.epfl.imhof.PointGeo;
import ch.epfl.imhof.projection.Projection;

/**
 * Class that contains two methods: one that convert a PointGeo into a Point using the equirectangular projection and the other do the contrary.
 */
public final class EquirectangularProjection implements Projection

{
	
	/**
	 * method that convert a PointGeo object into a Point object with the transformation described below.
	 * @param point : which is a PointGeo
	 * @return return a Point
	 */
	public Point project(PointGeo point)
	{
		return new Point ((point.longitude()),(point.latitude()));
	}
	
	/**
	 * method that convert a Point object into a PointGeo object with the transformation described below.
	 * @param point : which is a Point
	 * @return return a PointGeo
	 */
	public PointGeo inverse(Point point)
	{
		return new PointGeo ((point.x()),(point.y()));
	}

}