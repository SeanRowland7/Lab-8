package geometry_objects.angle;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import exceptions.FactException;
import geometry_objects.Segment;
import geometry_objects.points.Point;

class AngleEquivalenceClassesTest {

	@Test
	void addTest() throws FactException 
	{
		AngleEquivalenceClasses eqclasses = new AngleEquivalenceClasses();
		
		Segment seg1 = new Segment(new Point(0,0), new Point(0, 1));
		Segment seg2 = new Segment(new Point(0,0), new Point(1, 0));
		Angle a1 = new Angle(seg1, seg2);
		
		assertTrue(eqclasses.add(a1));
		assertTrue(eqclasses.contains(a1));
		assertFalse(eqclasses.add(a1));
		assertEquals(1, eqclasses.numClasses());
		
		Segment seg3 = new Segment(new Point(0,0), new Point(2, 0));
		Angle a2 = new Angle(seg1, seg3);
		
		assertTrue(eqclasses.add(a2));
		assertTrue(eqclasses.contains(a2));
		assertEquals(1, eqclasses.numClasses());
		
		Segment seg4 = new Segment(new Point(0,0), new Point(1, 1));
		Angle a3 = new Angle(seg1, seg4);
		
		assertTrue(eqclasses.add(a3));
		assertTrue(eqclasses.contains(a3));
		assertEquals(2, eqclasses.numClasses());
	}
}
