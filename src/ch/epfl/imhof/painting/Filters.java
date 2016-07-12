package ch.epfl.imhof.painting;

import java.util.function.Predicate;

import ch.epfl.imhof.Attributed;
import ch.epfl.imhof.Attributes;

public class Filters
{
	private Filters()
	{
		throw new RuntimeException("YOU SHALL NOT INSTANTIATE!");
	}

	public static Predicate<Attributed<?>> tagged(String name)
	{
		Predicate<Attributed<?>> predicate = (a) -> a.attributes().contains(name);
		return predicate;
	}

	public static <N> Predicate<Attributed<?>> tagged(String name, String... values)
	{
		Predicate<Attributed<?>> predicate = (a) -> 
		{
			Attributes att = a.attributes();
			for(int i=0; i<values.length; ++i)
			{
				if (att.contains(name))
				{
					if(att.get(name).equals(values[i]))
					{
						return true;
					}
				}
			}
			return false;
		};

		return predicate;
	}
	
	public static Predicate<Attributed<?>> onLayer(int number)
	{
		Predicate<Attributed<?>> predicate = (n) -> 
		{
			String s = Integer.toString(number);
			return s.equals(n.attributes().get("layer","0"));
		};
		
		return predicate;
	}

}
