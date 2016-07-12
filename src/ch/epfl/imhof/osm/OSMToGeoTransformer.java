




package ch.epfl.imhof.osm;

import ch.epfl.imhof.Attributed;
import ch.epfl.imhof.Attributes;
import ch.epfl.imhof.Graph;
import ch.epfl.imhof.Map;
import ch.epfl.imhof.geometry.ClosedPolyLine;
import ch.epfl.imhof.geometry.Point;
import ch.epfl.imhof.geometry.PolyLine;
import ch.epfl.imhof.geometry.Polygon;
import ch.epfl.imhof.osm.OSMRelation.Member;
import ch.epfl.imhof.projection.Projection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * @author Mario Robert D'Ambrosio (249757)
 * @author Nicodème Stalder (234584)
 *
 */
public final class OSMToGeoTransformer {

	private final static Set<String>  PolyLineKeys = new HashSet<String>(Arrays.asList
			("bridge", "highway", "layer", "man_made", "railway",
					"tunnel", "waterway"));

	private final static Set<String>  PolygonKeys = new HashSet<String>(Arrays.asList
			("building", "landuse", "layer", "leisure", "natural",
					"waterway"));


	private final Projection projection;
	public OSMToGeoTransformer(Projection projection){
		this.projection=projection;
	}

	public Map transform(OSMMap map){



		final Map.Builder mapToReturn = new Map.Builder();


		putWaysInMap(map,mapToReturn);
		map.relations().forEach(e->putRelationsInMap(assemblePolygon(e,e.attributes()),mapToReturn));


		return mapToReturn.build();
	}


/**
 * It puts all the Polygons of a List in a map and returns them 
 * @param polygons
 * @param mapToReturn
 */
	private void putRelationsInMap(List<Attributed<Polygon>> polygons, Map.Builder mapToReturn){
		polygons.forEach(e-> 
		mapToReturn.addPolygon(e));
	}
	private List<ClosedPolyLine> ringsForRole(OSMRelation relation, String role){
		List<ClosedPolyLine> ringsToReturn = new ArrayList<>();
		Set<OSMWay> ways = waysOfRelation(relation, role);
		Graph<OSMNode> graph = graphOfWays(ways);
		if(!nodesOfGraphHaveTwoNeighbors(graph))
		{
			return ringsToReturn;
		}
		return ringsOfGraph(graph);


	}



	private List<ClosedPolyLine> ringsOfGraph(Graph<OSMNode> graph)
	{
		Set<OSMNode> nodes = new HashSet<OSMNode>(graph.nodes());
		Set<OSMNode> nodesRemoved = new HashSet<>();
		List<ClosedPolyLine> rings = new ArrayList<>();


		Iterator<OSMNode> i = nodes.iterator();

		OSMNode firstNode;
		List<Point> points;
		try{
			firstNode = i.next();


			do
			{


				points = new ArrayList<>();
				points.add(projection.project(firstNode.position()));

				Set<OSMNode> neighborsOfFirstNode = graph.neighborsOf(firstNode);
				Iterator<OSMNode> j = neighborsOfFirstNode.iterator();
				OSMNode currentNode = j.next();
				points.add(projection.project(currentNode.position()));
				nodesRemoved.add(currentNode);

				Set<OSMNode> neighborsOfSecondNode = graph.neighborsOf(currentNode);
				Iterator<OSMNode> k = neighborsOfSecondNode.iterator();
				currentNode = k.next();
				if(currentNode==firstNode)
				{
					currentNode=k.next();
				}



				while(currentNode!=firstNode)
				{
					points.add(projection.project(currentNode.position()));
					nodesRemoved.add(currentNode);
					currentNode=neighborOfNodeNotInSet(currentNode,nodesRemoved,graph);
				}


				nodesRemoved.add(currentNode);



				do
				{
					if(i.hasNext())
					{
						firstNode = i.next();
					}
				}while(i.hasNext() && nodesRemoved.contains(firstNode));
				rings.add(new ClosedPolyLine(points));
			}while(i.hasNext());

		}catch(Exception e){}


		return rings;


	}


	private OSMNode neighborOfNodeNotInSet(OSMNode node, Set<OSMNode> nodes, Graph<OSMNode> graph)
	{


		Set<OSMNode> neighbors = graph.neighborsOf(node);

		Iterator<OSMNode> i = neighbors.iterator();

		OSMNode nodeTMP = i.next();


		if(!nodes.contains(nodeTMP))
		{
			return nodeTMP;
		}

		return i.next();

	}


	private Set<OSMWay> waysOfRelation(OSMRelation relation, String role)
	{
		final List<Member> members = relation.members();

		final Set<OSMWay> ways = new HashSet<>();

		for(Member memberTMP : members)
		{

			if (memberTMP.type()==OSMRelation.Member.Type.WAY && memberTMP.role().equals(role))
			{
				ways.add((OSMWay)memberTMP.member());
			}
		}

		return ways;
	}

	private boolean nodesOfGraphHaveTwoNeighbors(Graph<OSMNode> graph)
	{
		Set<OSMNode> nodes = graph.nodes();

		for(OSMNode nodeTMP : nodes)
		{
			if(graph.neighborsOf(nodeTMP).size()!=2)
			{
				return false;
			}
		}

		return true;

	}


	private Graph<OSMNode> graphOfWays(Set<OSMWay> ways)
	{
		Graph.Builder<OSMNode> graphToReturn = new Graph.Builder<OSMNode>();
		List <OSMNode> nodes;

		for(OSMWay way: ways)
		{
			//final List <OSMNode> nodes = way.nodes();

			nodes = way.nodes();


			Iterator<OSMNode> i = nodes.iterator();
			OSMNode nodeTMP1 = i.next();
			OSMNode nodeTMP2;
			graphToReturn.addNode(nodeTMP1);
			while(i.hasNext())
			{
				nodeTMP2=i.next();
				graphToReturn.addNode(nodeTMP2);
				graphToReturn.addEdge(nodeTMP1, nodeTMP2);
				nodeTMP1 = nodeTMP2;
			}

		}


		return graphToReturn.build();





	}
/**
 * It creates a List of attributed polygons and returns it (uses our multiple private methods)
 * @param relation The relation for each cycle in the method Transform
 * @param attributes the attributes of the Relation already filtered to assign to every polygon
 * @return List of attributed polygons
 */
	private List<Attributed<Polygon>> assemblePolygon(OSMRelation relation, Attributes attributes){
		return PolygonToAttributed(
				createPolygons(
						PolyLineSort(
								ringsForRole(relation, "outer")),
								PolyLineSort(
										ringsForRole(relation,"inner"))
						),
						Attributesfilter
						(attributes, Type.Polygon)
				);
	}
/**
 * It creates a List of Polygons from two sorted Lists: outers and inners ClosedpolyLine
 * @param outers the closed polylines recognized with role outer and given as a list
 * @param inners the closed polylines recognized with role inner and given as List
 * @return a List of Polygons
 */
	private List<Polygon> createPolygons(List<ClosedPolyLine> outers,List<ClosedPolyLine> inners ){
		final List<Polygon> polygons= new ArrayList<Polygon>();
		for (int i=0, n=outers.size(); i<n; i++)
		{   final ClosedPolyLine e= outers.get(i);
		final List<ClosedPolyLine> innersOfe= new ArrayList<ClosedPolyLine>();
		innersOfe.addAll(innersOf(e,inners));
		inners.removeAll(innersOfe);
		polygons.add(new Polygon(e, innersOfe));
		}
		return polygons;
	}
/**
 * A method to detect the inners of a ClosedPolyLine
 * @param outer the outer of which we want to find the inners
 * @param inners the inners (as list of ClosedPolyLine)
 * @return a Set of ClosedPolyLine, the inners
 */
	private Set<ClosedPolyLine> innersOf(ClosedPolyLine outer, List<ClosedPolyLine> inners ){
		final Set<ClosedPolyLine> innersOf= new HashSet<ClosedPolyLine>();
		inners.forEach(e->{
			if(contains(outer,e))
			{innersOf.add(e);
			}
		}

				);
		return innersOf;
	}
/**
 * Checks if an outer closedPolyLine contains an inner
 * @param outer
 * @param inner
 * @return true if it is the case, false in any other case 
 */
	private boolean contains(ClosedPolyLine outer, ClosedPolyLine inner){
		for(Point p: inner.points()){
			if(!outer.containsPoint(p)){
				return false;
			}
		}
		return true;
	}

/**
 * 
 * @param polygons the polygons as list
 * @param attributes the attributes 
 * @return an attributed list of polygons
 */
	private List<Attributed<Polygon>> PolygonToAttributed(List<Polygon> polygons, Attributes attributes) {

		final List<Attributed<Polygon>> attributedPolygons = new ArrayList<Attributed<Polygon>>();

		if(!attributes.isEmpty()){
			polygons.forEach(e->
			attributedPolygons.add
			(new Attributed<Polygon>(e,
					attributes)));
		}
		return attributedPolygons;


	}
/**
 * Enumeration with the types for filtering: Polygon or PolyLine
 *
 */
	private static enum Type{
		PolyLine("polyline"), Polygon("polygon");
		private Type(String type){}
	}

	/**
	 * 
	 * @param attributes the the attributes
	 * @param type the Type either PolyLine or Polygon
	 * @return the attributes filtered
	 */
	private Attributes Attributesfilter(Attributes attributes, Type type){

		switch(type) {
		case PolyLine :
			return (attributes.keepOnlyKeys(PolyLineKeys));
		case Polygon :
			return attributes.keepOnlyKeys(PolygonKeys);
		default: return new Attributes(new HashMap<String,String>());
		}

	}
/**
 * 
 * @param polylines the list of Closed Polylines to sort out
 * @return  the list of ClosedPolylines sorted by area
 */
	private List<ClosedPolyLine> PolyLineSort(List<ClosedPolyLine> polylines){
		polylines.sort(new Comparator<ClosedPolyLine>(){
			public int compare(ClosedPolyLine p1, ClosedPolyLine p2){
				if(p1.area()<p2.area()){return -1;}
				else if(p1.area()>p2.area()){return 1;}
				return 0;
			};
		}
				);
		return polylines;
	}



	private boolean isAeraWay(OSMWay way)
	{
		
			if(way.isClosed()) {
				try{
				if(way.attributes().get("area").equals("yes")
						||way.attributes().get("area").equals("1")
						||way.attributes().get("area").equals("true")){return true;}
				}catch(NullPointerException e){}

				final String[] keys = {"aeroway", "amenity", "building", "harbour", "historic",
						"landuse", "leisure", "man_made", "military", "natural",
						"office", "place", "power", "public_transport", "shop",
						"sport", "tourism", "water", "waterway", "wetland"};
				for(int i=0; i<keys.length; i++)
				{
					if (way.attributes().contains(keys[i]))
					{
						return true;
					}
				}
			}

		return false;

	}



	private void putWaysInMap(OSMMap map, Map.Builder mapToReturn)
	{
		final List<OSMWay> ways = map.ways();


		for(OSMWay wayTMP : ways)
		{

			if(isAeraWay(wayTMP))
			{
				try{
					mapToReturn.addPolygon(wayToAttributedPolygon(wayTMP));
				}catch (Exception e){}
			}
			else
			{
				try{
					mapToReturn.addPolyLine(wayToAttributedPolyLine(wayTMP));
				}catch (Exception e){}

			}
		}

	}




	private Attributed<Polygon> wayToAttributedPolygon(OSMWay way) throws Exception
	{
		final Polygon polygon = new Polygon((ClosedPolyLine)wayToPolyline(way));
		final Attributes attributes = Attributesfilter(way.attributes(), Type.Polygon);
		if(attributes.isEmpty()) throw new Exception();
		return new Attributed<Polygon>(polygon,attributes);
	}


	private Attributed<PolyLine> wayToAttributedPolyLine(OSMWay way) throws Exception
	{

		final  PolyLine polyline = wayToPolyline(way);
		final Attributes attributes = Attributesfilter(way.attributes(), Type.PolyLine);   

		if(attributes.isEmpty()) throw new Exception();
		return new Attributed<PolyLine>(polyline,attributes);
	}


	private PolyLine wayToPolyline(OSMWay way)
	{
		final List<OSMNode> nodes = way.nodes();
		final PolyLine.Builder polyLine = new PolyLine.Builder();

		if (way.isClosed())
		{
			for (int i=0, n=nodes.size(); i < n; i++){
				if(i!=n-1){
					polyLine.addPoint(projection.project(nodes.get(i).position()));
				}
			}
			return polyLine.buildClosed();
		}
		else
		{
			for(OSMNode nodeTMP : nodes)
			{
				polyLine.addPoint(projection.project(nodeTMP.position()));
			}
			return polyLine.buildOpen();
		}

	}




}

