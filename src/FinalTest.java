import java.io.File;

import image.utils.ImageUtils;
import ch.epfl.imhof.Main;


public class FinalTest {
public static void main(String[] args) throws Exception {
Main.main( new String[] { "lausanne.osm.gz", "N46E006.hgt", "6.5594", "46.5032", "6.6508", "46.5459",
			  "300", "lausanne.png" } );
	
	//ImageUtils.visuallyCompare(new File("lausanne.png"), new File("lausanneVraie.png"), 1);
	//ImageUtils.visuallyCompare(new File("relief.png"), new File("lausanne_reliefVraie.png"), 1);
	//ImageUtils.visuallyCompare(new File("MapSolo.png"), new File("lausanne_map.png"), 1);
	
	
}
}
