package ch.epfl.imhof.painting;

import static org.junit.Assert.*;

import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import ch.epfl.imhof.painting.Color;

public class ColorTest {
	@Rule
	 public ExpectedException exception = ExpectedException.none();
	
	//@Ignore
	@Test
	public void grayTest() {
		
		assertEquals((Color.rgb(0xFF_FF_FF)),
				(Color.gray(1)));
		
		assertEquals(Color.rgb(0x60_60_60), 
				Color.gray(96/255.0f));
		
		assertEquals(Color.rgb(0xA8A8A8), 
				Color.gray(168/255.0f));
	}
	
	//@Ignore
	@Test
	public void IllegalsTest() {
		int i=0;
		try {	
		System.out.print("Gray Test");
		Color.gray(128);
		Color.gray(-128);
		
		Color.gray(1.0000000001);
		Color.gray(-0.0000000001);
		
		//rgb test
		System.out.print("RGB Test");
		Color.rgb(128,0,0);
		Color.rgb(0,128,0);
		Color.rgb(0,0,128);
		Color.rgb(0,0.1, 0);
		Color.rgb(-128,0,0);
		Color.rgb(0,-128,0);
		Color.rgb(0,0,-128);
		
		Color.rgb(1.0000000001,0,0);
		Color.rgb(0,0,-0.0000000001);
		fail( "Another error was thrown and not IllegalArgumentException" );
		} catch (IllegalArgumentException e) {++i;
		}
	System.out.println(" " + i + " IllegalArgumentException thrown");
	}
	
	//@Ignore
	@Test
	public void rgbTest() {
		assertEquals((Color.rgb(0xFF_FF_FF)),
				(Color.rgb(1,1,1)));
		
		assertEquals((Color.rgb(0xFF_33_99)),
				(Color.rgb(255/255.0f,51/255.0f,153/255.0f)));
		
		assertEquals((Color.rgb(0x00_CC_99)),
				(Color.rgb(0,204/255.0f,153/255.0f)));
		
		assertEquals((Color.rgb(0x00_33_CC)),
				(Color.rgb(0,51/255.0f,204/255.0f)));
		
	}
	
	
	@Test
	public void multiplierTest(){		
		assertEquals((Color.rgb(0xFF_FF_FF)).
				multiplication(
						Color.rgb(1.0,(double)51/255d,(double)153/255d)),
				(Color.rgb(0xFF_33_99)));
	}
	
	@Test
	public void JavaColorTest(){
		assertEquals(Color.rgb(0xFF_B5_30).javaColor(),
				new java.awt.Color(1.0f,0xB5/255.0f,0x30/255.0f));
	}
	
	
}
