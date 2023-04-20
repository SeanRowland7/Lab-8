package preprocessor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import exceptions.FactException;
import geometry_objects.Segment;
import geometry_objects.angle.Angle;
import geometry_objects.angle.AngleEquivalenceClasses;

public class AngleIdentifier
{
	protected AngleEquivalenceClasses _angles;
	protected Map<Segment, Segment> _segments; // The set of ALL segments for this figure

	public AngleIdentifier(Map<Segment, Segment> segments)
	{
		_segments = segments;
	}

	/*
	 * Compute the figure triangles on the fly when requested; memoize results for subsequent calls.
	 */
	public AngleEquivalenceClasses getAngles()
	{
		if (_angles != null) return _angles;

		_angles = new AngleEquivalenceClasses();

		computeAngles();

		return _angles;
	}

	private void computeAngles()
	{
		//get all the segments in the figure
		List<Segment> segmentList = new ArrayList<>(_segments.keySet());
		
		//loop through all combinations
		for (int i = 0; i < segmentList.size() - 2; i++)
		{
			for (int j = i + 1; j < segmentList.size() - 1; j++)
			{
				Segment seg1 = segmentList.get(i);
				Segment seg2 = segmentList.get(j);

				try 
				{
					// If two segs share an endpoint, get the angle between them
					// Add the angle to an equivalence class.
					_angles.add(new Angle(seg1, seg2));
				}catch (FactException e) {}
			}
		}
	}
}
