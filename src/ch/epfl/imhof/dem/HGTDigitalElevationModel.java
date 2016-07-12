package ch.epfl.imhof.dem;

import java.io.File;
import java.io.FileInputStream;
import java.math.BigDecimal;
import java.math.MathContext;
import java.nio.ShortBuffer;
import java.nio.channels.FileChannel.MapMode;

import ch.epfl.imhof.Vector3;
import ch.epfl.imhof.PointGeo;

import static java.lang.Math.sqrt;
import static java.lang.Math.floor;
import static java.lang.Math.toDegrees;
import static java.lang.Math.toRadians;


/**
 * @since Java 1.8.0
 * @author Mario Robert D'Ambrosio (249757)
 * @author Nicodème Stalder (234584)
 *<p></p>
 *A class that represents an HGT file range of altitudes 
 *
 */
public class HGTDigitalElevationModel implements DigitalElevationModel {

	final double latitude;
	final double longitude;
	final int resolution;
	ShortBuffer buffer;
	final FileInputStream stream;
	final double s;








	/**
	 * Constructs a model from a given HGT-like file
	 * @param file the ".hgt" to input
	 * @throws Exception when the filename is irregular
	 */
	public HGTDigitalElevationModel(File file) throws Exception{

		resolution = (int)Math.sqrt(file.length()/2);

		if(resolution==0L || 
				!isPerfectSquare(file.length()/2) ||
				!file.getName().endsWith(".hgt")||
				file.getName().length() != 11 
				)
		{
			throw new IllegalArgumentException("Illegal filename!");
		}

		latitude=latitude(file.getName().substring(0, 3));

		longitude=longitude(file.getName().substring(3,7));

		final long length = file.length();

		s = Earth.RADIUS*toRadians(1d/(resolution-1));

		try {
			stream = new FileInputStream(file);
			buffer = stream.getChannel()
					.map(MapMode.READ_ONLY, 0, length)
					.asShortBuffer();
		} finally{
			close();
		}
	}








	/**
	 * Method that checks whether a number is a perfect square or not
	 * @param n the number to check on 
	 * @return boolean value (<code>true</code> if it's a perfect square, <code>false</code> otherwise)
	 */
	public final static boolean isPerfectSquare(long n)
	{
		switch((int)(n & 0xF))
		{
		case 0: case 1: case 4: case 9:
			return new BigDecimal(sqrt(n), MathContext.DECIMAL64).doubleValue() ==((int) sqrt(n));

		default: return false;
		}
	}







	/**
	 * Converts a string into a latitude double value
	 * @param s the string to be converted into latitude
	 * @return a double value that represents the latitude
	 * @throws  IllegalArgumentException if the string is invalid
	 */
	private double latitude(String s){
		if(!s.startsWith("N") && !s.startsWith("S")) 
			throw new IllegalArgumentException("Illegal filename! (there is no 'N' or 'S')");

		return s.startsWith("N") ? +Integer.parseInt(s.substring(1)) : -Integer.parseInt(s.substring(1));
	}


	/**
	 * Converts a string into a longitude double value
	 * @param s the string to be converted into longitude
	 * @return a double value that represents the longitude
	 * @throws  IllegalArgumentException if the string is invalid
	 */
	private double longitude(String s){
		if(!s.startsWith("E") && !s.startsWith("W")) 
			throw new IllegalArgumentException("Illegal filename! (there is no 'E' or 'W')");

		return s.startsWith("E") ? +Integer.parseInt(s.substring(1)) : -Integer.parseInt(s.substring(1));
	}






	/** 
	 * It closes the stream and clears the buffer
	 * @see java.lang.AutoCloseable#close()
	 * @throws Exception 
	 */
	@Override
	public void close() throws Exception {
		stream.close();
		buffer.clear();
	}









	/** 
	 * @see ch.epfl.imhof.dem.DigitalElevationModel#normalAt(ch.epfl.imhof.PointGeo)
	 * 
	 * @throws Exception when the point is out of file range 
	 * (i.e. latitude or longitude are not in [latitude;latitude+1] nor in [longitude;longitude+1])
	 */
	@Override
	public Vector3 normalAt(PointGeo point) throws Exception {
		final double pointLongitude=toDegrees(point.longitude());
		final double pointLatitude=toDegrees(point.latitude());

		if(pointLongitude < longitude || 
				pointLongitude > longitude+1 ||
				pointLatitude  < latitude || 
				pointLatitude > latitude+1)
			throw new IllegalArgumentException("Point out of HGT file range!");

		final int i = (int)(floor((pointLongitude-longitude)*resolution));
		final  int j = (int)(resolution - floor((pointLatitude -latitude)*resolution));

		final int z0=buffer.get((j*resolution)+i -1);
		final int z1=buffer.get((j*resolution)+i);
		final int z2=buffer.get(((j-1)*resolution)+i-1);
		final int z3=buffer.get(((j-1)*resolution)+i);

		return new Vector3
				(
						(s/2d) *(z0-z1+z2-z3),
						(s/2d) *(z0+z1-z2-z3),
						s*s
						);
	}


}
