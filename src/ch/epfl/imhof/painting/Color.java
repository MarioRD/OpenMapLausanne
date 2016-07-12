 package ch.epfl.imhof.painting;
 

/**
 * @since Java 1.8.0
 * @author Mario Robert D'Ambrosio (249757)
 * @author Nicodème Stalder (234584)
 * 
 *<pre><p><i><b>Represents a RGB Color (alpha=1.0) </b></i></p></pre>
 *
 *@see java.awt.Color Java's official class for handling colors
 */
public class Color
{
	
	/**
	 * The RGB Color value for RED  
	 * @see #BLACK 
	 * @see #WHITE
	 * @see #GREEN
	 * @see #BLUE
	 */
	public final static Color RED = new Color(0xFF_00_00);

	
	/**
	 * The RGB Color value for GREEN 
	 * @see #BLACK 
	 * @see #WHITE
	 * @see #RED
	 * @see #BLUE 
	 */
	public final static Color GREEN = new Color(0x00_FF_00);

	
	/**
	 * The RGB Color value for BLUE 
	 * @see #BLACK 
	 * @see #WHITE
	 * @see #RED
	 * @see #GREEN
	 */
	public final static Color BLUE = new Color(0x00_00_FF);

	
	/**
	 * The RGB Color value for BLACK 
	 * @see #WHITE 
	 * @see #RED
	 * @see #GREEN
	 * @see #BLUE 
	 */
	public final static Color BLACK = new Color(0x00_00_00);

	
	/**
	 * The RGB Color value for WHITE 
	 * @see #BLACK 
	 * @see #RED
	 * @see #GREEN
	 * @see #BLUE
	 */
	public final static Color WHITE = new Color(0xFF_FF_FF);

	/**
	 * The color value of the class <a href="#Color">Color</a>  
	 * @see #Color
	 */
	final private int color;

	/**
	 * A mask filter initialized as the integer <code>0x00_00_FF</code>
	 */
	final private static int MASK=0x00_00_FF;



	/**
	 * Constructs a Color with its integer value
	 * @param color the integer that defines the Color
	 * @Example <code>Color(0xFF_FF_FF)</code> creates a white color
	 * <p>  	(<code>16777215</code> in decimal value)</p>
	 * 
	 */
	private Color(int color)
	{
		this.color = color;
	}




	/**
	 * Constructs a B/W Color (by only using one component)
	 * @param gr the B/W component value
	 * @return a new B/W Color with three components (i.e. RGB)  
	 * @throws IllegalArgumentException if the value is out of boundary 
	 * 		   <p>(i.e. if the parameter is 0< or >1)</p>
	 */
	static public Color gray(double gr) throws IllegalArgumentException
	{
		if (gr<0 || gr>1)
		{
			throw new IllegalArgumentException("Gray value out of range (0< V >1)");
		}

		int r = (int) (gr * 255.9999);
		int g = (int) (gr * 255.9999);
		int b = (int) (gr * 255.9999);

		return new Color(r<<16|g<<8|b);

	}


	/**
	 * 
	 * @param r the RED component
	 * @param g the GREEN component
	 * @param b the blue component
	 * @return a new RGB Color with the r,g and b parameters initialized as components
	 * @throws IllegalArgumentException <b>r</b>,<b>b</b> or <b>g</b> 0< or >1
	 * @see #rgb(int color)
	 */
	static public Color rgb(double r, double g, double b) throws IllegalArgumentException
	{
		if (r<0 || r>1)
		{
			throw new IllegalArgumentException("Red value out of range (0< V >1) ");
		}
		if (g<0 || g>1)
		{
			throw new IllegalArgumentException("Green value out of range (0< V >1) ");
		}
		if (b<0 || b>1)
		{
			throw new IllegalArgumentException("Blue value out of range (0< V >1) ");
		}

		int r1 = (int) (r * 255.9999);
		int g1 = (int) (g * 255.9999);
		int b1 = (int) (b * 255.9999);

		return new Color(r1<<16|g1<<8|b1);

	}



	/**
	 * 
	 * @param color the integer value of the color
	 * @return the Color constructed with the integer passed as parameter
	 * @throws IllegalArgumentException
	 * @see #rgb(double r, double g, double b)
	 */
	static public Color rgb(int color) throws IllegalArgumentException
	{
		if(color>=0x01_00_00_00)
		{
			throw new IllegalArgumentException("Value out of range (>255)");
		}

		return new Color(color);

	}

	
	/**
	 * Getter for the RED component
	 * @return the red component as double (<i>0<=<b>r</b><=1</i>)
	 * 
	 * @see #green
	 * @see #blue
	 */
	public double red()
	{
		return ((double) (this.color>>16 & MASK) / 255d);
	}


	
	/**
	 * Getter for the GREEN component
	 * @return the green component as double (<i>0<=<b>g</b><=1</i>)
	 * 
	 * @see #red
	 * @see #blue
	 */
	public double green()
	{
		return ((double) (this.color>>8 & MASK) / 255d);
	}


	
	/**
	 * Getter for the BLUE component
	 * @return the blue component as double (<i>0<=<b>b</b><=1</i>)
	 * 
	 * @see #red 
	 * @see #green
	 */
	public double blue()
	{
		return ((double) (this.color & MASK ) / 255d);
	}


	
	/**
	 * 
	 * @param c the second color to multiply the values with
	 * @return a new RGB Color with the three components multiplied with each other
	 * @see #rgb(double r, double g, double b)
	 */
	public Color multiplication(Color c)
	{
		double r = red()*c.red();
		double g = green()*c.green();
		double b = blue()*c.blue();

		return rgb( r, g , b );
	}


	/**
	 * Transforms a <a href="#Color">Color</a> Object to a Java Color Object instance
	 * @see java.awt.Color
	 * @return the color as instance of the java.awt.Color Class
	 */
	public java.awt.Color javaColor()
	{
		return new java.awt.Color( (float) red() , 
				(float) green() , 
				(float) blue());
	}
}

