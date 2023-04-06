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
	 *    TODO
	 */
	public static Set<Point> compute(PointDatabase givenPoints, List<Segment> givenSegments)
	{
		Set<Point> implicitPoints = new LinkedHashSet<Point>();

		for (int  i = 0; i < givenSegments.size(); i++)
		{
			for (int j = i + 1; j < givenSegments.size(); j++)
			{
				Segment seg1 = givenSegments.get(i);
				Segment seg2 = givenSegments.get(j);

				Point connection = IntersectionDelegate.segmentIntersection(seg1, seg2);

				Point isIn = givenPoints.getPoint(connection);

				if (isIn == null)
				{
					givenPoints.put(Point.ANONYMOUS, connection.getX(), connection.getY());
					implicitPoints.add(connection);
				}
			}
		}

		return implicitPoints;
	}

}
