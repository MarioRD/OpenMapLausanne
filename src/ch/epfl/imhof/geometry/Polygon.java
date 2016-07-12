package ch.epfl.imhof.geometry;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * 
 * --Class that defines the construction of a polygon with holes--
 * 
 * @author Mario Robert D'Ambrosio (249757)
 * @author Nicodème Stalder (234584)
 * 
 */

/**
 * 
 * --Class that defines the construction of a polygon with holes--
 * 
 * @var holes: the list of holes
 * @var shell: the closed PolyLine
 * 
 * @constructor Polygon(param a, param b): builds up a PolyLine storing the holes positioning
 * 
 * @param shell: the Closed Polyline without holes 
 * @param holes: the holes list
 * 
 */
public final class Polygon
{
	private final ClosedPolyLine shell;
    private final List<ClosedPolyLine> holes; 

    public Polygon(ClosedPolyLine shell, List<ClosedPolyLine> holes)
    {
        final List<ClosedPolyLine> holesave= new ArrayList<ClosedPolyLine>(holes);
        this.holes =Collections.unmodifiableList(holesave);
        this.shell=shell;
    }

    /**
     * @var shell: the closed PolyLine
     * 
     * @constructor initialates a closed Polyline without holes
     * 
     * @return the closed Polyline without holes
     * 
     * @param shell: the Closed Polyline without holes 
     */
    public Polygon(ClosedPolyLine shell){
        holes =Collections.emptyList();
        this.shell=shell;
    }


    /**
     * @return the List of ClosedPolyLine called holes
     */
    public List<ClosedPolyLine> holes() {
        final List<ClosedPolyLine> holescopy=Collections.unmodifiableList(holes);
        return holescopy;
    }


    /**
     * @return the shell, that is a closed Polyline.
     */
    public ClosedPolyLine shell() {

        return shell;
    }

}
