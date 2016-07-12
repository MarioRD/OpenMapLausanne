package ch.epfl.imhof;

/**
 * @author Mario Robert D'Ambrosio (249757)
 * @author Nicodème Stalder (234584)
 * 
 * --Generic class that associate some elements with others: each element is associated with a set of other elements using a Map.
 */

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Generic class that associate some elements with others: each element is associated with a set of other elements using a Map.
 * It will be used to represent a graph of nodes. Each node is associated with a set neighbors.
 * @param neighbors which is a Map associating a object of generic type <N> with a Set of object of generic type <N>.
 */
public class Graph<N>

{
	private final Map<N, Set<N>> neighbors;

	public Graph(Map<N, Set<N>> neighbors)
	{
		Map<N, Set<N>> finalNeighbors = new HashMap<>();

		for(Map.Entry<N, Set<N>> entry : neighbors.entrySet())
		{
			finalNeighbors.put(entry.getKey(), Collections.unmodifiableSet(new HashSet<>(entry.getValue())));		
		}
		this.neighbors = Collections.unmodifiableMap(finalNeighbors);
	}

	/**
	 * @return a set containing all the generic objects of type <N> which are contained in neighbors.
	 */
	public Set<N> nodes()
	{
		return neighbors.keySet();
	}

	/**
	 * @param node, the element we want to retrieve the neighbors.
	 * @return a set containing all the objects of type <N> associated with the object called node in the Map class attribute neighbors.
	 */
	public Set<N> neighborsOf(N node)
	{
		if(!neighbors.containsKey(node)){
			throw new IllegalArgumentException();
		}
		return neighbors.get(node);
	}

	/**
	 * Builder of the graph class. We can use it to build iteratively a Grap object.
	 * @param neighbors which is a Map associating a object of generic type <N> with a Set of object of generic type <N>.
	 */
	public static class Builder<N>
	{
		private Map<N, Set<N>> neighbors= new HashMap<>();

		public void addNode(N n)
		{
			if (!neighbors.containsKey(n))
			{
				neighbors.put(n, new HashSet<N>());
			}
		}
		/**
		 * @param n1 , object of type <N> that will be associated with the object of type <N> n2.
		 * @param n2 , object of type <N> that will be associated with the object of type <N> n1.
		 */
		public void addEdge(N n1, N n2)
		{
			if(!(neighbors.containsKey(n1) && neighbors.containsKey(n2)))
			{
				throw new IllegalArgumentException();
			}

			neighbors.get(n1).add(n2);
			neighbors.get(n2).add(n1);
		}


		/**
		 * @return the object of type Graph we were creating with this builder class.
		 */
		public Graph<N> build()
		{
			return new Graph<N>(neighbors);
		}



	}
}
