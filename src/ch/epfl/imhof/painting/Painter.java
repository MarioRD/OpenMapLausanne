package ch.epfl.imhof.painting;

import java.util.function.Predicate;
import java.util.stream.Collectors;

import ch.epfl.imhof.Map;
import ch.epfl.imhof.geometry.Polygon;
import ch.epfl.imhof.painting.LineStyle.LineCap;
import ch.epfl.imhof.painting.LineStyle.LineJoin;
import ch.epfl.imhof.Attributed;

/**
 * @since Java 1.8.0
 * @author Mario Robert D'Ambrosio (249757)
 * @author Nicodème Stalder (234584)
 * 
 *<pre><p><i><b>Represents a RGB Color (alpha=1.0) </b></i></p></pre>
 *
 *@see java.awt.Color Java's official class for handling colors
 */

@FunctionalInterface
public interface Painter {

	/**
	 * 
	 * @param map
	 * @param canvas
	 * draw the given map on the canvas
	 */
	void drawMap(Map map, Canvas canvas);

	/**
	 * 
	 * @param color
	 * @return a painter that draw all the polygon with the specified color
	 */
	public static Painter polygon(Color color){
		return (map, canvas)->{
			map.polygons().forEach(
					e->{
						canvas.drawPolygon(e.value(), color);
					});
		};
	}


	/**
	 * 
	 * @param linestyle
	 * @return a painter that draw all the polyline with the specified linestyle
	 */
	public static Painter line(LineStyle linestyle){
		return (map, canvas)->{
			map.polyLines().forEach(
					e->{canvas.drawPolyLine(e.value(), linestyle);}
					);
		};
	}

	/**
	 * 
	 * @param width
	 * @param color
	 * @param linecap
	 * @param linejoin
	 * @param dashpattern
	 * @return a painter that draw all the polyline with the specified linestyle
	 */
	public static Painter line(float width, Color color, LineCap linecap, LineJoin linejoin, float... dashpattern){
		return (map, canvas)->{
			map.polyLines().forEach(
					e->{
							canvas.drawPolyLine(e.value(), 
									new LineStyle(width, color, linecap, linejoin, dashpattern));
					}
					);
		};
	}

	/**
	 * 
	 * @param width
	 * @param color
	 * @return a painter that draw all the polyline with the specified parameters and use default parameters of linestyle to complete missing parameters
	 */
	public static Painter line(float width, Color color){
		return (map, canvas)->{
			map.polyLines().forEach(
					e->{
							canvas.drawPolyLine(e.value(), 
									new LineStyle(width, color));
					}
					);
		};
	}


	/**
	 * 
	 * @param linestyle
	 * @return a painter that draw the outline of all the polygon of the given map with the specifid linestyle
	 */
	public static Painter outline(LineStyle linestyle){
		return (map,  canvas) ->{
			map.polygons().forEach(
					e->{
						final Polygon polygon=e.value();
						canvas.drawPolyLine(polygon.shell(), linestyle);
						polygon.holes().forEach(
								hole->canvas.drawPolyLine(hole, linestyle)
								);
					}
					);
		};
	}


	/**
	 * 
	 * @param width
	 * @param color
	 * @param linecap
	 * @param linejoin
	 * @param dashpattern
	 * @return a painter that draw the outline of all the polygon of the given map with the specifid line style
	 */
	public static Painter outline(float width, Color color, LineCap linecap, LineJoin linejoin, float... dashpattern){
		final LineStyle linestyle= new LineStyle(width, color, linecap, linejoin, dashpattern);
		return (map, canvas) ->{
			map.polygons().forEach(
					e->{
						final Polygon polygon=e.value();
						canvas.drawPolyLine(polygon.shell(), linestyle);
						polygon.holes().forEach(
								hole->canvas.drawPolyLine(hole, linestyle)
								);
					}
					);
		};
	}


	/**
	 * 
	 * @param width
	 * @param color
	 * @return a painter that draw the outline of all the polygon of the given map with the specified parameters and use default parameters of linestyle to complete
	 */
	public static Painter outline(float width, Color color){
		final LineStyle linestyle= new LineStyle(width, color);
		return (map, canvas) -> {
			map.polygons().forEach(
					e->{
						final Polygon polygon=e.value();
						canvas.drawPolyLine(polygon.shell(), linestyle);
						polygon.holes().forEach(
								hole->canvas.drawPolyLine(hole, linestyle)
								);
					}
					);
		};
	}

	/**
	 * 
	 * @param attributed
	 * @return a painter that draw only the entity that satisfy the given predicate
	 */
	public default Painter when(Predicate<Attributed<?>> attributed){

		return (map, canvas)->{
			drawMap(
					new Map(

							map.polyLines().parallelStream().filter
							(polyline -> attributed.test(polyline)).collect(Collectors.toList()),

							map.polygons().parallelStream().filter
							(polygon -> attributed.test(polygon)).collect(Collectors.toList())
							)
					,
					canvas);
		};
	}

	/**
	 * 
	 * @param painter
	 * @return a painter that draw first the map of the given painter and then the map of this painter
	 */
	public default Painter above(Painter painter){
		return (map, canvas)->{
			painter.drawMap(map, canvas);
			drawMap(map, canvas);
		};
	}


	/**
	 * 
	 * @return a painter that draw the elements in the order defined by the layer level (it draws from smaller to bigger layer level)
	 */
	public default Painter layered(){

		Painter painter =(map, canvas)->drawMap(map,canvas);

		Painter painterToReturn=painter.when(Filters.onLayer(5));

		for(int i=4; i>=-5; --i)
		{			
					painterToReturn=painterToReturn.above(painter.when(Filters.onLayer(i)));
				}
		return painterToReturn;
	}

}
