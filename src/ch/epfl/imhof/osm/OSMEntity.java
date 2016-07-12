package ch.epfl.imhof.osm;

/**
 * @author Mario Robert D'Ambrosio (249757)
 * @author Nicodème Stalder (234584)
 *
 * abstract class that define the general elements of an Open Street Map entity such has an id ans attributes.
 */

import ch.epfl.imhof.Attributes;

/**
 * abstract class that define the general elements of an Open Street Map entity such has an id ans attributes.
 * 
 * @param id is a unique identifiant of type long of the OSM entity object.
 * @param attributes contains the possible attributes of the OSM entity.
 */
public abstract class OSMEntity

{

	private final long id;
	private final Attributes attributes;

	public OSMEntity(long id, Attributes attributes)
	{
		this.id=id;
		this.attributes=attributes;
	}

	/**
	 * @return this.id, the identifiant of this OSM entity.
	 */
	public long id()
	{
		return id;
	}

	/**
	 * @return this.attributes, the list of attributes of this OSM entity.
	 */
	public Attributes attributes()
	{
		return attributes;
	}

	/**
	 * @param key, we want to know if the attributes list contains this key.
	 * @return true if the attributes list contains this key, false otherwise.
	 */
	public boolean hasAttribute(String key)
	{
		return attributes.contains(key);
	}

	/**
	 * @param key, we want to return the associated element of this key in attributes, if this key exist in attributes.
	 * @return the the associated element of this key in attributes or null if this key does not exist in attributes.
	 */
	public String attributeValue(String key)
	{
		return attributes.get(key);
	}

	/**
	 * -- class that can be used to iteratively create an OSMEntity object.
	 * @param id, which is the identifiant of the future OSMEntity object.
	 * @param attributes which contains the possible attributes of the future OSM entity.
	 */
	public static abstract class Builder
	{
		public long id;
		public Attributes.Builder attributes = new Attributes.Builder();
		private boolean isIncomplete = false;

		public Builder(long id)
		{
			if(Long.valueOf(id)==null)System.out.println("-----Null element");
			this.id = id;
		}

		/**
		 * @param key, which will be added in the attributes list associated with value
		 * @param value, which will be added in the attributes list associated with key
		 */
		public void setAttribute(String key, String value)
		{
			attributes.put(key,value);
		}

		/**
		 * if used, this method prevent this builder class to be enable to generate a OSMEntity object.
		 */
		public void setIncomplete()
		{
			isIncomplete=true;
		}
		/**
		 * @return true if this builder class is enable to generate a OSMEntity object.
		 */
		public boolean isIncomplete()
		{
			return isIncomplete;
		}

	}

}
