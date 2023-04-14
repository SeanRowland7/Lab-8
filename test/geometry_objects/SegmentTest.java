package geometry_objects;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

import geometry_objects.points.Point;

class SegmentTest {

	@Test
	void testHasSubSegment() 
	{
		/**
		 *   A-------B-------C------D     E
		 * 
		 */
		
		Point ptA = new Point("A", 0,0);
		Point ptB = new Point("B", 1,0);
		Point ptC = new Point("C", 2,0);
		Point ptD = new Point("D", 3,0);
		Point ptE = new Point("E", 4,0);

		Segment segAD = new Segment(ptA, ptD);
		Segment segAB = new Segment(ptA, ptB);
		Segment segBC = new Segment(ptB, ptC);
		Segment segAE = new Segment(ptA, ptE);

		assertTrue(segAD.HasSubSegment(segAD));
		assertTrue(segAD.HasSubSegment(segAB));
		assertTrue(segAD.HasSubSegment(segBC));

		assertFalse(segAD.HasSubSegment(segAE));


		/**
		 *   A-------B-------C------D------E
		 * 
		 */

		assertTrue(segAE.HasSubSegment(segAD));
		assertTrue(segAE.HasSubSegment(segAB));
		assertTrue(segAE.HasSubSegment(segBC));
	}

	@Test
	void testCoincideWithoutOverlapDistantSegmentsHorizontal() 
	{
		/**
		 *       this                     that
		 *  |----------------|           |===========|
		 *  A				 B	         C           D
		 */

		Point ptA = new Point("A", 0,0);
		Point ptB = new Point("B", 1,0);
		Point ptC = new Point("C", 3,0);
		Point ptD = new Point("D", 4,0);

		Segment segAB = new Segment(ptA, ptB);
		Segment segCD = new Segment(ptC, ptD);
		

		assertTrue(segAB.coincideWithoutOverlap(segCD));
		assertTrue(segCD.coincideWithoutOverlap(segAB));
	}

	@Test
	void testCoincideWithoutOverlapSharingEndpoint() 
	{
		/**
		 *		this        that
		 *   |----------|==========|   
		 *   A          B          C
		 */

		Point ptA = new Point("A", 0,0);
		Point ptB = new Point("B", 1,0);
		Point ptC = new Point("C", 2,0);

		Segment segAB = new Segment(ptA, ptB);
		Segment segBC = new Segment(ptB, ptC);
		
		assertTrue(segAB.coincideWithoutOverlap(segBC));
		assertTrue(segBC.coincideWithoutOverlap(segAB));
	}

	@Test
	void testCoincideWithoutOverlapOverlap() 
	{
		/**
		 *			  this 
		 *   |----------|=-=-=-=-=-|   
		 *   A          B          C
		 *   				that
		 */
		
		Point ptA = new Point("A", 22/54, 0);
		Point ptB = new Point("B", 31/32, 0);
		Point ptC = new Point("C", 59/32, 0);
		
		Segment segAC = new Segment(ptA, ptC);
		Segment segBC = new Segment(ptB, ptC);
		
		assertTrue(segAC.coincideWithoutOverlap(segBC));
		assertTrue(segBC.coincideWithoutOverlap(segAC));
	}

	@Test
	void testCoincideWithoutOverlapDoesNotCoincide() 
	{
		/**
		 *       this                     
		 *       
		 *  |----------------|     
		 *  A				 B	    
		 *  	   that
		 *     |===========|
		 *     C           D
		 */
		
		Point ptA = new Point("A", 0, 0);
		Point ptB = new Point("B", 0, 10);
		Point ptC = new Point("C", -2, 2);
		Point ptD = new Point("C", -2, 8);
		
		Segment segAB = new Segment(ptA, ptB);
		Segment segCD = new Segment(ptC, ptD);

		assertFalse(segAB.coincideWithoutOverlap(segCD));
		assertFalse(segCD.coincideWithoutOverlap(segAB));	
	}



	@Test
	void testCollectOrderedPointsOnSegment() 
	{
		/**
		 *							   Q *
		 *
		 *                A-------B-------C------D     E
		 *
		 *      * Z
		 *      
		 */
		
		Set<Point> points = new HashSet<Point>();
		Set<Point> answer = new HashSet<Point>();

		
		Point ptA = new Point("A", 0,0); 	points.add(ptA);	answer.add(ptA);
		Point ptB = new Point("B", 1,0); 	points.add(ptB);	answer.add(ptB);
		Point ptC = new Point("C", 2,0); 	points.add(ptC);	answer.add(ptC);
		Point ptD = new Point("D", 3,0); 	points.add(ptD); 	answer.add(ptD);
		
		Point ptE = new Point("E", 4,0); 	points.add(ptE);
		Point ptQ = new Point("Q", 2,1); 	points.add(ptQ);
		Point ptZ = new Point("Z", -1,-1); 	points.add(ptZ);
		
		Segment segAD = new Segment(ptA, ptD);
		assertEquals(answer, segAD.collectOrderedPointsOnSegment(points));
	}

}
