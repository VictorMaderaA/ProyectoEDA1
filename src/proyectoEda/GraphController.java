package proyectoEda;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.JOptionPane;

import org.graphstream.algorithm.Dijkstra;
import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.MultiGraph;

import Enums.EdgeStatus;
import Enums.GSelectMode;
import Enums.NodeStatus;
import proyectoEda.model.Airport;
import proyectoEda.model.Route;
import proyectoEda.utils.GraphUtils;
import proyectoEda.utils.MapUtils;

public class GraphController {
	
	
	private static GraphController instance = null;

	private GraphController() {
		graph.setStrict(true);
		graph.setAutoCreate( true );
		graph.addAttribute("ui.stylesheet", "url('src/files/style/styleSheet')");
		System.setProperty("org.graphstream.ui.renderer", "org.graphstream.ui.j2dviewer.J2DGraphRenderer");
		
		GenerateStartingGraph();
	}

	public static GraphController getInstance() {
		if (instance == null) {
			instance = new GraphController();
		}
		return instance;
	}
	
	
	ArrayList<Node> lastEdgeTargetNodes = new ArrayList<Node>();
	ArrayList<Node> lastEdgeSourceNodes = new ArrayList<Node>();
	
	ArrayList<Node> lastRouteNodes = new ArrayList<Node>();
	
	private Node[] selectedNodes = new Node[2];
	private GSelectMode selectionMode = GSelectMode.ArrDep;
	private GSelectMode prevSelecMode = null;
	
	
	final String filePathAirplanes = "src/files/airports.txt";
	final String filePathRoutes = "src/files/routes.txt";
	
	private final String  GHIDE= "ui.hide";
	private final String  GCLASS= "ui.class";
	
	public Graph graph = new MultiGraph("Graph");		
	
	
	private void GenerateStartingGraph()
	{	
		AddAirportsToGraph(Airport.GetListAirportsFromCsv(filePathAirplanes));
		AddRoutesToGraph(Route.GetListRoutesFromCsv(filePathRoutes));
		RemoveNodesWithoutEdges();
	}
	
	private void AddAirportsToGraph(Iterable<Airport> airports)
	{
		for (Airport a : airports) {			
			graph.addNode(a.getId()).addAttribute("xyz", a.getLongitud(), a.getLatitud(), a.getAltitud());
			graph.getNode(a.getId()).addAttribute("name", a.getName());
		}
	}
	
	private void AddRoutesToGraph(Iterable<Route> routes)
	{
		for (Route r : routes) {
			
			String sourceId = r.getSourceRouteId();
			String destinationId = r.getDestinationRouteId();	
				
		
			String edgeId = sourceId + destinationId;
					
			try {
				Object[] posa = graph.getNode(sourceId).getAttribute("xyz");
				Object[] posb = graph.getNode(destinationId).getAttribute("xyz");
				double distance = MapUtils.distanceToCordinates(posa[0], posb[0], posa[1], posb[1], posa[2], posb[2]);
				graph.addEdge(edgeId, sourceId, destinationId, true).addAttribute("lenght", distance);;
			} catch (Exception e) {
				System.err.println(e);
				System.err.println("Edge between Node1: " + sourceId + ", Node2: " + destinationId + ". Could not be created " + edgeId);
			}
		}
	}
	
	private void RemoveNodesWithoutEdges()
	{
		for (Node node : graph) {
			int degree = node.getDegree();
			
			if(degree <= 0)
			{
				graph.removeNode(node);
//				System.out.println(node.getId() + " | Degree: " + degree + " -> REMOVED");
			}
		}
	}
	
	
	
	
	
	public void HideAllEdges()
	{
		ChangeEdgesStatus(EdgeStatus.Default, graph.getEachEdge());
	}
	
	public void ShowAllEdges()
	{
		ChangeEdgesStatus(EdgeStatus.Show, graph.getEachEdge());
	}
	
	public void HideAllNodes()
	{
		ChangeNodesStatus(NodeStatus.Hiden, graph.getEachNode());
	}
	
	public void ShowAllNodes()
	{
		ChangeNodesStatus(NodeStatus.Default, graph.getEachNode());
	}
	
	
	
	
	
	
	public void ShowInfo()
	{
		if(selectedNodes[0] == null && selectedNodes[1] == null)
			return;
		String s = "Selected Airport: " + selectedNodes[0].getId() + "\n";
		int x = 1;
		switch (selectionMode) {
		case Arr:
			s += "Available Destinations: \n";
			x = 1;
			for (Node n : lastEdgeTargetNodes) {
				s += "Id: " + n.getId() + "  ||  ";
				if(x > 6)
				{
					s += "\n"; x = 0;
				}		
				x++;
			}
			s += "\n";
			break;
		case Dep:
			x = 1;
			s += "Destinations to arrive from: \n";
			for (Node n : lastEdgeSourceNodes) {
				s += "Id: " + n.getId() + "  ||  ";
				if(x > 6)
				{
					s += "\n"; x = 0;
				}
				x++;
			}
			s += "\n";
			break;
		case ArrDep:
			s += "Available Destinations: \n";
			x = 1;
			for (Node n : lastEdgeTargetNodes) {
				s += "Id: " + n.getId() + "  ||  ";
				if(x > 6)
				{
					s += "\n"; x = 0;
				}		
				x++;
			}
			s += "\n";
			
			
			x = 1;
			s += "Destinations to arrive from: \n";
			for (Node n : lastEdgeSourceNodes) {
				s += "Id: " + n.getId() + "  ||  ";
				if(x > 6)
				{
					s += "\n"; x = 0;
				}
				x++;
			}
			s += "\n";
			break;
		case Route:
			s += "Destination Airport: " + selectedNodes[1].getId() + "\n";
			s += "Route to take: \n";
			
			int i = 1;
			for (Node n : lastRouteNodes) {
				s += i + "-.  Id: " + n.getId() + "\n";
				i++;
			}
			s += "\n";
			break;
			
		default:
			break;
		}
		
		JOptionPane.showMessageDialog(null, s, "Info", JOptionPane.INFORMATION_MESSAGE);
	}
	
	public void ChangeSelectionMode(GSelectMode mode)
	{
		selectionMode = mode;
		switch (mode) {
		case Arr:
		case Dep:
		case ArrDep:
		case Route:
			NodeSelected(selectedNodes[0]);
			break;
		case All:
		case Def:
			selectionMode = GSelectMode.ArrDep;
			NodeSelected(null);
			break;
		default:
			break;
		}
		prevSelecMode = selectionMode;
	}
	

	
	public void NodeSelected(Node node)
	{
		if(prevSelecMode != selectionMode)
		{
			ResetSelectedNodes();
			if(prevSelecMode == GSelectMode.Route)
			{
				GraphController.instance.ChangeNodesStatus(NodeStatus.Default, graph.getNodeSet());
				ShowAllNodes();
				HideAllEdges();
			}
		}
		
			
		
		if(node == null)
		{
			if(selectionMode != GSelectMode.Route)
				ResetSelectedNodes();
			return;
		}

		
		if(		selectionMode == GSelectMode.Arr ||
				selectionMode == GSelectMode.ArrDep ||
				selectionMode == GSelectMode.Dep)
		{
			ResetSelectedNodes();
			selectedNodes[0] = node;
			ChangeNodeStatus(NodeStatus.Selected, selectedNodes[0]);
		}
		else if(selectionMode == GSelectMode.Route)
		{
			
		}
			
		
		switch (selectionMode) {
		case Arr:
			ActivateEnteringEdgesFromNode(selectedNodes[0]);
			break;
		case Dep:
			ActivateLeavingEdgesFromNode(selectedNodes[0]);
			break;
		case ArrDep:
			ActivateEnteringEdgesFromNode(selectedNodes[0]);
			ActivateLeavingEdgesFromNode(selectedNodes[0]);
			break;		
		case Route:
			do
			{
				if(selectedNodes[0] == null && selectedNodes[1] == null)
				{
					selectedNodes[0] = node;			
					ArrayList<Node> nodes = new GraphUtils().GetPosibleNodesFromNode(selectedNodes[0]);
					if(nodes == null)
					{
						ResetSelectedNodes();
						break;
					}
					GraphController.instance.HideAllNodes();
					GraphController.instance.ChangeNodesStatus(NodeStatus.Default, nodes);
					ChangeNodeStatus(NodeStatus.Selected, selectedNodes[0]);
					break;
				}
				else if(selectedNodes[1] == null)
				{
					selectedNodes[1] = node;
					ShowAllNodes();
					ChangeNodeStatus(NodeStatus.Selected, selectedNodes[0]);
					ChangeNodeStatus(NodeStatus.Selected, selectedNodes[1]);		
					GetShortestPath(selectedNodes[0], selectedNodes[1]);
					
					break;
				}
				else
				{
					ResetSelectedNodes();
					ShowAllNodes();
					HideAllEdges();
				}
			}while(true);
		default:
			break;
		}
	}
	
	private void ResetSelectedNodes()
	{
		for (Node n : selectedNodes) {
			if(n == null)
				continue;
			Iterable<Edge> leavingEdges = GetLeavingEdgesFromNode(n);
			Iterable<Edge> enteringEdges = GetEnteringEdgesFromNode(n);
			ChangeEdgesStatus(EdgeStatus.Default, leavingEdges);
			ChangeEdgesStatus(EdgeStatus.Default, enteringEdges);
			ChangeNodeStatus(NodeStatus.Default, n);
			for (Edge e : leavingEdges) {
				ChangeNodeStatus(NodeStatus.Default, e.getTargetNode());
			}
			for (Edge e : enteringEdges) {
				ChangeNodeStatus(NodeStatus.Default, e.getSourceNode());
			}	
		}
		selectedNodes = new Node[2];
	}
	
	

	public void GetShortestPath(Node nodeA, Node nodeB)
	{
		lastRouteNodes.clear();
		Dijkstra dijkstra  = new Dijkstra(Dijkstra.Element.EDGE, null, "lenght");
		
		dijkstra.init(graph);
		dijkstra.setSource(graph.getNode(nodeA.getId()));
		dijkstra.compute();
		
		for (Node node : dijkstra.getPathNodes(graph.getNode(nodeB.getId())))
		{
			ChangeNodeStatus(NodeStatus.Secondary, node);
			lastRouteNodes.add(node);
		}
		Collections.reverse(lastRouteNodes);
		for (Edge edge : dijkstra.getPathEdges(graph.getNode(nodeB.getId())))
			ChangeEdgeStatus(EdgeStatus.ShowRoute, edge);
	}
	
	
	
	
	
	
	
	public void ChangeNodesStatus(NodeStatus status, Iterable<? extends Node> iterable)
	{
		for (Node n : iterable) {
			ChangeNodeStatus(status, n);
		}
	}
	
	public void ChangeNodeStatus(NodeStatus status, Node node)
	{
		if(node == null)
			return;
		
		Node n = graph.getNode(node.getId());
		
		if(n == null)
			return;
		switch (status) {
		case Default:
			n.removeAttribute(GHIDE);
			n.removeAttribute(GCLASS);
			break;
		case Secondary:
		case Selected:
			n.removeAttribute(GHIDE);
			n.addAttribute(GCLASS, status.toString());
			break;
		case Hiden:
			n.removeAttribute(GCLASS);
			n.addAttribute(GHIDE);
			break;
		default:
			break;
		}
	}
	
	
	
	
	
	
	public void ChangeEdgesStatus(EdgeStatus status , Iterable<? extends Edge> iterable)
	{
		for (Edge e : iterable) {
			ChangeEdgeStatus(status, e);
		}
	}
	
	public void ChangeEdgeStatus(EdgeStatus status , Edge edge)
	{
		if(edge == null)
			return;
		
		Edge e = graph.getEdge(edge.getId());
		
		if(e == null)
			return;
				
		switch (status) {
		case Default:
			e.addAttribute(GHIDE);
			e.removeAttribute(GCLASS);
			break;
		case Show:
		case ShowEntering:
		case ShowLeaving:
		case ShowRoute:
			e.removeAttribute(GHIDE);
			e.addAttribute(GCLASS, status.toString());
			break;
		default:
			break;
		}
	}
	

	

	
	public void ActivateLeavingEdgesFromNode(Node node)
	{	
		lastEdgeTargetNodes.clear();
		for (Edge edge : GetLeavingEdgesFromNode(node)) 
		{			
			if(node != edge.getSourceNode())
				continue;		
			ChangeEdgeStatus(EdgeStatus.ShowLeaving, edge);
			ChangeNodeStatus(NodeStatus.Secondary, edge.getTargetNode());
			lastEdgeTargetNodes.add(edge.getTargetNode());
		}	
	}
	
	public void ActivateEnteringEdgesFromNode(Node node)
	{
		lastEdgeSourceNodes.clear();
		for (Edge edge : GetEnteringEdgesFromNode(node)) 
		{						
			if(node != edge.getTargetNode())
				continue;		
			ChangeEdgeStatus(EdgeStatus.ShowEntering, edge);
			ChangeNodeStatus(NodeStatus.Secondary, edge.getSourceNode());
			lastEdgeSourceNodes.add(edge.getSourceNode());
		}	
	}
	
	
	
	public static Iterable<Edge> GetLeavingEdgesFromNode(Node node)
	{
		try {
			Iterable<Edge> edges =  node.getEachLeavingEdge();
			return edges;
		} catch (Exception e) {
			return new ArrayList<Edge>();
		}

	}
	
	public static Iterable<Edge> GetEnteringEdgesFromNode(Node node)
	{
		try {
			Iterable<Edge> edges =  node.getEachEnteringEdge();
			return edges;
		} catch (Exception e) {
			return new ArrayList<Edge>();
		}

	}
	

	
}
