package ch.epfl.imhof.dem;
/**
 * @since Java 1.8.0
 * @author Mario Robert D'Ambrosio (249757)
 * @author Nicodème Stalder (234584)
 *<p></p>
 * class that contains a method that return an image of the relief of a specified location 
 * by using a light vector and a digital elevation model
 */
import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.io.File;
import java.util.function.Function;
import javax.imageio.ImageIO;

import ch.epfl.imhof.PointGeo;
import ch.epfl.imhof.Vector3;
import ch.epfl.imhof.geometry.Point;
import ch.epfl.imhof.projection.Projection;


/**
 * class that contains a method that return a blurred image of the relief of a specified location by using a light vector and a digital elevation model
 */
public class ReliefShader {

	Projection projection;
	DigitalElevationModel dEM;
	Vector3 lightVector;
	
	public ReliefShader(Projection projection,DigitalElevationModel dEM ,Vector3 lightVector)
	{
		this.projection = projection;
		this.dEM = dEM;
		this.lightVector = lightVector;
	}
	
	/**
	 * method that return a blurred image of the relief of the specified location of the coordinate system given to the constructor
	 */
	public BufferedImage shadedRelief(Point p_BL, Point p_TR, int width, int height, double r) throws Exception
	{
		
		if(r==0)
		{
			final Function<Point,Point> pixelPointToPoint = Point.alignedCoordinateChange(new Point(0,height), p_BL, new Point(width,0), p_TR);
			final Function<Point,PointGeo> pixelPointToPointGeo = (point) -> projection.inverse(pixelPointToPoint.apply(point));
			return drawGrossShadedRelief(width,height,pixelPointToPointGeo);
		}
		
		float[] kernel = kernel(r);
		
		int border = (kernel.length-1)/2;
		final Function<Point,Point> pixelPointToPoint = Point.alignedCoordinateChange(new Point(border,height+border), p_BL, new Point(width+border,border), p_TR);
		final Function<Point,PointGeo> pixelPointToPointGeo = (point) -> projection.inverse(pixelPointToPoint.apply(point));
		BufferedImage imageTMP = drawGrossShadedRelief(width+2*border,height+2*border,pixelPointToPointGeo);
		ImageIO.write(imageTMP, "png", new File("ns"));
		return blurredPicture(imageTMP,kernel).getSubimage(border, border, width, height);
	}
	
	/**
	 * return an image of the relief that is not blurred
	 */
	private BufferedImage drawGrossShadedRelief(int width, int height, Function<Point,PointGeo> pixelPointToPointGeo) throws Exception
	{
		BufferedImage image = new BufferedImage(width,height, BufferedImage.TYPE_INT_RGB);
		for(int y=0; y<height; ++y)
		{
			for(int x=0; x<width; ++x)
			{
				image.setRGB(x,y,colorAtPointGeo(pixelPointToPointGeo.apply(new Point(x,y))));
			}
		}
		
		return image;
	}
	/**
	 * methode that calculate the color of a point relatively to the relief caracteristics
	 */
	private int colorAtPointGeo(PointGeo pointGeo) throws Exception
	{
		Vector3 vector = dEM.normalAt(pointGeo);
		double cosTheta = vector.normalized().scalarProduct(lightVector.normalized());
		int r = (int)(((0.5*(cosTheta+1)))*255.9999);
		int g = r;
		int b = (int)(((0.5*(0.7*cosTheta+1)))*255.9999);
		
		return (r<<16|g<<8|b);
	}
	/**
	 * @return a float array that contains the blurring coefficients
	 */
	private float[] kernel(double r)
	{
		double sigma = r/3;
		int rCeiled = (int)Math.ceil(r);
		int border = rCeiled;
		int n = 2*rCeiled+1;
		float[] kernel = new float[n];

		double sum = 0;
		for (int i=0; i<n; ++i)
		{
			kernel[i] = (float)Math.exp(-Math.pow((-border+i),2)/(2*(Math.pow(sigma,2))));
			sum += kernel[i];
		}

		for (int i=0; i<n; ++i)
		{
			kernel[i] /= sum;
		}

		return kernel;

	}
	/**
	 * take an image and a float array that contains blurring varacteristics and blurre the given image
	 */
	private BufferedImage blurredPicture(BufferedImage image,float[] kernel)
	{
		Kernel xKernel = new Kernel(kernel.length,1,kernel);
		Kernel yKernel = new Kernel(1,kernel.length,kernel);
		
		ConvolveOp xBlurring = new ConvolveOp(xKernel, ConvolveOp.EDGE_NO_OP, null);
		ConvolveOp yBlurring = new ConvolveOp(yKernel, ConvolveOp.EDGE_NO_OP, null);
		
		image = xBlurring.filter(image,null);
		image = yBlurring.filter(image,null);

		return image;
	}

}
