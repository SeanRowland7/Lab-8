package input.components.segment;

/**
 * Creates a database of SegmentNodes by creating a dictionary of connecting PointNodes. The user may only add connections that go both ways.
 * @author Hayes Brown and Caden Parry
 * @date 1/26/23
 */


import java.util.*;

import input.components.ComponentNode;
import input.components.point.PointNode;
import input.visitor.ComponentNodeVisitor;
import utilities.io.StringUtilities;

public class SegmentNodeDatabase implements ComponentNode{


private Map<PointNode, Set<PointNode>> _adjLists;

public SegmentNodeDatabase()
{
	_adjLists = new HashMap<PointNode, Set<PointNode>>();
}

public SegmentNodeDatabase(Map<PointNode, Set<PointNode>> adjLists)
{
	_adjLists = adjLists;
}

public Map<PointNode, Set<PointNode>> getAdjList(){
	return _adjLists;
}

/**
 * This method counts the number of directed edges in a geometric shape.
 * This method assumes that all edges are directed ie. if pointA points to some other pointB, pointB will always point back at PointA.
 * 
 * @return the number of directed edges in a given geometric shape
 */
public int numUndirectedEdges()
{
	//divides number of directed edges by 2 to get number directed
	return numDirectedEdges() / 2;
}

/**
 * This method counts the number of undirected edges in a geometric shape.
 * This method assumes that all edges are directed ie. if pointA points to some other pointB, pointB will always point back at PointA
 * 
 * @return the number of undirected edges in a given geometric shape
 */
public int numDirectedEdges()
{
	int numEdges = 0;
	for (PointNode point : _adjLists.keySet())
	{
		numEdges += _adjLists.get(point).size();
	}
	return numEdges;
}

/**
 * This method takes in a directed edge and an undirected edge and puts them into a HashMap. The directed edge will be the key in the 
 * map while the undirected edge is one of the value(s) in a HashSet that the key is mapped to.
 * 
 * @param directedE - PointNode that will act as the key of the map
 * @param undirectedE - PointNode that the key maps to
 */
private void addDirectedEdge(PointNode directedE, PointNode undirectedE)
{
	//checks see if the directed edge exists
	if(!_adjLists.containsKey(directedE))
	{
		//puts the directed edge and its set in the database
		_adjLists.put(directedE, new HashSet<PointNode>());
	}
	//puts in the undirected edge in the map
	_adjLists.get(directedE).add(undirectedE);
}

/**
 * Leverages a private method to map two points to one another. For this implementation, order of paramerters does not matter 
 * because the points will both map to each other
 * 
 * @param point1
 * @param point2
 */
public void addUndirectedEdge(PointNode point1, PointNode point2)
{
	//maps the points in both directions 
	addDirectedEdge(point1, point2);
	addDirectedEdge(point2, point1);
}

/**
 * This method maps a specified point to the contents of a list (if the given point is not mapped to that).
 * 
 * @param point - specific point that will map to all of the unique elements in the list
 * @param list - list that contains PointNodes that point will map to
 */
public void addAdjacencyList(PointNode point, List<PointNode> list)
{
	for (PointNode items : list)
	{
		//call addUndirectedEdge to put each item in
		addUndirectedEdge(point, items);
	}
}

/**
 * This method returns all of the mappings in the adjacency list (even if they are not unique).
 * 
 * @return List of every segment in the adjacency list
 */
public List<SegmentNode> asSegmentList()
{		
	List<SegmentNode> segments = new ArrayList<SegmentNode>();

	//nested loop to get all point combinations
	for(PointNode dEdge : _adjLists.keySet())
	{
		for(PointNode uEdge : _adjLists.get(dEdge))
		{
			//adds all undirected edges for each directed edge
			segments.add(new SegmentNode(dEdge, uEdge));
		}
	}
	return segments;
}

/**
 * This method returns unique mapping in the adjacency list
 * 
 * @return List of all the unique segments in the adjacency list
 */
public List<SegmentNode> asUniqueSegmentList()
{
	//same process as asSegmentList() until most inner code
	List<SegmentNode> segmentList = new ArrayList<SegmentNode>();
	for(PointNode dEdge : _adjLists.keySet())
	{
		for(PointNode uEdge : _adjLists.get(dEdge))
		{
			//create SegmentNode object and use below
			SegmentNode segment = new SegmentNode(dEdge, uEdge);

			//check to see if the segment is already in the segment list
			if(!segmentList.contains(segment))
			{
				segmentList.add(segment);
			}
		}
	}
	return segmentList;
}

@Override
public Object accept(ComponentNodeVisitor visitor, Object o) 
{
	return visitor.visitSegmentDatabaseNode(this, o);
}


}
