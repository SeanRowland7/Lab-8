package preprocessor;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.TreeSet;

import geometry_objects.points.Point;
import geometry_objects.points.PointDatabase;
import preprocessor.delegates.ImplicitPointPreprocessor;
import geometry_objects.Segment;

/**
* A preprocessor class that provides various computations for geometry figures.
*
* <p>Bugs: None
*
* @author Sean Rowland, Caden Parry
* @date 14 April 2023
*/
public class Preprocessor
{
	// The explicit points provided to us by the user.
	// This database will also be modified to include the implicit
	// points (i.e., all points in the figure).
	protected PointDatabase _pointDatabase;

	// Minimal ('Base') segments provided by the user
	protected Set<Segment> _givenSegments;

	// The set of implicitly defined points caused by segments
	// at implicit points.
	protected Set<Point> _implicitPoints;

	// The set of implicitly defined segments resulting from implicit points.
	protected Set<Segment> _implicitSegments;

	// Given all explicit and implicit points, we have a set of
	// segments that contain no other subsegments; these are minimal ('base') segments
	// That is, minimal segments uniquely define the figure.
	protected Set<Segment> _allMinimalSegments;

	// A collection of non-basic segments
	protected Set<Segment> _nonMinimalSegments;

	// A collection of all possible segments: maximal, minimal, and everything in between
	// For lookup capability, we use a map; each <key, value> has the same segment object
	// That is, key == value. 
	protected Map<Segment, Segment> _segmentDatabase;
	public Map<Segment, Segment> getAllSegments() { return _segmentDatabase; }

	public Preprocessor(PointDatabase points, Set<Segment> segments)
	{
		_pointDatabase  = points;
		_givenSegments = segments;

		_segmentDatabase = new HashMap<Segment, Segment>();

		analyze();
	}

	/**
	 * Invoke the precomputation procedure.
	 */
	public void analyze()
	{
		//
		// Implicit Points
		//
		
		// change so that the database is updated
		_implicitPoints = ImplicitPointPreprocessor.compute(_pointDatabase, _givenSegments.stream().toList());

		//
		// Implicit Segments attributed to implicit points
		//
		_implicitSegments = computeImplicitBaseSegments(_implicitPoints);

		//
		// Combine the given minimal segments and implicit segments into a true set of minimal segments
		//     * givenSegments may not be minimal
		//     * implicitSegmen
		//
		_allMinimalSegments = identifyAllMinimalSegments(_implicitPoints, _givenSegments, _implicitSegments);

		//
		// Construct all segments inductively from the base segments
		//
		_nonMinimalSegments = constructAllNonMinimalSegments(_allMinimalSegments);

		//
		// Combine minimal and non-minimal into one package: our database
		//
		_allMinimalSegments.forEach((segment) -> _segmentDatabase.put(segment, segment));
		_nonMinimalSegments.forEach((segment) -> _segmentDatabase.put(segment, segment));
	}


	//
	// Implicit Segments attributed to implicit points
	//
	public Set<Segment> computeImplicitBaseSegments(Set<Point> implicitPoints) 
	{
		Set<Segment> implicitBaseSegments = new LinkedHashSet<Segment>();

		// for each given segment
		for (Segment seg : _givenSegments)
		{
			Set<Point> ptList = new TreeSet<Point>();

			// add the segments endpoints to a point list
			ptList.add(seg.getPoint1());
			ptList.add(seg.getPoint2());

			// add all implicit points that lie on the segment to a point list
			ptList.addAll(seg.collectOrderedPointsOnSegment(implicitPoints));

			// convert the point list to an array
			Point[] ptArr = ptList.toArray(new Point[ptList.size()]);

			// attempt to connect each point to the one in front of it
			for (int x = 0; x < ptArr.length - 1; x++)
			{
				Segment newSeg = new Segment(ptArr[x], ptArr[x+1]);

				// if the newSeg isnt a given segment
				// -> add

				if(!_givenSegments.contains(newSeg)) implicitBaseSegments.add(newSeg);
			}
		}
		return implicitBaseSegments;
	}


	//
	// Combine the given minimal segments and implicit segments into a true set of minimal segments
	//     * givenSegments may not be minimal
	//     * implicitSegmen
	//
	public Set<Segment> identifyAllMinimalSegments(Set<Point> implicitPoints, Set<Segment> givenSegments,
			Set<Segment> implicitSegments) 
	{
		Set<Segment> allMinimalSegments = new HashSet<Segment>();

		// add all implicit and given segments
		allMinimalSegments.addAll(implicitSegments);
		allMinimalSegments.addAll(givenSegments);


		// compare each seg to each point
		for (Segment seg : givenSegments)
		{
			for (Point pt : implicitPoints)
			{
				// remove the segment if an implicit point lies on it
				if (seg.pointLiesBetweenEndpoints(pt)) allMinimalSegments.remove(seg);
			}	
		}

		return allMinimalSegments;
	}

	
	public Set<Segment> constructAllNonMinimalSegments(Set<Segment> allMinimalSegments) 
	{
		Set<Segment> allNonMinimalSegments = new HashSet<Segment>();

		// For each minimal segment, find all the non-minimal segments that that segment is apart of.
		for (Segment seg1 : allMinimalSegments)
		{
			// Get all the minimal segments that are collinear to seg1.
			HashSet<Segment> collinearMinimalSegments = getCollinearMinimalSegments(seg1, allMinimalSegments);
			
			// Calculate all the non-minimal segments that can be constructed from the set of collinear minimal segments.
			allNonMinimalSegments.addAll(getNonMinimalSegments(collinearMinimalSegments));
		}
		
		return allNonMinimalSegments;
	}

	// Get all the minimal segments that are collinear to seg1.
	private HashSet<Segment> getCollinearMinimalSegments(Segment seg1, Set<Segment> allMinimalSegments) 
	{
		HashSet<Segment> collinearMinimalSegments = new HashSet<Segment>();
		
		// Compare every other segment to seg1 and add it to the set if it is collinear.
		for(Segment seg2 : allMinimalSegments)
		{
			if(seg1.coincideWithoutOverlap(seg2))
			{
				collinearMinimalSegments.add(seg2);
			}
		}
		
		return collinearMinimalSegments;
	}

	// Calculate all the non-minimal segments that can be constructed from the set of collinear minimal segments.
	private Set<Segment> getNonMinimalSegments(Set<Segment> collinearMinimalSegments) 
	{
		Set<Segment> nonMinimalSegments = new HashSet<Segment>();
		
		// Create a queue of segments to process that begins with the set of collinear minimal segments.
		Queue<Segment> segsToProcess = new LinkedList<Segment>(collinearMinimalSegments);
		
		// As long as there are segments to process...
		while(!segsToProcess.isEmpty())
		{
			// Get the next segment to be processed.
			Segment seg1 = segsToProcess.remove();
			
			// Go through all the collinear minimal segments and see if a new segment can be constructed.
			for(Segment seg2 : collinearMinimalSegments)
			{
				Point pt = seg1.sharedVertex(seg2);
				
		//make own method
				// Check whether the two segments share a vertex and coincide without overlapping.
				if(pt != null && seg1.coincideWithoutOverlap(seg2))
				{
					// If they do then construct a segment with each non-shared endpoint between the two segments.
					Segment constructedSeg = new Segment(seg1.other(pt), seg2.other(pt));
					
					// If the constructed segment is new and not minimal, then we found a new nonminimal segment.
					if(!nonMinimalSegments.contains(constructedSeg) && !collinearMinimalSegments.contains(constructedSeg) )
					{
						
						nonMinimalSegments.add(constructedSeg);
						
						// Add the new nonminimal segment back onto the queue 
						//  to check if it combines with other minimal segments to form nonminimal segments.
						segsToProcess.add(constructedSeg);
					}
				}
			}
		}
		
		return nonMinimalSegments;
	}
}
