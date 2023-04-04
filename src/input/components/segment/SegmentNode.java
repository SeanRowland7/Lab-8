package input.components.segment;
/**
 * A line between two PointNodes
 * @author Hayes Brown and Caden Parry
 * @date 1/26/23
 */

import input.components.point.PointNode;

/**
* A utility class only for representing ONE segment
*/
public class SegmentNode
{
protected PointNode _point1;
protected PointNode _point2;

public PointNode getPoint1() { return _point1; }
public PointNode getPoint2() { return _point2; }

public SegmentNode(PointNode pt1, PointNode pt2)
{
	_point1 = pt1;
	_point2 = pt2;
}

/**
 * compares SegmentNodes
 * @param that -- SegmentNode that is compared
 * @return boolean -- returns true if 
 */
@Override
public boolean equals(Object o)
{
	if(o == null) return false;
	if(!(o instanceof SegmentNode)) return false;
	
	SegmentNode that = (SegmentNode) o;
	
	return (this._point1.equals(that._point1) && this._point2.equals(that._point2)) ||
	       (this._point1.equals(that._point2) && this._point2.equals(that._point1));
}

public String toString()
{
	return "Point1: " + _point1 + ", Point2: " + _point2 + ")";
}
}