package ch.epfl.imhof.painting;

import java.util.ArrayList;
//import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

import ch.epfl.imhof.Attributed;

import static ch.epfl.imhof.painting.LineStyle.LineCap;
import static ch.epfl.imhof.painting.LineStyle.LineJoin;
import static ch.epfl.imhof.painting.Painter.line;
import static ch.epfl.imhof.painting.Filters.tagged;

/**
 * @since Java 1.8.0
 * @author Mario Robert D'Ambrosio (249757)
 * @author Nicodème Stalder (234584)
 * <p></p>
 * Class that builds a composed (layered) painter (5 per each road specification)  
 *
 */
public class RoadPainterGenerator {

	/**
	 * The line pattern (in this case continuous)
	 */
	final static float[] FULL_DASHPATTERN= new float[0];

	/**
	 * Private constructor
	 */
	private RoadPainterGenerator()
	{
		throw new RuntimeException("YOU SHALL NOT INSTANTIATE!");
	}


/**
 * 
 * @param specs The roads specifications
 * @return the composed painter
 */
	public static Painter painterForRoads(RoadSpec...specs)
	{

		Painter painterToReturn =specs[0].composedPainter.get(0);
		for(int i=0; i<=4; ++i){
			for(RoadSpec roadspec: specs){
				if( !roadspec.equals(specs[0]) || i!=0  ){
					painterToReturn = painterToReturn.above(roadspec.composedPainter.get(i));
				}
			}
		}

		return painterToReturn;
	}

	/**
	 * @since Java 1.8.0
	 * @author Mario Robert D'Ambrosio (249757)
	 * @author Nicodème Stalder (234584)
	 * <p></p>
	 * Class that initializes the roads' specifications
	 *
	 */
	public static class RoadSpec{

		private final List<Painter> composedPainter = new ArrayList<>(5); 

		private Predicate<Attributed<?>> isTunnelAndFiltered;
		private Predicate<Attributed<?>> isNormalWayAndFiltered; 
		private Predicate<Attributed<?>> isBridgeAndFiltered;

		private final float w_i;
		private final Color c_i;

		private final float w_c;
		private final Color c_c;


		/**
		 * Constructs the composed painter for the RoadSpecs
		 * @param filter the filter to use
		 * @param w_i the width of the style i (linestyle i)
		 * @param c_i the color of style i (linestyle i)
		 * @param w_c the width used to determine linestyle c
		 * @param c_c the color used to determine linestyle c
		 */
		public RoadSpec(Predicate<Attributed<?>> filter, float w_i, Color c_i, float  w_c, Color c_c){
			this.w_i=w_i;
			this.c_i=c_i;

			this.w_c=w_c;
			this.c_c=c_c;

			predicatesFilter(filter);
			painterConstruct();
		}



		/**
		 * 
		 * @param filter
		 */
		private void predicatesFilter(Predicate<Attributed<?>>filter){
			isTunnelAndFiltered =filter.
					and(tagged("tunnel"));


			isNormalWayAndFiltered =(filter.
					and(((tagged("tunnel").or
							(tagged("bridge"))).negate())));


			isBridgeAndFiltered =(filter.
					and(tagged("bridge")));
		}





		/**
		 * 
		 * Constructs a painter with 5 different components
		 * 
		 */
		private void painterConstruct(){


			//Painter of bridges' interiors 
			composedPainter.add(
					line( w_i, c_i, LineCap.Round, 
							LineJoin.Round, FULL_DASHPATTERN)
							.when(isBridgeAndFiltered)
					);


			//Painter of bridges' borders
			composedPainter.add(
					line( (w_i + 2*w_c), c_c, 
							LineCap.Butt, LineJoin.Round, FULL_DASHPATTERN)
							.when(isBridgeAndFiltered)
					);


			//Painter of normal ways' interior 
			composedPainter.add(
					line(w_i, c_i, LineCap.Round, 
							LineJoin.Round, FULL_DASHPATTERN)
							.when(isNormalWayAndFiltered)
					);


			//Painter of normal ways' borders
			composedPainter.add(
					line( (w_i + 2*w_c), c_c, LineCap.Round, 
							LineJoin.Round, FULL_DASHPATTERN)
							.when(isNormalWayAndFiltered)
					);


			//Painter of tunnels
			composedPainter.add(
					line(w_i/2, c_c, LineCap.Butt, 
							LineJoin.Round, new float[] {2f*w_i,2f*w_i})
							.when(isTunnelAndFiltered)
					);
		}
	}

}
