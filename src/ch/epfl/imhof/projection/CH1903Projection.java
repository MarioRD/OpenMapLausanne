package ch.epfl.imhof.projection;

/**
 * @author Mario Robert D'Ambrosio (249757)
 * @author Nicodème Stalder (234584)
 * 
 * --Class that is used to convert PointGeo from the WGS 84 coordinate system into Point in the CH1093 coordinate system and the contrary.
 */

import ch.epfl.imhof.geometry.Point;
import ch.epfl.imhof.PointGeo;

/**
 * Class that contains two methods: one that convert a PointGeo from the WGS 84 coordinate system into a Point in the CH1093 coordinate system and the other do the contrary.
 */
public final class CH1903Projection implements Projection

{

	/**
	 * method that converts a PointGeo object into a Point object with the transformation described below.
	 * @param point : which is a PointGeo
	 * @return returns a Point Object
	 */
	public Point project(PointGeo point){

		final double longitude1;
		final double latitude1;
		longitude1 = ((180.0/(Math.PI)*(point.longitude())*3600-26782.5)*0.0001);
		latitude1 = (((Math.toDegrees(point.latitude()))*3600.0-169028.66)*0.0001);

		final double x;
		final double y;
		x=              600072.37
				+(211455.93*longitude1)
				-10938.51*longitude1*latitude1
				-0.36*longitude1*(Math.pow(latitude1,2.0))
				-44.54*(Math.pow(longitude1,3.0));

		y=              200147.07
				+308807.95*latitude1
				+3745.25*(Math.pow(longitude1,2.0))
				+76.63*(Math.pow(latitude1,2.0))
				-194.56*(Math.pow(longitude1,2.0))*latitude1
				+119.79*(Math.pow(latitude1,3.0));

		return new Point(x,y);
	}

	/**
	 * method that converts a Point Object into a PointGeo object with the transformation described below.
	 * @param point : which is a Point Object
	 * @return returns a PointGeo Object
	 */
	public PointGeo inverse(Point point){

		final double x1;
		final double y1;
		x1 = (((point.x())-600000.0)/1000000.0);
		y1 = (((point.y())-200000.0)/1000000.0);

		final double longitude0;
		final double latitude0;
		longitude0 = 2.6779094
				+4.728982*x1
				+0.791484*x1*y1
				+0.1306*x1*(Math.pow(y1,2.0))
				-0.0436*(Math.pow(x1,3.0));

		latitude0 = 16.9023892
				+3.238272*y1
				-0.270978*(Math.pow(x1,2.0))
				-0.002528*(Math.pow(y1,2.0))
				-0.0447*(Math.pow(x1,2.0))*y1
				-0.0140*(Math.pow(y1,3.0));

		final  double longitude;
		final double latitude;
		longitude = longitude0 * (100.0/36.0);
		latitude = latitude0 * (100.0/36.0);

		return new PointGeo((Math.toRadians(longitude)), (Math.toRadians(latitude)));
	}

}