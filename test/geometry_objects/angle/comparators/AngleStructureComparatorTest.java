package geometry_objects.angle.comparators;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import exceptions.FactException;
import geometry_objects.Segment;
import geometry_objects.angle.Angle;
import geometry_objects.points.Point;

class AngleStructureComparatorTest {

	@Test
	void structurallyIncomparableTest() throws FactException 
	{
		Segment seg1 = new Segment(new Point(0,0), new Point(0, 1));
		Segment seg2 = new Segment(new Point(0,0), new Point(1, 0));
		Angle a1 = new Angle(seg1, seg2);
		
		Segment seg3 = new Segment(new Point(0,0), new Point(1, 1));
		Angle a2 = new Angle(seg1, seg3);
		
		assertEquals(AngleStructureComparator.STRUCTURALLY_INCOMPARABLE, 
					 new AngleStructureComparator().compare(a1, a2));
	}

	@Test
	void largerSmallerTest() throws FactException 
	{
		Segment seg1 = new Segment(new Point(0,0), new Point(0, 1));
		Segment seg2 = new Segment(new Point(0,0), new Point(1, 0));
		Angle smaller = new Angle(seg1, seg2);
		
		Segment seg3 = new Segment(new Point(0,0), new Point(2, 0));
		Angle larger = new Angle(seg1, seg3);
		
		assertEquals(1, new AngleStructureComparator().compare(larger, smaller));
		assertEquals(-1, new AngleStructureComparator().compare(smaller, larger));
		
	}
	
	@Test
	void inconclusiveTest() throws FactException 
	{
		Segment seg1 = new Segment(new Point(0,0), new Point(0, 1));
		Segment seg2 = new Segment(new Point(0,0), new Point(1, 0));
		Angle a1 = new Angle(seg1, seg2);
		
		Segment seg3 = new Segment(new Point(0,0), new Point(0, 0.5));
		Segment seg4 = new Segment(new Point(0,0), new Point(2, 0));
		Angle a2 = new Angle(seg3, seg4);
		
		assertEquals(0, new AngleStructureComparator().compare(a1, a2));
		assertEquals(0, new AngleStructureComparator().compare(a2, a1));
	}
}
