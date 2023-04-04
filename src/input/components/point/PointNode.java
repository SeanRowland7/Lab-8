package input.components.point;

import input.components.ComponentNode;
import utilities.math.MathUtilities;

/**
* A 2D Point (x, y).
*/
public class PointNode
{
protected static final String ANONYMOUS = "__UNNAMED";

protected double _x;
public double getX() { return this._x; }

protected double _y; 
public double getY() { return this._y; }

protected String _name; 
public String getName() { return _name; }

/**
 * Create a new Point with the specified coordinates.
 * @param x The X coordinate
 * @param y The Y coordinate
 */
public PointNode(double x, double y)
{
	_x = x;
	_y = y;
	_name = ANONYMOUS;
}

/**
 * Create a new Point with the specified coordinates.
 * @param name -- The name of the point. (Assigned by the UI)
 * @param x -- The X coordinate
 * @param y -- The Y coordinate
 */
public PointNode(String name, double x, double y)
{
	_x = x;
	_y = y;
	_name = name;
	
	if(name == null || name.equals(""))
		_name = ANONYMOUS;
}

@Override
public int hashCode()
{
	return Double.valueOf(_x).hashCode() + Double.valueOf(_y).hashCode();
}

/**
 * compares PointNodes
 * @param that -- PointNode compared with the object used to call the method
 * @return boolean -- returns true if the both coordinates are equal
 */
public boolean equals(Object o)
{
	if(o == null) return false;
	if(!(o instanceof PointNode)) return false;
	
	PointNode that = (PointNode) o;
	return (MathUtilities.doubleEquals(this._x, that._x) && MathUtilities.doubleEquals(this._y, that._y));
}

@Override
public String toString()
{
	return _name + "(" + _x + ", " + _y + ")";
}

}