package preprocessor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import exceptions.FactException;
import geometry_objects.Segment;
import geometry_objects.Triangle;

public class TriangleIdentifier
{
	protected Set<Triangle>         _triangles;
	protected Map<Segment, Segment> _segments; // The set of ALL segments for this figure.

	public TriangleIdentifier(Map<Segment, Segment> segments)
	{
		_segments = segments;
	}

	/*
	 * Compute the figure triangles on the fly when requested;
	 * memoize results for subsequent calls.
	 */
	public Set<Triangle> getTriangles()
	{
		if (_triangles != null) return _triangles;

		_triangles = new HashSet<Triangle>();

		computeTriangles();

		return _triangles;
	}

	private void computeTriangles()
	{
		List<Segment> segmentList = new ArrayList<>(_segments.keySet());

		// loop all possible combonations
		for (int i = 0; i < segmentList.size() - 2; i++)
		{
			for (int j = i + 1; j < segmentList.size() - 1; j++)
			{				
				for (int k = j + 1; k < segmentList.size(); k++)
				{
					Segment seg1 = segmentList.get(i);
					Segment seg2 = segmentList.get(j);
					Segment seg3 = segmentList.get(k);

					try 
					{
						// attempt to make and add a new triangle
						Triangle triangle = new Triangle(Arrays.asList(seg1, seg2, seg3));
						_triangles.add(triangle);
					}
					catch (FactException e) {}
				}
			}
		}
	}
}
