package proyectoEda;

import java.util.ArrayList;
import java.util.List;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.MultiGraph;
import Enums.NodeStatus;
import proyectoEda.model.Airport;
import proyectoEda.model.Route;

public class GraphController {
	
	private static GraphController instance = null;

	private GraphController() {
		graph.setStrict(true);
		graph.setAutoCreate( true );
		graph.addAttribute("ui.stylesheet", "url('src/files/style/styleSheet')");
		System.setProperty("org.graphstream.ui.renderer", "org.graphstream.ui.j2dviewer.J2DGraphRenderer");
	}

	public static GraphController getInstance() {
		if (instance == null) {
			instance = new GraphController();
		}
		return instance;
	}
	
	
	
	
	
	public Graph graph = new MultiGraph("Graph");	
	
	public void GenerateStartingGraph()
	{
		String filePathAirplanes = "src/files/airports.txt";
		String filePathRoutes = "src/files/routes.txt";
	
		List<Airport> airports = Airport.GetListAirportsFromCsv(filePathAirplanes);
		List<Route> routes = Route.GetListRoutesFromCsv(filePathRoutes);

		
		
		for (Airport a : airports) {
			if(Integer.parseInt(a.getId()) < 0)
				continue;
			graph.addNode(a.getId());
			Node n = graph.getNode(graph.getNodeCount()-1);
			n.addAttribute("xyz", a.getLongitud(), a.getLatitud(), a.getAltitud());
			n.addAttribute("layout.frozen");
			
		}
		
		for (Route r : routes) {
			
			String sourceId = r.getSourceRouteId();
			String destinationId = r.getDestinationRouteId();	
			
			try {
				if(Integer.parseInt(sourceId) < 0 || Integer.parseInt(destinationId) < 0)
					continue;
			} catch (Exception e) {
				continue;
			}		
		
			String edgeId = sourceId + destinationId;
			
			try {
				graph.addEdge(edgeId, sourceId, destinationId, true);
			} catch (Exception e) {
				System.err.println(e);
				System.err.println("Edge between Node1: " + sourceId + ", Node2: " + destinationId + ". Could not be created " + edgeId);
			}
		}
	}
	
	public void HideAllEdges()
	{
		for (Edge edge : graph.getEachEdge()) {
			edge.addAttribute( "ui.hide" );
		}		
	}
	
	public void ShowAllEdges()
	{
		for (Edge edge : graph.getEachEdge()) {
			edge.removeAttribute( "ui.hide" );
		}	
	}
	
	
	private Node selectedNode1 = null;
	public void NodeSelected(Node node)
	{
		System.out.println("Se selecciono el nodo " + node.getId());
		if(selectedNode1 != null)
			ChangeNodeStatus(NodeStatus.Default, selectedNode1);
		selectedNode1 = node;
		
		ActivateLeavingEdgesFromNode(node);
		ActivateEnteringEdgesFromNode(node);
		
		ChangeNodeStatus(NodeStatus.Selected, selectedNode1);
	}
	
	
	public void ChamgeNodesStatus(NodeStatus status, Iterable<Node> node)
	{
		for (Node n : node) {
			ChangeNodeStatus(status, n);
		}
	}
	
	public void ChangeNodeStatus(NodeStatus status, Node node)
	{
		if(node == null)
			return;
		
		node.removeAttribute("ui.class");
		node.removeAttribute("ui.hide");
		
		switch (status) {
		case Default:
			
			break;
		case Secondary:
			node.addAttribute("ui.class", NodeStatus.Secondary.toString());
			break;
		case Selected:
			node.addAttribute("ui.class", NodeStatus.Selected.toString());
			break;
		case Hiden:
			node.addAttribute( "ui.hide" );
			break;
		default:
			break;
		}
	}
	
	
	
	Iterable<Edge> ActiveLeavingEdges = null;
	Iterable<Edge> ActiveEnteringEdges = null;
	
	
	ArrayList<Node> modifiedLeavingNodes = new ArrayList<Node>();
	ArrayList<Node> modifiedEnteringNodes = new ArrayList<Node>();
	public void ActivateLeavingEdgesFromNode(Node node)
	{		
		if(ActiveLeavingEdges != null)
			HideEdges(ActiveLeavingEdges);
		
		ChamgeNodesStatus(NodeStatus.Default, modifiedLeavingNodes);
		
		modifiedLeavingNodes.clear();
		
		ActiveLeavingEdges = GetLeavingEdgesFromNode(node);
		
		if(ActiveLeavingEdges == null)
			return;
		for (Edge edge : ActiveLeavingEdges) {
			
			if(node != edge.getSourceNode())
				continue;
			modifiedLeavingNodes.add(edge.getTargetNode());
			edge.addAttribute("ui.class", "active, depart");
			edge.removeAttribute( "ui.hide" );
		}
		
		ChamgeNodesStatus(NodeStatus.Secondary, modifiedLeavingNodes);
	}
	
	public void ActivateEnteringEdgesFromNode(Node node)
	{
		if(ActiveEnteringEdges != null)
			HideEdges(ActiveEnteringEdges);
		
		ChamgeNodesStatus(NodeStatus.Default, modifiedEnteringNodes);
		
		modifiedEnteringNodes.clear();
		
		ActiveEnteringEdges = GetEnteringEdgesFromNode(node);
		
		if(ActiveEnteringEdges == null)
			return;
		for (Edge edge : ActiveEnteringEdges) {
			
			if(node != edge.getTargetNode())
				continue;
			modifiedEnteringNodes.add(edge.getSourceNode());
			edge.addAttribute("ui.class", "active, arrive");
			edge.removeAttribute( "ui.hide" );
		}
		
		
		ChamgeNodesStatus(NodeStatus.Secondary, modifiedEnteringNodes);
		
	}
	
	public void HideEdges(Iterable<Edge> edges)
	{
		for (Edge edge : edges) {
			edge.removeAttribute("ui.class");
			edge.addAttribute("ui.hide");
		}
	}
	
	
	public static Iterable<Edge> GetLeavingEdgesFromNode(Node node)
	{
		try {
			Iterable<Edge> edges =  node.getEachLeavingEdge();
			return edges;
		} catch (Exception e) {
			return null;
		}

	}
	
	public static Iterable<Edge> GetEnteringEdgesFromNode(Node node)
	{
		try {
			Iterable<Edge> edges =  node.getEachEnteringEdge();
			return edges;
		} catch (Exception e) {
			return null;
		}

	}
	
	
}
