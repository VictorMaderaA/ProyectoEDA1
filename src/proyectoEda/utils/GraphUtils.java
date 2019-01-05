package proyectoEda.utils;

import java.util.ArrayList;
import java.util.Collection;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Node;

public class GraphUtils {

	ArrayList<Node> nodesFinded = new ArrayList<Node>();
	
	public ArrayList<Node> GetPosibleNodesFromNode(Node node)
	{
		Collection<Edge> edges = null;
		try {
			edges = node.getLeavingEdgeSet();
		} catch (Exception e) {
			edges = null;
		}
		
		
		if(edges == null || edges.isEmpty())
			return null;
		
		for (Edge e : edges) {
			Node newNode = e.getTargetNode();
			if(nodesFinded.contains(newNode))
				continue;
			nodesFinded.add(newNode);
			GetPosibleNodesFromNode(newNode);
		}
		return nodesFinded;
	}
	
	
}
