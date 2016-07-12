package ch.epfl.imhof.painting;

import static org.junit.Assert.*;

import org.junit.Test;

public class LineStyleTest {

	@Test
	public void test1() {
		float[] dashpattern = new float[3];
		dashpattern[0]=0.3f;
		dashpattern[1]=1.6f;
		dashpattern[2]=0.9f;
		LineStyle lineStyle1 = new LineStyle(12.5f, Color.rgb(0x00_03_04_05), LineStyle.LineCap.Round, LineStyle.LineJoin.Bevel, dashpattern);
		assertTrue(lineStyle1.getWidth()==12.5f);
		assertTrue(lineStyle1.getColor().red()==3);
		assertTrue(lineStyle1.getColor().green()==4);
		assertTrue(lineStyle1.getColor().blue()==5);
		assertEquals(lineStyle1.getLinecap(),LineStyle.LineCap.Round);
		assertEquals(lineStyle1.getLinejoin(),LineStyle.LineJoin.Bevel);
		assertTrue(lineStyle1.getDashpattern()[0]==dashpattern[0]);
		assertTrue(lineStyle1.getDashpattern()[1]==dashpattern[1]);
		assertTrue(lineStyle1.getDashpattern()[2]==dashpattern[2]);
		assertTrue(lineStyle1.getDashpattern().length==3);
	}
	
	@Test
	public void test2() {
		LineStyle lineStyle1 = new LineStyle(12.5f, Color.rgb(0x00_03_04_05));
		assertTrue(lineStyle1.getWidth()==12.5f);
		assertTrue(lineStyle1.getColor().red()==3);
		assertTrue(lineStyle1.getColor().green()==4);
		assertTrue(lineStyle1.getColor().blue()==5);
		assertEquals(lineStyle1.getLinecap(),LineStyle.LineCap.Butt);
		assertEquals(lineStyle1.getLinejoin(),LineStyle.LineJoin.Miter);
		assertTrue(lineStyle1.getDashpattern().length==0);
	}
	
	@Test
	public void test3() {
		float[] dashpattern = new float[3];
		dashpattern[0]=0.3f;
		dashpattern[1]=1.6f;
		dashpattern[2]=0.9f;
		float[] newDashpattern = new float[3];
		newDashpattern[0]=0.3f;
		newDashpattern[1]=1.6f;
		newDashpattern[2]=0.9f;
		LineStyle lineStyle1 = new LineStyle(12.5f, Color.rgb(0x00_03_04_05), LineStyle.LineCap.Round, LineStyle.LineJoin.Bevel, dashpattern);
		lineStyle1=lineStyle1.withColor(Color.rgb(0x00_06_07_08));
		lineStyle1=lineStyle1.withDashPattern(newDashpattern);
		lineStyle1=lineStyle1.withLineCap(LineStyle.LineCap.Square);
		lineStyle1=lineStyle1.withLineJoin(LineStyle.LineJoin.Miter);
		lineStyle1=lineStyle1.withWidth(2.5f);
		assertTrue(lineStyle1.getWidth()==2.5f);
		assertTrue(lineStyle1.getColor().red()==6);
		assertTrue(lineStyle1.getColor().green()==7);
		assertTrue(lineStyle1.getColor().blue()==8);
		assertEquals(lineStyle1.getLinecap(),LineStyle.LineCap.Square);
		assertEquals(lineStyle1.getLinejoin(),LineStyle.LineJoin.Miter);
		assertTrue(lineStyle1.getDashpattern()[0]==newDashpattern[0]);
		assertTrue(lineStyle1.getDashpattern()[1]==newDashpattern[1]);
		assertTrue(lineStyle1.getDashpattern()[2]==newDashpattern[2]);
	}

}
