package geometry_objects;

import static org.junit.jupiter.api.Assertions.*;

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
		Point ptB = new Point("B", 0,1);
		Point ptC = new Point("C", 0,2);
		Point ptD = new Point("D", 0,3);
		Point ptE = new Point("E", 0,4);

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
		Point ptB = new Point("B", 0,1);
		Point ptC = new Point("C", 0,3);
		Point ptD = new Point("D", 0,4);

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
		Point ptB = new Point("B", 0,1);
		Point ptC = new Point("C", 0,2);

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



		/**
        		this    B    that
		 *   |----------|==========|   
		 *   A          B          C
		 */
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
	}

}
