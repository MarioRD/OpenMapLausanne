package ch.epfl.imhof.osm;

/**
 * @author Mario Robert D'Ambrosio (249757)
 * @author Nicodème Stalder (234584)
 *
 * -- class that stores OSMWays and OSMRelations object to represent a map.
 *
 */

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * -- class that stores OSMways and OSMRelations to form a map.
 * @param ways, which is a list of the OSMWays contained in the map.
 * @param relations, which a list of the OSMRelations contained in the map. 
 */
public final class OSMMap

{

	private final List<OSMWay> ways;
	private final List<OSMRelation> relations;
	
	public OSMMap(Collection<OSMWay> ways, Collection<OSMRelation> relations)
	{
		this.ways = Collections.unmodifiableList(new ArrayList<>(ways));
		this.relations = Collections.unmodifiableList(new ArrayList<>(relations));
	}
	
	/**
	 * @return ways, the list of OSMWays stored in the map.
	 */
	public List<OSMWay> ways()
	{
		return this.ways;
	}
	
	/**
	 * @return relations, the list of OSMRelations stored in the map.
	 */
	public List<OSMRelation> relations()
	{
		return this.relations;
	}
	
	/**
	 * class that can be used to iteratively create an OSMMap object.
	 * @param nodes, which is a Map of identifiants (keys) to OSMNodes objects. we'll use these OSMNodes to create ways and relations.
	 * @param ways, which is a Map of identifiants (keys) to OSMWays objects. These OSMWays objects will used to build a OSMMap object.
	 * @param relations, which is a Map of identifiants (keys) to OSMRelations objects. These OSRelations objects will used to build a OSMMap object.
	 */
	public static final class Builder
	{
		private Map<Long,OSMNode> nodes = new HashMap<Long,OSMNode>();
		private Map<Long,OSMWay> ways = new HashMap<Long,OSMWay>();
		private Map<Long,OSMRelation> relations = new HashMap<Long,OSMRelation>();
		
		/**
		 * @param newNode, which is a node that will be added to the class attribute nodes.
		 */
		public void addNode(OSMNode newNode)
		{
			nodes.put(newNode.id(), newNode);
		}
		
		/**
		 * @param id, which is the identifiant of the node we want to return.
		 * @return the node of specified identifiant contained the map or null if it doesn't exist.
		 */
		public OSMNode nodeForId(long id)
		{
			return nodes.get(id);
		}
		
		/**
		 * @param newWay, which is a way that will be added to the class attribute ways.
		 */
		public void addWay(OSMWay newWay)
		{
			ways.put(newWay.id(), newWay);
		}

		/**
		 * @param id, which is the identifiant of the way we want to return.
		 * @return the way of specified identifiant contained in the map or null if it doesn't exist.
		 */
		public OSMWay wayForId(long id)
		{
			return ways.get(id);
		}

		/**
		 * @param newRelation, which is a relation that will be added to the class attribute relations.
		 */
		public void addRelation(OSMRelation newRelation)
		{
			relations.put(newRelation.id(),newRelation);
		}
		
		/**
		 * @param id, which is the identifiant of the relation we want to return.
		 * @return the relation of specified identifiant contained in the map or null if it doesn't exist.
		 */
		public OSMRelation relationForId(long id)
		{
			return relations.get(id);
		}

		/**
		 * @return an object of type OSMMap which is the map we were building with this building class.
		 */
		public OSMMap build()
		{
			return new OSMMap(ways.values(), relations.values());
		}
		
	}
	
}