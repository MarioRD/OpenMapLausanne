package ch.epfl.imhof;

/**
 * @author Mario Robert D'Ambrosio (249757)
 * @author Nicodème Stalder (234584)
 * 
 *--The coordinates of a user-defined spheric-like geographical mapping (on Earth of course) and, eventually, prints it out if needed.-- 
 */

import java.lang.Math;


/**
 * This class saves the coordinates of a user-defined spheric-like geographical mapping (on Earth of course) and, eventually, prints it out if needed.
 * 
 * @param longitude: the given longitude of the point, in radians
 * @param latitude: the given latitude of the point, in radians
 * 
 * @Throws IllegalArgumentException
 *           if longitude is not defined in the following interval: [-PI;PI] 
 *           or if latitude is not defined in the following interval: [-PI/2; PI/2]
 */
public final class PointGeo {

	private final double longitude;
	private final double latitude;

	public PointGeo(double longitude, double latitude)
	{
		this.longitude=longitude; 
		this.latitude=latitude;
		if(longitude<(-Math.PI) || longitude>(Math.PI) || latitude<(-Math.PI/2)|| latitude>(Math.PI/2) )
		{
			throw new IllegalArgumentException();
		}
	}

	/** 
	 * Method: longitude()
	 * @return longitude in radians 
	 */
	public final double longitude() {
		return longitude;
	}

	/** Method: latitude()
	 * @return latitude in radians
	 * 
	 */
	public final double latitude() {
		return latitude;
	}

}
