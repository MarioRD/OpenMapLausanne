package ch.epfl.imhof;


/**
 * @since Java 1.8.0
 * @author Mario Robert D'Ambrosio (249757)
 * @author Nicodème Stalder (234584)
 * 
 * <p><b>Class that represents a three dimensional vector </b></p>
 *
 */
public class Vector3 {


	/**
	 *The vetor's three components (x,y,z axis)
	 */
	private final double x, y, z;


	/**
	 * Main constructor 
	 * @param x the x-axis of the vector
	 * @param y the y-axis of the vector
	 * @param z the z-axis of the vector
	 */
	public Vector3(double x, double y, double z){
		this.x=x;
		this.y=y;
		this.z=z;
	}


	/**
	 * 
	 * @return the vector's norm
	 */
	public double norm(){
		return Math.sqrt(scalarProduct(this));
	}


	/**
	 * 
	 * @return the vector normalized s a Vector3 object
	 */
	public Vector3 normalized(){
		final double norm=norm();
		return new Vector3(this.x/norm, this.y/norm, this.z/norm);
	}


	/**
	 * 
	 * @param v_2
	 * @return the scalar product between <code>this.Vector3</code> and another vector <code>v_2</code>
	 */
	public double scalarProduct(Vector3 v_2){

		return ( this.x * v_2.x) + 
				(this.y * v_2.y) + 
				(this.z * v_2.z); 

	}
}
