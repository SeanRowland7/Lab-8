package preprocessor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import exceptions.FactException;
import geometry_objects.Segment;
import geometry_objects.angle.Angle;
import geometry_objects.angle.AngleEquivalenceClasses;

/**
 * The AngleIdentifier class provides functionality to compute the angles
 *  present in a geometry figure given ALL segments present in that figure
 *
 * <p>Bugs: None
 *
 * @author Sean Rowland, Caden Parry
 * @date   24 April 2023
 */
public class AngleIdentifier
{
	protected AngleEquivalenceClasses _angles;
	protected Map<Segment, Segment> _segments; // The set of ALL segments for this figure

	public AngleIdentifier(Map<Segment, Segment> segments)
	{
		_segments = segments;
	}

	/*
	 * Compute the figure triangles on the fly when requested; memorize results for subsequent calls.
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
		// Get the list of all segments.
		List<Segment> segmentList = new ArrayList<>(_segments.keySet());
		
		// Check each combination of segments
		for (int i = 0; i < segmentList.size() - 1; i++)
		{
			for (int j = i + 1; j < segmentList.size(); j++)
			{
				try 
				{
					// If two segments share a vertex, add the angle to an equivalence class.
					_angles.add(new Angle(segmentList.get(i), segmentList.get(j)));
				} 
				// If the angle cannot be constructed, do nothing.
				catch (FactException e) {}
			}
		}
	}
}
