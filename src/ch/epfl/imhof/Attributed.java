package ch.epfl.imhof;

/**
 * @author Mario Robert D'Ambrosio (249757)
 * @author Nicodème Stalder (234584)
 * 
 * --A class that lets us relate attributes to various geometry-related classes types (Polygon,ecc..)--
 */

/**
 * --A class that lets us relate attributes to various geometry-related classes types (Polygon,ecc..)--
 * @constructor defines our two variables: the value type and the attributes that must be attached
 * @param value the value type for the class (ex.: 'Polygon')
 * @param attributes the attributes the user wants to attach to the value type
 */
public final class Attributed<T>

{
	private final T value;
	private final Attributes attributes;

	public Attributed(T value, Attributes attributes)
	{
		final T myvalue=value;
		final Attributes myattributes= attributes;
		this.value=myvalue;    
		this.attributes=myattributes;
	}

	/**
	 * @return the value of the class
	 */
	public T value(){
		return value;
	}

	/**
	 * @return our attributes that must be attached to the value type
	 */
	public Attributes attributes(){
		return attributes;
	}

	/**
	 * @param attributeName the attribute which we want to check
	 * @return returns true only if the class has the attribute 'attributeName'
	 */
	public boolean hasAttribute(String attributeName) {

		return attributes.contains(attributeName);
	}    

	/**
	 * 
	 * @param attributeName the attribute which we want to check
	 * @return returns he value of the attribute or null if it doesn't exist
	 * 
	 */
	public String attributeValue(String attributeName) {
		return attributes.get(attributeName);
	}

	/**
	 * 
	 * @param attributeName the name of the attribute we want to verify
	 * @param defaultValue the default value that should be returned in case 'attributeName' does not exist
	 * @return either the attibute's value either the default value
	 */
	public String attributeValue(String attributeName, String defaultValue) {
		return attributes.get(attributeName, defaultValue);
	}

	/**
	 * 
	 * @param attributeName the name of the attribute we want to verify
	 * @param defaultValue the default value that should be returned in case the value does not exist
	 * @return either the attibute's integer value either the default value (when an integer is not the value or the value doesn't exist)
	 */
	public int attributeValue(String attributeName, int defaultValue) {
		return attributes.get(attributeName, defaultValue);
	}

}
