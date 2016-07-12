package ch.epfl.imhof.painting;

import static org.junit.Assert.*;
import java.util.function.Predicate;

import org.junit.Test;
import ch.epfl.imhof.Attributes;
import ch.epfl.imhof.Attributed;
import java.util.HashMap;


public class FiltersTest {

	@Test
	public void tagged1Test() {
		
		HashMap<String,String> map = new HashMap<>();
		map.put("building","yes");
		map.put("tower","no");
		map.put("lake","no");
		map.put("Zeus","no");
		
		Attributes attributes = new Attributes(map);
		
		Attributed<String> attributed = new Attributed<>("attributed 1",attributes);
		
		Predicate<Attributed<?>> predicate1 = Filters.tagged("tower");
		Predicate<Attributed<?>> predicate2 = Filters.tagged("train station");
		
		assertTrue(predicate1.test(attributed));
		assertTrue(!predicate2.test(attributed));
		
	}
	
	@Test
	public void tagged2Test() {
		
		HashMap<String,String> map = new HashMap<>();
		map.put("building","yes");
		map.put("tower","no");
		map.put("lake","no");
		map.put("Zeus","no");
		
		Attributes attributes = new Attributes(map);
		
		Attributed<String> attributed = new Attributed<>("attributed 1",attributes);
		
		Predicate<Attributed<?>> predicate1 = Filters.tagged("building","yes","no","vfre");
		Predicate<Attributed<?>> predicate2 = Filters.tagged("lake","yes","lalala");
		Predicate<Attributed<?>> predicate3 = Filters.tagged("train station","yes");
		
		assertTrue(predicate1.test(attributed));
		assertTrue(!predicate2.test(attributed));
		assertTrue(!predicate3.test(attributed));
		
	}
	
	@Test
	public void onLayerTest() {
		
		HashMap<String,String> map1 = new HashMap<>();
		map1.put("building","yes");
		map1.put("tower","no");
		map1.put("lake","no");
		map1.put("Zeus","no");
		
		Attributes attributes1 = new Attributes(map1);
		
		Attributed<String> attributed1 = new Attributed<>("attributed 1",attributes1);
		
		
		HashMap<String,String> map2 = new HashMap<>();
		map2.put("building","yes");
		map2.put("tower","no");
		map2.put("lake","no");
		map2.put("Zeus","no");
		map2.put("layer","3");
		
		Attributes attributes2 = new Attributes(map2);
		
		Attributed<?> attributed2 = new Attributed<>("attributed 2",attributes2);
		
		
		Predicate<Attributed<?>> predicate1 = Filters.onLayer(3);
		Predicate<Attributed<?>> predicate2 = Filters.onLayer(2);
		Predicate<Attributed<?>> predicate3 = Filters.onLayer(0);
		
		assertTrue(!predicate1.test(attributed1));
		assertTrue(predicate3.test(attributed1));
		
		
		assertTrue(predicate1.test(attributed2));
		assertTrue(!predicate2.test(attributed2));
		
		
	}

}
