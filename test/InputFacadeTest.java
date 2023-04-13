/**
* Tests for the InputFacade class
*
* @author Michael Leiby, Sean Rowland
* @date date
*/


import static org.junit.jupiter.api.Assertions.*;

import java.util.AbstractMap;
import java.util.LinkedHashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

import geometry_objects.points.*;
import geometry_objects.Segment;
import input.InputFacade;
import input.components.ComponentNode;
import input.components.FigureNode;
import input.visitor.UnparseVisitor;

class InputFacadeTest {

	@Test
	void testExtractFigure() 
	{
		//A----B-----C--D-----E----------F

		String figureStr = utilities.io.FileUtilities.readFileFilterComments("collinear_line_segments.json");
			
		ComponentNode node = InputFacade.extractFigure(figureStr);

		assertTrue(node instanceof FigureNode);
		
		//	     A                                 
		//      / \                                
		//     B___C                               
		//    / \ / \                              
		//   /   X   \ 
		//  D_________E 
		
		String figureStr2 = utilities.io.FileUtilities.readFileFilterComments("crossing_symmetric_triangle.json");
		
		ComponentNode node2 = InputFacade.extractFigure(figureStr2);

		assertTrue(node2 instanceof FigureNode);
	}
	
	@Test
	void testToGeometryRepresentation() 
	{
		//A----B-----C--D-----E----------F
		
		PointDatabase expectedPoints = new PointDatabase();
		expectedPoints.put("A", 0, 0);
		expectedPoints.put("B", 4, 0);
		expectedPoints.put("C", 9, 0);
		expectedPoints.put("D", 11, 0);
		expectedPoints.put("E", 16, 0);
		expectedPoints.put("F", 26, 0);
		
		LinkedHashSet<Segment> expectedSegments = new LinkedHashSet<Segment>();
		Point pointA = new Point("A", 0, 0);
		Point pointB = new Point("B", 4, 0);
		Point pointC = new Point("C", 9, 0);
		Point pointD = new Point("D", 11, 0);
		Point pointE = new Point("E", 16, 0);
		Point pointF = new Point("F", 26, 0);
		
		expectedSegments.add(new Segment(pointA, pointB));
		expectedSegments.add(new Segment(pointB, pointC));
		expectedSegments.add(new Segment(pointC, pointD));
		expectedSegments.add(new Segment(pointD, pointE));
		expectedSegments.add(new Segment(pointE, pointF));

		
		String figureStr = utilities.io.FileUtilities.readFileFilterComments("collinear_line_segments.json");
		ComponentNode node = InputFacade.extractFigure(figureStr);
		PointDatabase actualPoints = InputFacade.toGeometryRepresentation((FigureNode) node).getKey();
		Set<Segment> actualSegments = InputFacade.toGeometryRepresentation((FigureNode) node).getValue();
		
		
		assertEquals(expectedSegments, actualSegments);
		assertEquals(expectedPoints.getPoints(), actualPoints.getPoints());
		
		
		//	     A                                 
		//      / \                                
		//     B___C                               
		//    / \ / \                              
		//   /   X   \ 
		//  D_________E 
		
		PointDatabase expectedPoints2 = new PointDatabase();
		expectedPoints2.put("A", 3, 6);
		expectedPoints2.put("B", 2, 4);
		expectedPoints2.put("C", 4, 4);
		expectedPoints2.put("D", 0, 0);
		expectedPoints2.put("E", 6, 0);
		
		LinkedHashSet<Segment> expectedSegments2 = new LinkedHashSet<Segment>();
		Point point2A = new Point("A", 3, 6);
		Point point2B = new Point("B", 2, 4);
		Point point2C = new Point("C", 4, 4);
		Point point2D = new Point("D", 0, 0);
		Point point2E = new Point("E", 6, 0);
		
		expectedSegments2.add(new Segment(point2A, point2B));
		expectedSegments2.add(new Segment(point2A, point2C));
		expectedSegments2.add(new Segment(point2B, point2C));
		expectedSegments2.add(new Segment(point2B, point2D));
		expectedSegments2.add(new Segment(point2B, point2E));
		expectedSegments2.add(new Segment(point2C, point2E));
		expectedSegments2.add(new Segment(point2C, point2D));
		expectedSegments2.add(new Segment(point2D, point2E));
		
		String figureStr2 = utilities.io.FileUtilities.readFileFilterComments("crossing_symmetric_triangle.json");
		
		ComponentNode node2 = InputFacade.extractFigure(figureStr2);
		PointDatabase actualPoints2 = InputFacade.toGeometryRepresentation((FigureNode) node2).getKey();
		Set<Segment> actualSegments2 = InputFacade.toGeometryRepresentation((FigureNode) node2).getValue();
		
		assertEquals(expectedSegments2, actualSegments2);
		assertEquals(expectedPoints2.getPoints(), actualPoints2.getPoints());
	}
}