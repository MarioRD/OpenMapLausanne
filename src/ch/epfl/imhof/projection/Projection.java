package ch.epfl.imhof.projection;
/**
 * @author Mario Robert D'Ambrosio (249757)
 * @author Nicodème Stalder (234584)
 * 
 * -- This interface is used to shows the two abstract method you need to implement this interface Projection.
 */
import ch.epfl.imhof.geometry.Point;
import ch.epfl.imhof.PointGeo;

/**
 * -- This interface shows the two abstract method you need to implement this interface Projection.
 * @method "project" : converts a PointGeo Object from the WGS 84 coordinate system into a Point in the CH1093 coordinate system
 * @method  "inverse" : converts a Point Object from the CH1903 coordinate system into a Point Object in the WGS 84 coordinate system
 */
public interface Projection
{
	/**
	 * @param point which is a point you want to transform in another point doing a projection.
	 * @return the point transformed
	 */
	public Point project(PointGeo point);
	
	/**
	 * @param point which is a point you want to transform in another point doing the inverse projection.
	 * @return the point transformed
	 */
	public PointGeo inverse(Point point);
}
