package ch.epfl.imhof.painting;

/**
 * @author Mario Robert D'Ambrosio (249757)
 * @author Nicodème Stalder (234584)
 *
 * Java2DCanvas is a class that is useful to draw polyLines and polyGons on an inside stored image that you can get with a getter method.
 */

import java.util.Iterator;
import java.util.List;
import java.util.function.Function;

import ch.epfl.imhof.geometry.ClosedPolyLine;
import ch.epfl.imhof.geometry.Point;
import ch.epfl.imhof.geometry.PolyLine;
import ch.epfl.imhof.geometry.Polygon;

import java.awt.image.BufferedImage;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.BasicStroke;
import java.awt.Shape;
import java.awt.geom.Path2D;
import java.awt.geom.Area;

/**
 * Java2DCanvas is a class that is useful to draw polyLines and polyGons on an inside stored image that you can get with a getter method.
 * 
 * @attributes image which represent the image we are drawing.
 * @attributes xDilatation the x dilatation that result from the use of scale method
 * (scale is used because we want to draw as if the DPI of the image were the DPI specified in the constructor)
 * @attributes yDilatation the x dilatation that result from the use of scale method.
 * (scale is used because we want to draw as if the DPI of the image were the DPI specified in the constructor)
 * @attributes T a functional interface that transform a point in the coordinate system of P_BL and P_TR into the image coordinate system.
 * @attributes graphics2D object take from the createGraphics method from this.image, which stores the drawing settings and is used to draw
 * @attributes Graphics2D_DPI is the default value of dpi of this.image
 *
 */
public final class Java2DCanvas implements Canvas {

	private final double dilatation;
	private final Function <Point,Point> T; // transform a point into new coordinated;
	private BufferedImage image;
	private Graphics2D graphics2D;
	public final static int Graphics2D_DPI = 72; //Image resolution of java image

	/**
	 * 
	 * @param p_BL point corresponding to the bottom left position on the image.
	 * @param p_TR point corresponding to the top right position on the image.
	 * @param width is he width of the image in pixels
	 * @param height is the height of the frame in pixels
	 * @param dPI : Java2DCanvas will draw as if the DPI of the frame were these specified dpi
	 * @param color the color of the background of the image.
	 */
	public Java2DCanvas(Point p_BL, Point p_TR, int width, int height, int dPI, Color color)
	{
		dilatation = (double)dPI/(double)Graphics2D_DPI;
		T = Point.alignedCoordinateChange(p_BL, new Point(0,height/dilatation), p_TR, new Point(width/dilatation,0));
		image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		graphics2D=image.createGraphics();
		graphics2D.setColor(color.javaColor());
		graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
		graphics2D.fillRect(0,0,width,height);
		graphics2D.scale(dilatation,dilatation);
	}

	/**
	 * Draw a polyLine with a given lineStyle on this.image
	 * @param polyLine : the polyLine we'll draw
	 * @param lineStyle : the parameters of the drawing
	 */
	public void drawPolyLine(PolyLine polyLine,LineStyle lineStyle)
	{
		// **** Part 1 : Set the drawing parameters

		//Extract cap from lineStyle
		int cap=0;
		switch(lineStyle.getLinecap())
		{
		case Butt : cap = BasicStroke.CAP_BUTT;
		break;
		case Round : cap = BasicStroke.CAP_ROUND;
		break;
		case Square : cap = BasicStroke.CAP_SQUARE;
		}

		//Extract join from lineStyle
		int join=0;
		switch(lineStyle.getLinejoin())
		{
		case Bevel : join = BasicStroke.JOIN_BEVEL;
		break;
		case Miter : join = BasicStroke.JOIN_MITER;
		break;
		case Round : join = BasicStroke.JOIN_ROUND;
		}

		//Set the stroke (using also cap and join to set it)
		Stroke stroke;
		if (lineStyle.getDashpattern().length==0)
		{
			stroke=new BasicStroke(lineStyle.getWidth(),cap,join,10.0f);
		}
		else
		{
			stroke=new BasicStroke(lineStyle.getWidth(),cap,join,10.0f,lineStyle.getDashpattern(),0.0f);
		}
		graphics2D.setStroke(stroke);

		//Set the color taken from lineStyle	
		graphics2D.setColor(lineStyle.getColor().javaColor());
		
		// **** Part 2 : Creating and drawing the polyLine
		
		//Create the shape associated with the polyLine we'll draw using a private method of the class
		Shape shape = polyLineToShape(polyLine);

		//Draw the shape of the polyLine
		graphics2D.draw(shape);

	}
	
	/**
	 * Draw a polyGon with a given color on this.image
	 * @param polyGon : the polyGon we'll draw
	 * @param color : the color of the drawing
	 */
	public void drawPolygon(Polygon polyGon,Color color)
	{
		// **** Part 1 : Set the drawing parameter
		
		// Set the color
		graphics2D.setColor(color.javaColor());
		
		
		// **** Part 2 : Creating and drawing the polyGon
		
		//Create an Area object representing the shell of the polyGon and soon, the polyGon itself
		Area aera = new Area(polyLineToShape(polyGon.shell()));
		
		//subtract the holes to the area to make it represent the polyGon
		List<ClosedPolyLine> holes = polyGon.holes();
		for(ClosedPolyLine hole: holes)
		{
			aera.subtract(new Area(polyLineToShape(hole)));
		}
		
		//Draw the polyGon
		graphics2D.fill(aera);

	}
	
	/**
	 * @return this.image, the image the class is drawing on.
	 */
	public BufferedImage image()
	{
		return image;
	}

	/**
	 * @param polyLine a given object of the class PolyLine which represent a polyLine
	 * @return an object of type Shape which represent the given polyLine
	 */
	private Shape polyLineToShape(PolyLine polyLine)
	{
		Path2D.Double shape = new Path2D.Double();
		//System.out.println(polyLine.firstPoint().x() + " " + polyLine.firstPoint().y());
		Point currentPoint = T.apply(polyLine.firstPoint());
		shape.moveTo(currentPoint.x(), currentPoint.y());

		Iterator<Point> p = polyLine.points().iterator();
		p.next();
		while(p.hasNext())
		{
			currentPoint=T.apply(p.next());
			shape.lineTo(currentPoint.x(),currentPoint.y());
		}

		if(polyLine.isClosed())
		{
			shape.closePath();
		}

		return shape;
	}

}
