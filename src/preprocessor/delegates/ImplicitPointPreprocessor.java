package preprocessor.delegates;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import geometry_objects.Segment;
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
	 * Compare each segment to all other segments
	 * if there is an intersection between lines -> add it to the list
	 *    
	 */
	public static Set<Point> compute(PointDatabase givenPoints, List<Segment> givenSegments)
	{
		Set<Point> implicitPoints = new LinkedHashSet<Point>();

		//compare all points to one another
		for(int seg1Index = 0; seg1Index < givenSegments.size() - 1; seg1Index++)
		{
			for(int seg2Index = seg1Index + 1; seg2Index < givenSegments.size(); seg2Index++)
			{
				// get two segments and find their intersections
				Segment seg1 = givenSegments.get(seg1Index);
				Segment seg2 = givenSegments.get(seg2Index);

				Point intersectionPoint = seg1.segmentIntersection(seg2);

				// If an intersection point exists
				// -> put it in the database
				if(intersectionPoint != null) implicitPoints.add(intersectionPoint);

			}
		}
		return implicitPoints;
	}
}