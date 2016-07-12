package ch.epfl.imhof.geometry;

import static org.junit.Assert.*;

import java.util.function.Function;

import org.junit.Test;

public class RepereTest {

	@Test
	public void test() {
		Function<Point, Point> blueToRed =
			    Point.alignedCoordinateChange(new Point(1, -1),
			                                  new Point(5, 4),
			                                  new Point(-1.5, 1),
			                                  new Point(0, 0));
		assertEquals(2.0 ,blueToRed.apply(new Point(0, 0)).y(), 0.00001);
	}

}
