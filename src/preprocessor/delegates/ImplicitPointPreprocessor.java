package preprocessor.delegates;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import geometry_objects.Segment;
import geometry_objects.delegates.intersections.IntersectionDelegate;
import geometry_objects.points.Point;
import geometry_objects.points.PointDatabase;

public class ImplicitPointPreprocessor
{
	/**
	 * It is possible that some of the defined segments intersect
	 * and points that are not named; we need to capture those
	 * points and name them.
	 * 
	 * Algorithm:
	 *    
	 */
	public static Set<Point> compute(PointDatabase givenPoints, List<Segment> givenSegments)
	{
		Set<Point> implicitPoints = new LinkedHashSet<Point>();

        for(int seg1Index = 0; seg1Index < givenSegments.size() - 1; seg1Index++)
        {
        	for(int seg2Index = seg1Index + 1; seg2Index < givenSegments.size(); seg2Index++)
        	{
        		Segment seg1 = givenSegments.get(seg1Index);
        		Segment seg2 = givenSegments.get(seg2Index);
        		
        		Point intersectionPoint = IntersectionDelegate.segmentIntersection(seg1, seg2);
        		
        		int sizePrior = givenPoints.size();
        		
        		// Put the point in the database so it gets a generated name
        		givenPoints.put(intersectionPoint.getX(), intersectionPoint.getY());
        		
        		
        		
        		// If an intersection point exists, then attempt to put it in the database
        		if(intersectionPoint != null && isImplicit(intersectionPoint, givenPoints)) 
        		{
        			implicitPoints.add(intersectionPoint);
        			
        		}
        	}
        }

		return implicitPoints;
	}

	
	private static boolean isImplicit(Point p, PointDatabase givenPoints)
	{
		return givenPoints.getPoint(p) != null;
	}
}
