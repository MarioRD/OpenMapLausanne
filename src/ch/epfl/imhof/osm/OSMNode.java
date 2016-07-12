package ch.epfl.imhof.osm;

/**
 * @author Mario Robert D'Ambrosio (249757)
 * @author Nicodème Stalder (234584)
 * 
 * class that stores a position in order to represent a Open Street Map node.
 */

import ch.epfl.imhof.Attributes;
import ch.epfl.imhof.PointGeo;

/**
 * class that stores a position in order to represent a Open Street Map node.
 * @param position, which stores the localisation of the point we are representing
 */
public final class OSMNode extends OSMEntity{

	private final PointGeo position; 

	public OSMNode(long id, PointGeo position, Attributes attributes)
	{
		super(id,attributes);
		this.position=position;
	}

	/**
	 * @return position, the position of the OSMNode.
	 */
	public PointGeo position()
	{
		return position;
	}

	/**
	 * class that can be used to iteratively create a OSMNode Object
	 * @param position, which is the position of the future OSMNode we'll create.
	 */
	static public final class Builder extends OSMEntity.Builder
	{
		PointGeo position;

		public Builder(long id, PointGeo position)
		{
			super(id);
			this.position=position;
		}

		/**
		 * @return a OSMNode object we were building.
		 */
		public OSMNode build()
		{
			if (super.isIncomplete())
			{
				throw new IllegalStateException();
			}

			return new OSMNode(super.id,position,super.attributes.build());

		}

	}

}
