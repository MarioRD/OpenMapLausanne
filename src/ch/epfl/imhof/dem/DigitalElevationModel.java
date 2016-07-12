package ch.epfl.imhof.dem;

import ch.epfl.imhof.Vector3;
import ch.epfl.imhof.PointGeo;



/**
 * @since Java 1.8.0
 * @author Mario Robert D'Ambrosio (249757)
 * @author Nicodème Stalder (234584)
 * <p></p>
 * Interface for the <b>HGTDigitalElevationModel.class</b>, it's useful since by extending the autocloseable interface itself, 
 * the method <code>normalAt</code> will just need a try-catch block for dealing with Exceptions 
 *
 *@see HGTDigitalElevationModel
 */
public interface DigitalElevationModel extends AutoCloseable {

	/**
	 * 
	 * @param point the point as PointGeo object
	 * @return a new Vector3 object from the point coordinates
	 * @throws Exception this is redefined in the method itself
	 */
	public abstract Vector3 normalAt(PointGeo point) throws Exception;

}
