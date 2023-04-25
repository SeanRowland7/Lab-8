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
/**
 * The TriangleIdentifier class provides functionality to compute the triangles
 *  present in a geometry figure given ALL segments present in that figure
 *
 * <p>Bugs: None
 *
 * @author Sean Rowland, Caden Parry
 * @date   24 April 2023
 */
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
	 * memorize results for subsequent calls.
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

		// Check all possible combinations of segments.
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
						// Attempt to make and add a new triangle
						_triangles.add(new Triangle(Arrays.asList(seg1, seg2, seg3)));
					}
					// If the triangle cannot be constructed, do nothing.
					catch (FactException e) {}
				}
			}
		}
	}
}
