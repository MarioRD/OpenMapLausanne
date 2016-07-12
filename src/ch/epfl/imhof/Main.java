package ch.epfl.imhof;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import ch.epfl.imhof.dem.HGTDigitalElevationModel;
import ch.epfl.imhof.dem.ReliefShader;
import ch.epfl.imhof.geometry.Point;
import ch.epfl.imhof.osm.OSMMapReader;
import ch.epfl.imhof.osm.OSMToGeoTransformer;
import ch.epfl.imhof.painting.Color;
import ch.epfl.imhof.painting.Java2DCanvas;
import ch.epfl.imhof.projection.CH1903Projection;
import static java.lang.Math.toRadians;
import static java.lang.Math.round;
import static ch.epfl.imhof.dem.Earth.RADIUS;
import static ch.epfl.imhof.painting.Color.rgb;

/**
 * @since Java 1.8.0
 * @author Mario Robert D'Ambrosio (249757)
 * @author Nicodème Stalder (234584)
 * 
 *<pre><p><i><b>Class for handling the main, 
 *which is the program that makes all the classes work together </b></i></p></pre>
 *
 */
public class Main {

	/**
	 * 
	 * @param <b>args[0]</b> The input ".osm" file compressed
	 * @param <b>args[1]</b> The altitude ".hgt" file
	 * @param <b>args[2]</b>  the left-most longitude of the section that the map should cover
	 * @param <b>args[3]</b>  the left-most latitude of the section that the map should cover
	 * @param <b>args[4]</b>  the right-most longitude of the section that the map should cover
	 * @param <b>args[5]</b> the right-most latitude of the section that the map should cover
	 * @param <b>args[6]</b>  the resolution expressed in DPI
	 * @param <b>args[7]</b>  the output name the final map should be saved as
	 * @throws Exception it throws IllegalArgumentException if the arguments' number is not equal to 8 
	 * or if the resolution is less than 0. For any other kind of problem it could throw errors from other classes
	 */
	public static void main(String args[]) throws Exception {

		if(args.length!=8)throw new IllegalArgumentException("Improper amount of arguments!!");

		final double latitudeLeft=toRadians(Double.parseDouble(args[3]));
		final double latitudeRight=toRadians(Double.parseDouble(args[5]));
		final int resolution=Integer.parseInt(args[6]);

		if(resolution < 0) throw new IllegalArgumentException("Negative resolution!!");

		final double flotRadius = resolution * 1.7 / 25.4;

		final Point p_TR= (new CH1903Projection()).project(
				new PointGeo(
						toRadians(Double.parseDouble(args[4])),
						latitudeRight
						)
				);

		final Point p_BL=(new CH1903Projection()).project(
				new PointGeo(
						toRadians(Double.parseDouble(args[2])),
						latitudeLeft
						)
				);

		final int height= (int) round( ( 
				(resolution/0.0254d/25000d*RADIUS)*
				(latitudeRight-latitudeLeft)
				));

		final int width= (int) round(( 
				height*
				(p_TR.x()  - p_BL.x())/
				(p_TR.y()  - p_BL.y())
				));

		OSMToGeoTransformer OSMtransformed = new OSMToGeoTransformer(new CH1903Projection());

		Map map = OSMtransformed.transform(OSMMapReader.readOSMFile(new File(args[0]).getName(), true));

		Java2DCanvas canvas =new Java2DCanvas(p_BL, p_TR, width, height, resolution, Color.WHITE);

		SwissPainter.painter().drawMap(map, canvas);

		ImageIO.write(canvas.image(), "png", new File("MapSolo.png"));


		ImageIO.write(new ReliefShader
				(new CH1903Projection(), 
						new HGTDigitalElevationModel(new File(args[1])), 
						new Vector3(-1,1,1)
						).shadedRelief(p_BL, p_TR, width, height, flotRadius), 
						"png", new File("relief.png"));

		final BufferedImage mapSolo = ImageIO.read(new File("MapSolo.png"));
		final BufferedImage relief = ImageIO.read(new File("relief.png"));
		final BufferedImage finalmap = composition(mapSolo, relief);

		ImageIO.write(finalmap, "png", new File(args[7]));

	}


	/**
	 * 
	 * @param map The input map_solo image
	 * @param relief the blurred relief BufferedImage
	 * @return a new BufferedImage whose pixels are a result of multiplication of the two inputs' components
	 */
	private static BufferedImage composition(BufferedImage map, BufferedImage relief){
		final int width = map.getWidth();
		final int height = map.getHeight();

		final BufferedImage mapToReturn = new BufferedImage(width,height, BufferedImage.TYPE_INT_RGB);

		for(int i = 0; i < width; i++){
			for(int j = 0; j < height; j++){
				Color pixel_color = rgb(map.getRGB(i, j)).
						multiplication(rgb(relief.getRGB(i, j)));

				mapToReturn.setRGB(i, j, pixel_color.javaColor().getRGB());
			}
		}
		return mapToReturn;
	}

}
