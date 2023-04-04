package input.components.point;

/**
 * Creates a database of PointNodes by creating a LinkedHashSet.
 * @author Hayes Brown and Caden Parry
 * @date 1/26/23
 */

import java.util.*;

import input.components.ComponentNode;
import input.visitor.ComponentNodeVisitor;
import utilities.io.StringUtilities;

public class PointNodeDatabase implements ComponentNode{

	protected Set<PointNode> _points;

	public PointNodeDatabase()
	{
		_points = new LinkedHashSet<PointNode>();
	}

	public PointNodeDatabase(List<PointNode> list)
	{
		_points = new LinkedHashSet<PointNode>(list);
	}

	public Set<PointNode> getPoints(){
		return _points;
	}
	/**
	 * Puts a PointNode into the map
	 * @param point
	 * @return void
	 */
	public void put(PointNode point)
	{
		if(point != null) _points.add(point);
	}

	/**
	 * checks to see if a PointNode is contained in the database
	 * @param point
	 * @return true if the PointNode is contained in the database, false otherwise
	 */
	public boolean contains(PointNode point)
	{
		return _points.contains(point);
	}

	/**
	 * Checks to see if an ordered pair is contained in the database
	 * @param x
	 * @param y
	 * @return true if the specified coordinates are contained in the database, false otherwise
	 */
	public boolean contains(double x, double y)
	{
		return _points.contains(new PointNode(x, y));
	}

	/**
	 * Gets the name of a PointNode
	 * 
	 * @param point
	 * @return the name of the PointNode as a String
	 */
	public String getName(PointNode point)
	{
		PointNode newPoint = getPoint(point);
		if(newPoint == null) return null;
		return newPoint._name;
	}

	/**
	 * Gets the name of a PointNode based on an inputed ordered pair
	 * 
	 * @param x - x-axis value
	 * @param y - y-axis value
	 * @return the name of the node with coordinates that match the params
	 */
	public String getName(double x, double y)
	{	
		PointNode point = getPoint(x, y);
		if(point == null) return null;
		return point._name;

	}

	/**
	 * Gets the point associated with a point, coordinate, or name
	 * 
	 * @param a point or a coordinate
	 * @return the point if it is in the database
	 */
	public PointNode getPoint(PointNode that)
	{
		for(PointNode point : _points)
		{
			if(point.equals(that)) return point;
		}
		return null;
	}

	public PointNode getPoint(double x, double y)
	{
		return getPoint(new PointNode(x, y));
	}

	public PointNode getPoint(String name)
	{
		for(PointNode point : _points)
		{
			if(point._name.equals(name)) return point;
		}
		return null;
	}
	@Override
	public Object accept(ComponentNodeVisitor visitor, Object o) 
	{
		return visitor.visitPointNodeDatabase(this, o);
	}


}