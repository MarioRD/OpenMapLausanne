package ch.epfl.imhof.painting;
import static org.junit.Assert.*;
import image.utils.ImageUtils;

import java.io.File;
import java.io.IOException;
import java.util.function.Predicate;

import javax.imageio.ImageIO;

import org.junit.Test;
import org.xml.sax.SAXException;

import ch.epfl.imhof.Attributed;
import ch.epfl.imhof.Map;
import ch.epfl.imhof.SwissPainter;
import ch.epfl.imhof.geometry.Point;
import ch.epfl.imhof.osm.OSMMapReader;
import ch.epfl.imhof.osm.OSMToGeoTransformer;
import ch.epfl.imhof.painting.Color;
import ch.epfl.imhof.painting.Filters;
import ch.epfl.imhof.painting.Java2DCanvas;
import ch.epfl.imhof.painting.Painter;
import ch.epfl.imhof.projection.CH1903Projection;


public class LaCarte {

	@Test
	public void test() throws SAXException, IOException {
		Predicate<Attributed<?>> isLake =
				Filters.tagged("natural", "water");
		Painter lakesPainter =
				Painter.polygon(Color.BLUE).when(isLake);

		Predicate<Attributed<?>> isBuilding =
				Filters.tagged("building");
		Painter buildingsPainter =
				Painter.polygon(Color.BLACK).when(isBuilding);

		Painter painter = lakesPainter.above(buildingsPainter);

		OSMToGeoTransformer megatron = new OSMToGeoTransformer(new CH1903Projection());

		Map map = megatron.transform(OSMMapReader.readOSMFile(getClass().getResource("/lausanne.osm").getFile(), false));


		// La toile
		Point bl = new Point(532510, 150590);
		Point tr = new Point(539570, 155260);
		Java2DCanvas canvas =
				new Java2DCanvas(bl, tr, 1600, 1060, 150, Color.WHITE);

		// Dessin de la carte et stockage dans un fichier
		//SwissPainter.painter().drawMap(map, canvas);
		//ImageIO.write(canvas.image(), "png", new File("lozSchinzHDFilters3.png"));
		ImageUtils.visuallyCompare(new File("lausVraie.png"), new File("lozSchinzHDFilters3.png"), 1);
	}

}
