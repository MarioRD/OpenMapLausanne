package ch.epfl.imhof;

/**
 * @author Mario Robert D'Ambrosio (249757)
 * @author Nicodème Stalder (234584)
 *
 *--This class sets the attributes that must be attached to an object (eventually with the class Attributed)--
 */

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.lang.Integer;

/**
 * --This class sets the attributes that must be attached to an object (eventually with the class Attributed)--
 * @param attributes the constructor uses attributes to create a new attributes map
 */
public final class  Attributes

{
	private final Map<String, String> attributes;

	public Attributes(Map<String, String> attributes)
	{
		this.attributes = new HashMap<String, String>(attributes);
	}


	/**
	 * @return true if the variable attributes is empty, false in the other case
	 */
	public boolean isEmpty()
	{
		return attributes.isEmpty();
	}

	/**
	 * @param key the key we want to verify
	 * @return checks whether attributes contains the key or not
	 */
	public boolean contains(String key)
	{
		return attributes.containsKey(key);
	}    

	/**
	 * @param key the key we are interested in finding
	 * @return gives out the value associated with key, null if there is no key 
	 */
	public String get(String key)
	{
		return attributes.get(key);
	}

	/**
	 * @param key the key we are interested in finding
	 * @param defaultValue the default value we'd like the code to return
	 * @return gives out the value associated with key, defaultValue if there is no key value
	 */
	public String get(String key, String defaultValue)
	{ 
		return attributes.getOrDefault(key, defaultValue);
	}

	/**
	 * @param key the key we are interested in finding
	 * @param defaultValue the default value that should be returned in case the value does not exist or it is not an integer
	 * @return either the key's integer value either the default value (when an integer is not the value or the value doesn't exist)
	 */
	public int get(String key, int defaultValue) {
		if (attributes.containsKey(key))
		{
			int entierKey;
			try {
				entierKey = Integer.parseInt(get(key)); 
			} catch(NumberFormatException e) {
				return defaultValue;
			}
			return entierKey;
		}

		return defaultValue;
	}

	/**
	 * @param keysToKeep the keys the method must keep in the new version of the attributs
	 * @return the new attributes version with only the keys desired that were contained in the previous attributes variable
	 */
	public Attributes keepOnlyKeys(Set<String> keysToKeep)
	{ 

		Attributes.Builder tmp = new Attributes.Builder();

		for(String s : keysToKeep)
		{
			if (attributes.containsKey(s))
			{
				tmp.put(s,attributes.get(s));
			}
		}

		return tmp.build();
	}

	/**
	 * --This class is fundamental to build the Attributes class--
	 * It gives out two new methods:
	 * @method put to put any key and value in a new attributes variable
	 * @method build to construct the class with that particular variable just builded
	 */
	public final static class Builder
	{
		private Map<String, String> attributes = new HashMap<>();

		public void put(String key, String value)
		{
			attributes.put(key , value);
		}

		public Attributes build()
		{
			return new Attributes (attributes);
		}

	}

}