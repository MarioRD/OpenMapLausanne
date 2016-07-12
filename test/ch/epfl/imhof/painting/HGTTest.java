package ch.epfl.imhof.painting;

import org.junit.Test;

import ch.epfl.imhof.Map;
import ch.epfl.imhof.geometry.Point;
import ch.epfl.imhof.osm.OSMMapReader;
import ch.epfl.imhof.osm.OSMToGeoTransformer;
import ch.epfl.imhof.projection.CH1903Projection;

import static java.lang.Math.toRadians;

public class HGTTest{

	@Test
	public void test() throws Exception {
		OSMToGeoTransformer megatron = new OSMToGeoTransformer(new CH1903Projection());
		Map map = megatron.transform(OSMMapReader.readOSMFile(getClass().getResource("lausanne.osm").getFile(), false));
		
		Point bl = new Point(toRadians(7.2), toRadians(46.2));
		Point tr = new Point(toRadians(7.8), toRadians(46.8));
		
		Java2DCanvas canvas =
				new Java2DCanvas(bl, tr, 800, 800, 150, Color.WHITE);
		
		

	}

}
