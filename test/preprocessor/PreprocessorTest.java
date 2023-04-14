package preprocessor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.jupiter.api.Test;

import geometry_objects.Segment;
import geometry_objects.points.Point;
import geometry_objects.points.PointDatabase;
import input.InputFacade;
import input.components.ComponentNode;
import input.components.FigureNode;
import preprocessor.delegates.ImplicitPointPreprocessor;

class PreprocessorTest
{
	@Test
	void crossing_symmetric_triangle_test()
	{
		//	     A                                 
		//      / \                                
		//     B___C                               
		//    / \ / \                              
		//   /   X*  \        X* is not a specified point (it is implied) 
		//  D_________E                            
		//
		// There are 4 implied segments inside the figure above
		//

		String figureStr = utilities.io.FileUtilities.readFileFilterComments("crossing_symmetric_triangle.json");

		ComponentNode node = InputFacade.extractFigure(figureStr);

		Map.Entry<PointDatabase, Set<Segment>> pair = InputFacade.toGeometryRepresentation((FigureNode) node);

		PointDatabase points = pair.getKey();

		Set<Segment> segments = pair.getValue();

		Preprocessor pp = new Preprocessor(points, segments);

		Set<Point> iPoints = ImplicitPointPreprocessor.compute(points, new ArrayList<Segment>(segments));
		assertEquals(1, iPoints.size());

		Point x_star = new Point(3, 3);


		assertTrue(iPoints.contains(x_star));

		Set<Segment> iSegments = pp.computeImplicitBaseSegments(iPoints);
		assertEquals(4, iSegments.size());

		List<Segment> expectedISegments = new ArrayList<Segment>();

		expectedISegments.add(new Segment(points.getPoint("B"), x_star));
		expectedISegments.add(new Segment(points.getPoint("C"), x_star));
		expectedISegments.add(new Segment(points.getPoint("D"), x_star));
		expectedISegments.add(new Segment(points.getPoint("E"), x_star));

		for (Segment iSegment : iSegments)
		{
			assertTrue(expectedISegments.contains(iSegment));
		}



		Set<Segment> minimalSegments = pp.identifyAllMinimalSegments(iPoints, segments, iSegments);
		assertEquals(10, minimalSegments.size());

		List<Segment> expectedMinimalSegments = new ArrayList<Segment>(iSegments);
		expectedMinimalSegments.add(new Segment(points.getPoint("A"), points.getPoint("B")));
		expectedMinimalSegments.add(new Segment(points.getPoint("A"), points.getPoint("C")));
		expectedMinimalSegments.add(new Segment(points.getPoint("B"), points.getPoint("C")));
		expectedMinimalSegments.add(new Segment(points.getPoint("B"), points.getPoint("D")));
		expectedMinimalSegments.add(new Segment(points.getPoint("C"), points.getPoint("E")));
		expectedMinimalSegments.add(new Segment(points.getPoint("D"), points.getPoint("E")));
		expectedISegments.add(new Segment(points.getPoint("B"), x_star));
		expectedISegments.add(new Segment(points.getPoint("C"), x_star));
		expectedISegments.add(new Segment(points.getPoint("D"), x_star));
		expectedISegments.add(new Segment(points.getPoint("E"), x_star));

		//
		// Construct ALL figure segments from the base segments
		//
		Set<Segment> computedNonMinimalSegments = pp.constructAllNonMinimalSegments(minimalSegments);



		//
		// All Segments will consist of the new 15 non-minimal segments.
		//
		assertEquals(4, computedNonMinimalSegments.size());

		//
		// Ensure we have ALL minimal segments: 20 in this figure.
		//

		//
		// Check size and content equality
		//

	}

	@Test
	void test_tic_tac_toe()
	{
		String figureStr = utilities.io.FileUtilities.readFileFilterComments("tic_tac_toe.json");

		ComponentNode node = InputFacade.extractFigure(figureStr);

		Map.Entry<PointDatabase, Set<Segment>> pair = InputFacade.toGeometryRepresentation((FigureNode) node);

		PointDatabase points = pair.getKey();

		Set<Segment> segments = pair.getValue();

		Preprocessor pp = new Preprocessor(points, segments);

		// 5 new implied points inside the pentagon
		Set<Point> iPoints = ImplicitPointPreprocessor.compute(points, new ArrayList<Segment>(segments));
		assertEquals(4, iPoints.size());


		//    	E    G
		//		|    |
		//   A-a*----b*--B
		//		|	 |
		//   C-c*---d*--D
		//		|    |
		//		F    H

		Point a_star = new Point(2, 1);
		Point b_star = new Point(4, 1);
		Point c_star = new Point(2, -1);
		Point d_star = new Point(4, -1);

		assertTrue(iPoints.contains(a_star));
		assertTrue(iPoints.contains(b_star));
		assertTrue(iPoints.contains(c_star));
		assertTrue(iPoints.contains(d_star));

		//
		// There are 15 implied segments inside the pentagon; see figure above
		//
		Set<Segment> iSegments = pp.computeImplicitBaseSegments(iPoints);
		assertEquals(12, iSegments.size());

		List<Segment> expectedISegments = new ArrayList<Segment>();

		expectedISegments.add(new Segment(points.getPoint("A"), a_star));
		expectedISegments.add(new Segment(points.getPoint("B"), b_star));
		expectedISegments.add(new Segment(points.getPoint("C"), c_star));
		expectedISegments.add(new Segment(points.getPoint("D"), d_star));
		expectedISegments.add(new Segment(points.getPoint("E"), a_star));
		expectedISegments.add(new Segment(points.getPoint("F"), c_star));
		expectedISegments.add(new Segment(points.getPoint("G"), b_star));
		expectedISegments.add(new Segment(points.getPoint("H"), d_star));
		
		expectedISegments.add(new Segment(a_star, b_star));
		expectedISegments.add(new Segment(a_star, c_star));
		
		expectedISegments.add(new Segment(b_star, d_star));
		
		expectedISegments.add(new Segment(d_star, c_star));


		for (Segment iSegment : iSegments)
		{
			assertTrue(expectedISegments.contains(iSegment));
		}

		//
		// Ensure we have ALL minimal segments: 20 in this figure.
		//
		List<Segment> expectedMinimalSegments = new ArrayList<Segment>(iSegments);
		

		Set<Segment> minimalSegments = pp.identifyAllMinimalSegments(iPoints, segments, iSegments);
		assertEquals(expectedMinimalSegments.size(), minimalSegments.size());

		for (Segment minimalSeg : minimalSegments)
		{
			assertTrue(expectedMinimalSegments.contains(minimalSeg));
		}

		//
		// Construct ALL figure segments from the base segments
		//
		Set<Segment> computedNonMinimalSegments = pp.constructAllNonMinimalSegments(minimalSegments);
		
		

		//
		// All Segments will consist of the new 15 non-minimal segments.
		//
		
		assertEquals(12, computedNonMinimalSegments.size());

		//
		// Ensure we have ALL minimal segments: 20 in this figure.
		//
		List<Segment> expectedNonMinimalSegments = new ArrayList<Segment>();
		
		expectedNonMinimalSegments.add(new Segment(points.getPoint("A"), b_star));
		expectedNonMinimalSegments.add(new Segment(points.getPoint("A"), points.getPoint("B")));
		expectedNonMinimalSegments.add(new Segment(a_star, points.getPoint("B")));
		
		expectedNonMinimalSegments.add(new Segment(points.getPoint("C"), d_star));
		expectedNonMinimalSegments.add(new Segment(points.getPoint("C"), points.getPoint("D")));
		expectedNonMinimalSegments.add(new Segment(c_star, points.getPoint("D")));
		
		expectedNonMinimalSegments.add(new Segment(points.getPoint("E"), c_star));
		expectedNonMinimalSegments.add(new Segment(points.getPoint("E"), points.getPoint("F")));
		expectedNonMinimalSegments.add(new Segment(a_star, points.getPoint("F")));
		
		expectedNonMinimalSegments.add(new Segment(points.getPoint("G"), d_star));
		expectedNonMinimalSegments.add(new Segment(points.getPoint("G"), points.getPoint("H")));
		expectedNonMinimalSegments.add(new Segment(b_star, points.getPoint("H")));


		//
		// Check size and content equality
		//
		assertEquals(expectedNonMinimalSegments.size(), computedNonMinimalSegments.size());

		for (Segment computedNonMinimalSegment : computedNonMinimalSegments)
		{
			assertTrue(expectedNonMinimalSegments.contains(computedNonMinimalSegment));
		}
	}

	@Test
	void collinear_line_segments_test()
	{
		String figureStr = utilities.io.FileUtilities.readFileFilterComments("collinear_line_segments.json");

		ComponentNode node = InputFacade.extractFigure(figureStr);

		Map.Entry<PointDatabase, Set<Segment>> pair = InputFacade.toGeometryRepresentation((FigureNode) node);

		PointDatabase points = pair.getKey();

		Set<Segment> segments = pair.getValue();

		Preprocessor pp = new Preprocessor(points, segments);

		Set<Point> iPoints = ImplicitPointPreprocessor.compute(points, new ArrayList<Segment>(segments));
		assertEquals(0, iPoints.size());


		//	    A----B-----C--D-----E----------F


		// There are 0 implied segments 

		Set<Segment> iSegments = pp.computeImplicitBaseSegments(iPoints);
		assertEquals(0, iSegments.size());

		// there are five 5 minimal segments 

		Set<Segment> minimalSegments = pp.identifyAllMinimalSegments(iPoints, segments, iSegments);
		assertEquals(5, minimalSegments.size());

		List<Segment> expectedMinimalSegments = new ArrayList<Segment>(iSegments);
		expectedMinimalSegments.add(new Segment(points.getPoint("A"), points.getPoint("B")));
		expectedMinimalSegments.add(new Segment(points.getPoint("B"), points.getPoint("C")));
		expectedMinimalSegments.add(new Segment(points.getPoint("C"), points.getPoint("D")));
		expectedMinimalSegments.add(new Segment(points.getPoint("D"), points.getPoint("E")));
		expectedMinimalSegments.add(new Segment(points.getPoint("E"), points.getPoint("F")));
		assertEquals(expectedMinimalSegments.size(), minimalSegments.size());

		//
		// Ensure we have ALL minimal segments: 5 in this figure.
		//

		for (Segment minimalSeg : minimalSegments)
		{
			assertTrue(expectedMinimalSegments.contains(minimalSeg));
		}

		//
		// Construct ALL figure segments from the base segments
		//
		Set<Segment> computedNonMinimalSegments = pp.constructAllNonMinimalSegments(minimalSegments);

		//
		// All Segments will consist of the new 15 non-minimal segments.
		//
		assertEquals(10, computedNonMinimalSegments.size());

		//
		// Ensure we have ALL minimal segments: 20 in this figure.
		//
		List<Segment> expectedNonMinimalSegments = new ArrayList<Segment>();
		expectedNonMinimalSegments.add(new Segment(points.getPoint("A"), points.getPoint("C")));
		expectedNonMinimalSegments.add(new Segment(points.getPoint("A"), points.getPoint("D")));
		expectedNonMinimalSegments.add(new Segment(points.getPoint("A"), points.getPoint("E")));
		expectedNonMinimalSegments.add(new Segment(points.getPoint("A"), points.getPoint("F")));

		expectedNonMinimalSegments.add(new Segment(points.getPoint("B"), points.getPoint("D")));
		expectedNonMinimalSegments.add(new Segment(points.getPoint("B"), points.getPoint("E")));
		expectedNonMinimalSegments.add(new Segment(points.getPoint("B"), points.getPoint("F")));

		expectedNonMinimalSegments.add(new Segment(points.getPoint("C"), points.getPoint("E")));
		expectedNonMinimalSegments.add(new Segment(points.getPoint("C"), points.getPoint("F")));

		expectedNonMinimalSegments.add(new Segment(points.getPoint("D"), points.getPoint("F")));


		//
		// Check size and content equality
		//
		assertEquals(expectedNonMinimalSegments.size(), computedNonMinimalSegments.size());

		for (Segment computedNonMinimalSegment : computedNonMinimalSegments)
		{
			assertTrue(expectedNonMinimalSegments.contains(computedNonMinimalSegment));
		}
	}

	@Test
	void test_fully_connected_irregular_polygon()
	{
		String figureStr = utilities.io.FileUtilities.readFileFilterComments("fully_connected_irregular_polygon.json");

		ComponentNode node = InputFacade.extractFigure(figureStr);

		Map.Entry<PointDatabase, Set<Segment>> pair = InputFacade.toGeometryRepresentation((FigureNode) node);

		PointDatabase points = pair.getKey();

		Set<Segment> segments = pair.getValue();

		Preprocessor pp = new Preprocessor(points, segments);

		// 5 new implied points inside the pentagon
		Set<Point> iPoints = ImplicitPointPreprocessor.compute(points, new ArrayList<Segment>(segments));
		assertEquals(5, iPoints.size());


		//
		//
		//		               D(3, 7)
		//
		//
		//   E(-2,4)       D*      E*
		//		         C*          A*       C(6, 3)
		//                      B*
		//		       A(2,0)        B(4, 0)
		//
		//		    An irregular pentagon with 5 C 2 = 10 segments

		Point a_star = new Point(56.0 / 15, 28.0 / 15);
		Point b_star = new Point(16.0 / 7, 8.0 / 7);
		Point c_star = new Point(8.0 / 9, 56.0 / 27);
		Point d_star = new Point(90.0 / 59, 210.0 / 59);
		Point e_star = new Point(194.0 / 55, 182.0 / 55);

		assertTrue(iPoints.contains(a_star));
		assertTrue(iPoints.contains(b_star));
		assertTrue(iPoints.contains(c_star));
		assertTrue(iPoints.contains(d_star));
		assertTrue(iPoints.contains(e_star));

		//
		// There are 15 implied segments inside the pentagon; see figure above
		//
		Set<Segment> iSegments = pp.computeImplicitBaseSegments(iPoints);
		assertEquals(15, iSegments.size());

		List<Segment> expectedISegments = new ArrayList<Segment>();

		expectedISegments.add(new Segment(points.getPoint("A"), c_star));
		expectedISegments.add(new Segment(points.getPoint("A"), b_star));

		expectedISegments.add(new Segment(points.getPoint("B"), b_star));
		expectedISegments.add(new Segment(points.getPoint("B"), a_star));

		expectedISegments.add(new Segment(points.getPoint("C"), a_star));
		expectedISegments.add(new Segment(points.getPoint("C"), e_star));

		expectedISegments.add(new Segment(points.getPoint("D"), d_star));
		expectedISegments.add(new Segment(points.getPoint("D"), e_star));

		expectedISegments.add(new Segment(points.getPoint("E"), c_star));
		expectedISegments.add(new Segment(points.getPoint("E"), d_star));

		expectedISegments.add(new Segment(c_star, b_star));
		expectedISegments.add(new Segment(b_star, a_star));
		expectedISegments.add(new Segment(a_star, e_star));
		expectedISegments.add(new Segment(e_star, d_star));
		expectedISegments.add(new Segment(d_star, c_star));


		for (Segment iSegment : iSegments)
		{
			assertTrue(expectedISegments.contains(iSegment));
		}

		//
		// Ensure we have ALL minimal segments: 20 in this figure.
		//
		List<Segment> expectedMinimalSegments = new ArrayList<Segment>(iSegments);
		expectedMinimalSegments.add(new Segment(points.getPoint("A"), points.getPoint("B")));
		expectedMinimalSegments.add(new Segment(points.getPoint("B"), points.getPoint("C")));
		expectedMinimalSegments.add(new Segment(points.getPoint("C"), points.getPoint("D")));
		expectedMinimalSegments.add(new Segment(points.getPoint("D"), points.getPoint("E")));
		expectedMinimalSegments.add(new Segment(points.getPoint("E"), points.getPoint("A")));

		Set<Segment> minimalSegments = pp.identifyAllMinimalSegments(iPoints, segments, iSegments);
		assertEquals(expectedMinimalSegments.size(), minimalSegments.size());

		for (Segment minimalSeg : minimalSegments)
		{
			assertTrue(expectedMinimalSegments.contains(minimalSeg));
		}

		//
		// Construct ALL figure segments from the base segments
		//
		Set<Segment> computedNonMinimalSegments = pp.constructAllNonMinimalSegments(minimalSegments);

		//
		// All Segments will consist of the new 15 non-minimal segments.
		//
		assertEquals(15, computedNonMinimalSegments.size());

		//
		// Ensure we have ALL minimal segments: 20 in this figure.
		//
		List<Segment> expectedNonMinimalSegments = new ArrayList<Segment>();
		expectedNonMinimalSegments.add(new Segment(points.getPoint("A"), d_star));
		expectedNonMinimalSegments.add(new Segment(points.getPoint("D"), c_star));
		expectedNonMinimalSegments.add(new Segment(points.getPoint("A"), points.getPoint("D")));

		expectedNonMinimalSegments.add(new Segment(points.getPoint("B"), c_star));
		expectedNonMinimalSegments.add(new Segment(points.getPoint("E"), b_star));
		expectedNonMinimalSegments.add(new Segment(points.getPoint("B"), points.getPoint("E")));

		expectedNonMinimalSegments.add(new Segment(points.getPoint("C"), d_star));
		expectedNonMinimalSegments.add(new Segment(points.getPoint("E"), e_star));
		expectedNonMinimalSegments.add(new Segment(points.getPoint("C"), points.getPoint("E")));		

		expectedNonMinimalSegments.add(new Segment(points.getPoint("A"), a_star));
		expectedNonMinimalSegments.add(new Segment(points.getPoint("C"), b_star));
		expectedNonMinimalSegments.add(new Segment(points.getPoint("A"), points.getPoint("C")));

		expectedNonMinimalSegments.add(new Segment(points.getPoint("B"), e_star));
		expectedNonMinimalSegments.add(new Segment(points.getPoint("D"), a_star));
		expectedNonMinimalSegments.add(new Segment(points.getPoint("B"), points.getPoint("D")));

		//
		// Check size and content equality
		//
		assertEquals(expectedNonMinimalSegments.size(), computedNonMinimalSegments.size());

		for (Segment computedNonMinimalSegment : computedNonMinimalSegments)
		{
			assertTrue(expectedNonMinimalSegments.contains(computedNonMinimalSegment));
		}
	}
}