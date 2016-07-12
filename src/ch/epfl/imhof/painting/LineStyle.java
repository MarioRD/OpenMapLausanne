/**
 * @author Mario Robert D'Ambrosio (249757)
 * @author Nicodème Stalder (234584)
 *
 * Collects and defines all the parameters needed to build a line design
 */

package ch.epfl.imhof.painting;

/**
 * Collects and defines all the parameters needed to build a line design
 */
public final class LineStyle {

	
	//-------------------------Constructors-----------------------------------

	private final float  width;
	private final Color color;
	private final LineCap linecap;
	private final LineJoin linejoin;
	private final float[] dashpattern;



	/**
	 * Constructs a LineStyle with all the possible parameters (Width, Color, LineCap, LineJoin and DashPattern)
	 *
	 * @param width the width of the line
	 * @param color the line's color
	 * @param linecap the line's ending
	 * @param linejoin the line's join
	 * @param dashpattern the line's pattern
	 * 
	 * @throws IllegalArgumentException if the width is null or negative, 
	 * else if dashpattern is invalid (i.e. negative or null values) 
	 */
	LineStyle(float width, Color color, LineCap linecap, LineJoin linejoin, float[] dashpattern){

		if(width<0) throw new IllegalArgumentException("Negative width is not allowed!");

		for(int i=0; i<dashpattern.length; ++i) {
			if(dashpattern[i]==0.0f || dashpattern[i]<0){
				throw new IllegalArgumentException("Invalid dash pattern!");
			}
		}

		this.width=width;
		this.color=color;
		this.linecap=linecap;
		this.linejoin=linejoin;
		this.dashpattern=dashpattern;
	}





	/**
	 * Constructs a LineStyle with only two parameters (Width and Color) and 
	 * default values for the other three (LineCap: Butt, LineJoin: Miter and DashPattern: an empty float array)
	 * 
	 * @param width the width of the line
	 * @param color the line's color
	 * 
	 * @throws IllegalArgumentException if the width is null or negative
	 */
	LineStyle(float width, Color color){
		this(width, color, LineCap.Butt, LineJoin.Miter, new float[0]);
	}





	/**
	 * Defines the line ending style: Butt, Round or Square 
	 */
	public static enum LineCap{
		Butt("butt"), Round("round"), Square("square");
		private LineCap(String type){}
	}



	/**
	 * Defines the line joining style: Bevel, Miter or Round 
	 */
	public static enum LineJoin{
		Bevel("bevel"), Miter("miter"), Round("round");
		private LineJoin(String type){}
	}

	//--------------------------End of Constructors-----------------------------



	
	
	
	
	
	
	//----------------------------METHODS--------------------------------------


	/**
	 * @return the width
	 */
	public float getWidth() {
		return width;
	}






	/**
	 * @return the color
	 */
	public Color getColor() {
		return color;
	}






	/**
	 * @return the linecap
	 */
	public LineCap getLinecap() {
		return linecap;
	}






	/**
	 * @return the linejoin
	 */
	public LineJoin getLinejoin() {
		return linejoin;
	}






	/**
	 * @return the dashpattern
	 */
	public float[] getDashpattern() {
		return dashpattern;
	}











	/**
	 * Changes the width to a given style and returns it modified
	 * @param The width to change
	 * @return the modified version of the Style
	 */
	public LineStyle withWidth(float width){
		return new LineStyle(width, this.color, this.linecap, this.linejoin, this.dashpattern);
	}




	/**
	 * Changes the color to a given style and returns it modified
	 * @param The color to change
	 * @return the modified version of the Style
	 */
	public LineStyle withColor(Color color){
		return new LineStyle(this.width, color, this.linecap, this.linejoin, this.dashpattern);
	}




	/**
	 * Changes the LineCap to a given style and returns it modified
	 * @param The LineCap to change
	 * @return the modified version of the Style
	 */
	public LineStyle withLineCap(LineCap linecap){
		return new LineStyle(this.width, this.color, linecap, this.linejoin, this.dashpattern);
	}




	/**
	 * Changes the LineJoin to a given style and returns it modified
	 * @param The LineJoin to change
	 * @return the modified version of the Style
	 */
	public LineStyle withLineJoin(LineJoin linejoin){
		return new LineStyle(this.width, this.color, this.linecap, linejoin, this.dashpattern);
	}




	/**
	 * Changes the LineCap to a given style and returns it modified
	 * @param The LineCap to change
	 * @return the modified version of the Style
	 */
	public LineStyle withDashPattern(float[] dashpattern){
		return new LineStyle(this.width, this.color, this.linecap, this.linejoin, dashpattern);
	}
}
