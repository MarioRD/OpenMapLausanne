package ch.epfl.imhof;

/**
 * @author Mario Robert D'Ambrosio (249757)
 * @author Nicodème Stalder (234584)
 * 
 * --Class that stores polyLines and polyLines to form a map.
 */

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ch.epfl.imhof.geometry.PolyLine;
import ch.epfl.imhof.geometry.Polygon;

/**
 * --Class that stores polyLines and polyLines to form a map.
 * @param polyLines is a list of the Attributed polyLines contained in the map.
 * @param polygins is a list of the Attributed polygons contained in the map.
 */
public final class Map

{ 

	private final List<Attributed<PolyLine>> polyLines;
	private final List<Attributed<Polygon>> polygons;

	public Map(List<Attributed<PolyLine>> polyLines, List<Attributed<Polygon>> polygons)
	{
		final List<Attributed<PolyLine>> polyLinesCopy = new ArrayList<Attributed<PolyLine>>(Collections.unmodifiableList(polyLines));
		this.polyLines=polyLinesCopy;

		final List<Attributed<Polygon>> polygonsCopy = new ArrayList<Attributed<Polygon>>(Collections.unmodifiableList(polygons));
		this.polygons=polygonsCopy;
	}

	/**
	 * @return the class attributes polyLines.
	 */
	public List<Attributed<PolyLine>> polyLines()
	{
		final List<Attributed<PolyLine>> polyLinesCopy = new ArrayList<Attributed<PolyLine>>(Collections.unmodifiableList(polyLines));
		return polyLinesCopy;
	}

	/**
	 * @return the class attributes polygons.
	 */
	public List<Attributed<Polygon>> polygons()
	{
		final List<Attributed<Polygon>> polygonsCopy = new ArrayList<Attributed<Polygon>>(Collections.unmodifiableList(polygons));
		return polygonsCopy;
	}

	/**
	 * class used to create iteratively a Map object.
	 * @param polyLines is a list of the Attributed polyLines which will be contained in the map.
	 * @param polygins is a list of the Attributed polygons which will be contained in the map.
	 */
	public static class Builder
	{
		private  List<Attributed<PolyLine>> polyLines = new ArrayList<>();
		private  List<Attributed<Polygon>> polygons  = new ArrayList<>();

		/**
		 * @param newPolyLine is an attributed PolyLine that will be added to the class attribute List polyLines
		 */
		public void addPolyLine(Attributed<PolyLine> newPolyLine)
		{
			polyLines.add(newPolyLine);
		}

		/**
		 * @param newPolygon is an attributed PolyLine that will be added to the class attribute List polygons
		 */
		public void addPolygon(Attributed<Polygon> newPolygon)
		{
			polygons.add(newPolygon);
		}
		/**
		 * @return a builded object of type Map we were creating using this building class.
		 */
		public Map build()
		{
			return new Map(polyLines,polygons);
		}
	}

}
