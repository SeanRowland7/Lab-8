package geometry_objects.angle;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import exceptions.FactException;
import geometry_objects.Segment;
import geometry_objects.angle.comparators.AngleStructureComparator;
import geometry_objects.points.Point;

class AngleLinkedEquivalenceClassTest {

	@Test
	void belongsTest() throws FactException 
	{
		AngleLinkedEquivalenceClass eqc = new AngleLinkedEquivalenceClass();
		
		Segment seg1 = new Segment(new Point(0,0), new Point(0, 1));
		Segment seg2 = new Segment(new Point(0,0), new Point(1, 0));
		Angle a1 = new Angle(seg1, seg2);
		
		assertTrue(eqc.belongs(a1));
		eqc.add(a1);
		assertFalse(eqc.belongs(a1));
		
		Segment seg3 = new Segment(new Point(0,0), new Point(2, 0));
		Angle a2 = new Angle(seg1, seg3);
		
		assertTrue(eqc.belongs(a2));
		
		Segment seg4 = new Segment(new Point(0,0), new Point(1, 1));
		Angle a3 = new Angle(seg1, seg4);
		
		assertFalse(eqc.belongs(a3));
		
	}

	@Test
	void addTest() throws FactException 
	{
		AngleLinkedEquivalenceClass eqc = new AngleLinkedEquivalenceClass();
		
		Segment seg1 = new Segment(new Point(0,0), new Point(0, 1));
		Segment seg2 = new Segment(new Point(0,0), new Point(1, 0));
		Angle a1 = new Angle(seg1, seg2);
		
		assertTrue(eqc.add(a1));
		assertTrue(eqc.contains(a1));
		assertFalse(eqc.add(a1));
		
		Segment seg3 = new Segment(new Point(0,0), new Point(2, 0));
		Angle a2 = new Angle(seg1, seg3);
		
		assertTrue(eqc.add(a2));
		assertTrue(eqc.contains(a2));
		
		Segment seg4 = new Segment(new Point(0,0), new Point(1, 1));
		Angle a3 = new Angle(seg1, seg4);
		
		assertFalse(eqc.add(a3));
		assertFalse(eqc.contains(a3));
	}

	
}
