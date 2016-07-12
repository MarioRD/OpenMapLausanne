package ch.epfl.imhof.osm;

/**
 * @author Mario Robert D'Ambrosio (249757)
 * @author Nicodème Stalder (234584)
 * 
 * -This class stores OSMNodes in order to represent a OSMWay.
 */

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ch.epfl.imhof.Attributes;

/**
 * --This class stores OSMNodes in order to represent a OSMWay.
 * @param nodes, which is a list of OSMNodes representing the OSMWay.
 */
public final class OSMWay extends OSMEntity {

    private final List<OSMNode> nodes;

    public OSMWay(long id, List<OSMNode> nodes, Attributes attributes)
    {
    	
        super(id, attributes);
        this.nodes =Collections.unmodifiableList( new ArrayList<OSMNode>(nodes));
        if(nodes.size()<2)
        {
            throw new IllegalArgumentException();
        }
    }

    /**
     * @return the number of OSMNodes contained in the OSMWay.
     */
    public int nodesCount()
    {
        return nodes.size();
    }

    /**
     * @return the class attribute nodes which is the list of OSMNodes representing the OSMWay.
     */
    public List<OSMNode> nodes()
    {
        return nodes;
    }

    /**
     * @return the nodes of the OSMWay but remove the last node if it is the same node as the first node.
     */
    public List<OSMNode> nonRepeatingNodes()
    {
        if(nodes.get(0).equals(nodes.get(nodes.size()-1)))
        {
            return Collections.unmodifiableList(nodes).subList(0, (nodes.size()-1));
        }
        return this.nodes;
    }

    /**
     * @return the first node of the OSMWay.
     */
    public OSMNode firstNode()
    {
        return nodes.get(0);
    }
    
    /**
     * @return the last node of the OSMWay.
     */
    public OSMNode lastNode()
    {
        return nodes.get(nodes.size()-1);
    }
    
    /**
     * @return true if the OSMWay is the last and the first node are the same (it is a closed way then), and false otherwise.
     */
    public boolean isClosed()
    {
        return((nodes.get(0).equals(nodes.get(nodes.size()-1))));
    }
    /**
     * class that can be used to build iteratively an OSMWay
     * @param nodes, a list of OSMNodes which are the future OSMNodes that will form the OSMWay we are building.
     */
    public final static class Builder extends OSMEntity.Builder
    {

        private List<OSMNode> nodes = new ArrayList<>();
        public Builder(long id)
        {
            super(id);
        }
        /**
         * @param newNode, which is the node we'll add to the nodes list.
         */
        public void  addNode(OSMNode newNode)
        {
            nodes.add(newNode);
        }
        
        /**
         * @return the OSMWay object we are building.
         */
        public OSMWay build(){
            if (super.isIncomplete()||nodes.size()<2)
            {
                throw new IllegalStateException();
            }
            return new OSMWay(super.id,this.nodes,super.attributes.build());

        }
        
        /**
         * @return true if the object was set as incomplete so was set has unable to build a OSMWay object.
         */
        public boolean isIncomplete(){
        	
        	if(nodes.size()<2){
                return true;
            }
            return super.isIncomplete();
        }
    }

}
