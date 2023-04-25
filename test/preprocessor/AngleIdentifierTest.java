package preprocessor;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.jupiter.api.Test;

import exceptions.FactException;
import geometry_objects.Segment;
import geometry_objects.angle.Angle;
import geometry_objects.angle.AngleEquivalenceClasses;
import geometry_objects.points.Point;
import geometry_objects.points.PointDatabase;
import input.components.ComponentNode;
import input.components.FigureNode;
import input.InputFacade;

class AngleIdentifierTest
{
	protected PointDatabase _points;
	protected Preprocessor _pp;
	protected Map<Segment, Segment> _segments;
	
	protected void init(String filename)
	{
		String figureStr = utilities.io.FileUtilities.readFileFilterComments(filename );

		ComponentNode node = InputFacade.extractFigure(figureStr);

		Map.Entry<PointDatabase, Set<Segment>> pair = InputFacade.toGeometryRepresentation((FigureNode) node);

		_points = pair.getKey();

		_pp = new Preprocessor(_points, pair.getValue());

		_pp.analyze();

		_segments = _pp.getAllSegments();
	}
	
	//      A                                 
	//     / \                                
	//    B___C                               
	//   / \ / \                              
	//  /   X   \  X is not a specified point (it is implied) 
	// D_________E
	//
	// This figure contains 44 angles
	//
	@Test
	void test_crossing_symmetric_triangle()
	{
		init("crossing_symmetric_triangle.json");

		AngleIdentifier angleIdentifier = new AngleIdentifier(_segments);

		AngleEquivalenceClasses computedAngles = angleIdentifier.getAngles();

		// The number of classes should equate to the number of 'minimal' angles
		assertEquals("Number of Angle Equivalence classes", 25, computedAngles.numClasses());
		
		//
		// ALL original segments: 8 in this figure.
		//
		
		Segment ab = new Segment(_points.getPoint("A"), _points.getPoint("B"));
		Segment ac = new Segment(_points.getPoint("A"), _points.getPoint("C"));
		Segment bc = new Segment(_points.getPoint("B"), _points.getPoint("C"));

		Segment bd = new Segment(_points.getPoint("B"), _points.getPoint("D"));
		Segment ce = new Segment(_points.getPoint("C"), _points.getPoint("E"));
		Segment de = new Segment(_points.getPoint("D"), _points.getPoint("E"));

		Segment be = new Segment(_points.getPoint("B"), _points.getPoint("E"));
		Segment cd = new Segment(_points.getPoint("C"), _points.getPoint("D"));

		//
		// Implied minimal segments: 4 in this figure.
		//
		Point a_star = _points.getPoint(3,3);

		Segment a_star_b = new Segment(a_star, _points.getPoint("B"));
		Segment a_star_c = new Segment(a_star, _points.getPoint("C"));
		Segment a_star_d = new Segment(a_star, _points.getPoint("D"));
		Segment a_star_e = new Segment(a_star, _points.getPoint("E"));

		//
		// Non-minimal, computed segments: 2 in this figure.
		//
		Segment ad = new Segment(_points.getPoint("A"), _points.getPoint("D"));
		Segment ae = new Segment(_points.getPoint("A"), _points.getPoint("E"));

		//
		// Angles we expect to find
		//
		List<Angle> expectedAngles = new ArrayList<Angle>();
		try {
			//
			//
			// Angles broken down by equivalence class
			//
			//

			// Straight angles
			//
			expectedAngles.add(new Angle(a_star_b, a_star_e));
			
			expectedAngles.add(new Angle(ac, ce));
			
			expectedAngles.add(new Angle(a_star_d, a_star_c));
			
			expectedAngles.add(new Angle(ab, bd));
			
			// right angles
			//
			expectedAngles.add(new Angle(a_star_b, a_star_d));
			
			expectedAngles.add(new Angle(a_star_b, a_star_c));
			
			expectedAngles.add(new Angle(a_star_d, a_star_e));

			expectedAngles.add(new Angle(a_star_c, a_star_e));
			
			// 
			//
			expectedAngles.add(new Angle(a_star_b, ab));
			expectedAngles.add(new Angle(be, ab));

			expectedAngles.add(new Angle(a_star_b, bc));
			expectedAngles.add(new Angle(be, bc));

			expectedAngles.add(new Angle(a_star_b, bd));
			expectedAngles.add(new Angle(be, bd));
			
			expectedAngles.add(new Angle(ac, a_star_c));
			expectedAngles.add(new Angle(ac, cd));

			expectedAngles.add(new Angle(bc, a_star_c));
			expectedAngles.add(new Angle(cd, bd));
			
			expectedAngles.add(new Angle(a_star_c, ce));
			expectedAngles.add(new Angle(cd, ce));
			
			expectedAngles.add(new Angle(a_star_d, de));
			expectedAngles.add(new Angle(cd, de));
			
			expectedAngles.add(new Angle(bd, de));
			expectedAngles.add(new Angle(ad, de));
			
			expectedAngles.add(new Angle(a_star_e, de));
			expectedAngles.add(new Angle(be, de));

			expectedAngles.add(new Angle(ce, de));
			expectedAngles.add(new Angle(ae, de));
			
			// Larger equivalence classes
			//
			expectedAngles.add(new Angle(ac, ab));
			expectedAngles.add(new Angle(ab, ae));
			expectedAngles.add(new Angle(ad, ae));
			expectedAngles.add(new Angle(ac, ad));

			expectedAngles.add(new Angle(a_star_d, bd));
			expectedAngles.add(new Angle(cd, bd));
			expectedAngles.add(new Angle(ad, a_star_d));
			expectedAngles.add(new Angle(cd, ad));

			expectedAngles.add(new Angle(a_star_e, ce));
			expectedAngles.add(new Angle(be, ae));
			expectedAngles.add(new Angle(be, ce));
			expectedAngles.add(new Angle(a_star_e, ce));

			
			// More singletons
			//
			expectedAngles.add(new Angle(ac, bc));

			expectedAngles.add(new Angle(ab, bc));
			
			expectedAngles.add(new Angle(bc, bd));

			expectedAngles.add(new Angle(bc, ce));			
		}
		catch (FactException te) { System.err.println("Invalid Angles in Angle test."); }

		assertEquals(expectedAngles.size(), computedAngles.size());
		
		//
		// Equality
		//
		for (Angle expected : expectedAngles)
		{
			assertTrue(computedAngles.contains(expected));
		}
	}
	
	@Test
	void test_fully_connected_square()
	{
		//       (0, 1)   (1, 1)
		//			A-------B
		//			| \   / |
		//			|   X   |
		//			| /   \ |
		//			C-------D
		//		 (0, 0)   (1, 0)



		init("fully_connected_square.json");

		AngleIdentifier angleIdentifier = new AngleIdentifier(_segments);

		AngleEquivalenceClasses computedAngles = angleIdentifier.getAngles();

		// The number of classes should equate to the number of 'minimal' angles
		assertEquals("Number of Angle Equivalence classes", 18, computedAngles.numClasses());
		
		//
		// ALL original segments: 6 in this figure.
		//
		
		Segment ab = new Segment(_points.getPoint("A"), _points.getPoint("B"));
		Segment ac = new Segment(_points.getPoint("A"), _points.getPoint("C"));
		Segment ad = new Segment(_points.getPoint("A"), _points.getPoint("D"));

		Segment bc = new Segment(_points.getPoint("B"), _points.getPoint("C"));
		Segment bd = new Segment(_points.getPoint("B"), _points.getPoint("D"));

		Segment cd = new Segment(_points.getPoint("C"), _points.getPoint("D"));

		//
		// Implied minimal segments: 4 in this figure.
		//
		Point a_star = _points.getPoint(0.5, 0.5);

		Segment a_star_a = new Segment(a_star, _points.getPoint("A"));
		Segment a_star_b = new Segment(a_star, _points.getPoint("B"));
		Segment a_star_c = new Segment(a_star, _points.getPoint("C"));
		Segment a_star_d = new Segment(a_star, _points.getPoint("D"));

		//
		// Non-minimal, computed segments: 0 in this figure.
		//

		//
		// Angles we expect to find
		//
		List<Angle> expectedAngles = new ArrayList<Angle>();
		try {
			//
			//
			// Angles broken down by equivalence class
			//
			//

			// Inner angles: Straight 
			//
			expectedAngles.add(new Angle(a_star_a, a_star_d));
			
			expectedAngles.add(new Angle(a_star_b, a_star_c));
			
			
			// Inner angles: Right
			//
			expectedAngles.add(new Angle(a_star_a, a_star_b));
			
			expectedAngles.add(new Angle(a_star_b, a_star_c));
			
			expectedAngles.add(new Angle(a_star_c, a_star_d));

			expectedAngles.add(new Angle(a_star_d, a_star_a));
			
			// Corner Angles: Right
			//
			expectedAngles.add(new Angle(ac, ab));
			
			expectedAngles.add(new Angle(ab, bd));
			
			expectedAngles.add(new Angle(bd, cd));
			
			expectedAngles.add(new Angle(cd, ac));
			
			// Corner Angles: Top Left 45 
			// 
			expectedAngles.add(new Angle(ab, a_star_a));
			expectedAngles.add(new Angle(ab, ad));
									
			expectedAngles.add(new Angle(ac, a_star_a));
			expectedAngles.add(new Angle(ac, ad));

						
			// Corner Angles: Top Right 45 
			// 
			expectedAngles.add(new Angle(ab, a_star_b));
			expectedAngles.add(new Angle(ab, bc));
						
			expectedAngles.add(new Angle(bd, a_star_b));
			expectedAngles.add(new Angle(bd, bc));


			// Corner Angles: Bottom Left 45 
			// 
			expectedAngles.add(new Angle(ac, a_star_c));
			expectedAngles.add(new Angle(ac, bc));
			
			expectedAngles.add(new Angle(cd, a_star_c));
			expectedAngles.add(new Angle(cd, bc));
						
			// Corner Angles: Bottom Right 45
			//
			expectedAngles.add(new Angle(cd, a_star_d));
			expectedAngles.add(new Angle(cd, ad));
			
			expectedAngles.add(new Angle(bd, a_star_d));
			expectedAngles.add(new Angle(bd, ad));	
			
				
		}
		catch (FactException te) { System.err.println("Invalid Angles in Angle test."); }

		assertEquals(expectedAngles.size(), computedAngles.size());
		
		//
		// Equality
		//
		for (Angle expected : expectedAngles)
		{
			assertTrue(computedAngles.contains(expected));
		}
		
	}
}
