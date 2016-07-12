package ch.epfl.imhof.painting;

import ch.epfl.imhof.geometry.PolyLine;
import ch.epfl.imhof.geometry.Polygon;

public interface Canvas {
	
	public void drawPolyLine(PolyLine polyLine,LineStyle lineStyle);
	
	public void drawPolygon(Polygon polyGon,Color color);

}
