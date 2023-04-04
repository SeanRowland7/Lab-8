package input.visitor;

import org.json.JSONArray;
import org.json.JSONObject;

import input.components.FigureNode;
import input.components.point.PointNode;
import input.components.point.PointNodeDatabase;
import input.components.segment.SegmentNode;
import input.components.segment.SegmentNodeDatabase;
/**
* This file converts AST into a JSON object
*  by unparsing the tree
*
* <p>Bugs: None
*
* @author Abby Dumke, Julia Hogg, Sean Rowland
* @date 03/17/2023
*/

public class ToJSONvisitor implements ComponentNodeVisitor {

	/**
	 * Construct and return the JSON representation of the given FigureNode.
	 */
	public Object visitFigureNode(FigureNode node, Object o)
	{
	
		JSONObject figure = new JSONObject();
		//gets description
		figure.put("Description" , node.getDescription());
		
		
		//calls to get Points of AST and adds to sb
		figure.put("Points", node.getPointsDatabase().accept(this, null));
	
		
		//calls to get segments of AST and adds to sb
		figure.put("Segments", node.getSegments().accept(this, null));
		

		JSONObject jsonOb = new JSONObject();
		jsonOb.put("Figure", figure);
		

		return jsonOb;
	}

	/**
	 * Construct and return the JSON representation of the given SegmentNodeDatabase.
	 */
	public Object visitSegmentDatabaseNode(SegmentNodeDatabase node, Object o)
	{
		//JSON object for adjacency list
		JSONObject adjList = new JSONObject();
		
		//loops through all keys in adjacency list (origin points)
		for(PointNode dEdge : node.getAdjList().keySet())
		{
			// adds origin point and calls destinationList which gets all of the adjacent points to origin
			//adjList.put(dEdge.getName(), getDestinationList(node,o,dEdge));
			for(PointNode uEdge : node.getAdjList().get(dEdge))
			{
				JSONArray destList = new JSONArray();
				//append each undirected edge for a given dEdge
				destList.put(uEdge.getName());
			}
			adjList.put(dEdge.getName(), getDestinationList(node,o,dEdge));
					
		}

		return adjList;
	}
	
	/**
	 * Construct and return the JSON representation of the adjacency list for the SegmentNodeDatabase.
	 */
	public Object getDestinationList(SegmentNodeDatabase node, Object o, PointNode dEdge)
	{
		//makes an array of all adjacent points and loops through adjacent points to origin and adds to array
		JSONArray destList = new JSONArray();
		for(PointNode uEdge : node.getAdjList().get(dEdge))
		{
			//append each undirected edge for a given dEdge
			destList.put(uEdge.getName());
		}
	
		return destList;
	}
	
	/**
	 * Construct and return the JSON representation of the given SegmentNode.
	 */
	public Object visitSegmentNode(SegmentNode node, Object o)
	{
		//makes segment object and adds segment; not used since AdjLists gets it from getDestinationList
		JSONObject segment = new JSONObject();
		segment.put(node.getPoint1().getName(), node.getPoint2().getName());
		return segment;
	}

	/**
	 * Construct and return the JSON representation of the given PointNodeDatabase.
	 */
	public Object visitPointNodeDatabase(PointNodeDatabase node, Object o)
	{
		//makes array for all points in pnd
		JSONArray jsonPoints = new JSONArray();
		for(PointNode point : node.getPoints())
		{
			//calls VisitPointNode to get the point and adds to array
			jsonPoints.put(visitPointNode(point, visitPointNode(point,null)));
		}

		return jsonPoints;
	}

	/**
	 * Construct and return the JSON representation of the given PointNode.
	 */
	public Object visitPointNode(PointNode node, Object o)
	{
		//creates object for each point
		JSONObject point = new JSONObject();
		point.put("name", node.getName());
		point.put("x", node.getX());
		point.put("y", node.getY());
		

		return point;
	}
}

