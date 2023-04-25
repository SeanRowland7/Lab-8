package geometry_objects.angle;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import exceptions.FactException;
import geometry_objects.Segment;
import geometry_objects.points.Point;

class AngleTest {

	@Test
	void equalsTest() throws FactException 
	{
		Segment seg1 = new Segment(new Point(0,0), new Point(0, 1));
		Segment seg2 = new Segment(new Point(0,0), new Point(1, 0));
		
		assertEquals(new Angle(seg1, seg2), new Angle(seg2, seg1)); 
		
		
	}

}
