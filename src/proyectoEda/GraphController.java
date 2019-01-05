package proyectoEda;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.MultiGraph;

import Enums.EdgeStatus;
import Enums.NodeStatus;
import proyectoEda.model.Airport;
import proyectoEda.model.Route;
import proyectoEda.utils.GraphUtils;

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
		
	
	public static enum GSelectMode
	{
		Arr,
		Dep,
		ArrDep,
		All,
		Def,
		Route
	}
	
	
	final String filePathAirplanes = "src/files/airports.txt";
	final String filePathRoutes = "src/files/routes.txt";
	
	private final String  GHIDE= "ui.hide";
	private final String  GCLASS= "ui.class";
	
	public Graph graph = new MultiGraph("Graph");		
	
	
	private void GenerateStartingGraph()
	{
		List<Airport> airports = Airport.GetListAirportsFromCsv(filePathAirplanes);
		List<Route> routes = Route.GetListRoutesFromCsv(filePathRoutes);		
		
		AddAirportsToGraph(airports);
		AddRoutesToGraph(routes);
		RemoveNodesWithoutEdges();
	}
	
	private void AddAirportsToGraph(Iterable<Airport> airports)
	{
		for (Airport a : airports) {
			
			if(Integer.parseInt(a.getId()) < 0)
				continue;
			
			graph.addNode(a.getId()).addAttribute("xyz", a.getLongitud(), a.getLatitud(), a.getAltitud());
		}
	}
	
	private void AddRoutesToGraph(Iterable<Route> routes)
	{
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
	
	
	
	
	
	public void ChangeSelectionMode(GSelectMode mode)
	{
		selectionMode = mode;
		switch (mode) {
		case Arr:
			NodeSelected(selectedNodes[0]);
			break;
		case Dep:
			NodeSelected(selectedNodes[0]);
			break;
		case ArrDep:
			NodeSelected(selectedNodes[0]);
			break;
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
	
	private Node[] selectedNodes = new Node[2];
	private GSelectMode selectionMode = GSelectMode.ArrDep;
	private GSelectMode prevSelecMode = null;
	
	public void NodeSelected(Node node)
	{
		if(prevSelecMode != selectionMode)
		{
			ResetSelectedNodes();
			if(prevSelecMode == GSelectMode.Route)
			{
				GraphController.instance.ChangeNodesStatus(NodeStatus.Default, graph.getNodeSet());
				ShowAllNodes();
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
					GetConectedNodesFromNode(node);
					ChangeNodeStatus(NodeStatus.Selected, selectedNodes[0]);
					break;
				}
				else if(selectedNodes[1] == null)
				{
					selectedNodes[1] = node;
					ShowAllNodes();
					ChangeNodeStatus(NodeStatus.Selected, selectedNodes[0]);
					ChangeNodeStatus(NodeStatus.Selected, selectedNodes[1]);		
					//TODO mostrar Ruta
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
			n.removeAttribute(GHIDE);
			n.addAttribute(GCLASS, NodeStatus.Secondary.toString());
			break;
		case Selected:
			n.removeAttribute(GHIDE);
			n.addAttribute(GCLASS, NodeStatus.Selected.toString());
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
			e.removeAttribute(GHIDE);
			e.addAttribute(GCLASS, EdgeStatus.Show.toString());
			break;
		case ShowEntering:
			e.removeAttribute(GHIDE);
			e.addAttribute(GCLASS, EdgeStatus.ShowEntering.toString());
			break;
		case ShowLeaving:
			e.removeAttribute(GHIDE);
			e.addAttribute(GCLASS, EdgeStatus.ShowLeaving.toString());
			break;
		default:
			break;
		}
	}
	
	
	
	
	
	
	
	
	
	
	

	
	
	
	
	public void ActivateLeavingEdgesFromNode(Node node)
	{	
		for (Edge edge : GetLeavingEdgesFromNode(node)) 
		{			
			if(node != edge.getSourceNode())
				continue;		
			ChangeEdgeStatus(EdgeStatus.ShowLeaving, edge);
			ChangeNodeStatus(NodeStatus.Secondary, edge.getTargetNode());
		}	
	}
	
	public void ActivateEnteringEdgesFromNode(Node node)
	{
		for (Edge edge : GetEnteringEdgesFromNode(node)) 
		{						
			if(node != edge.getTargetNode())
				continue;		
			ChangeEdgeStatus(EdgeStatus.ShowEntering, edge);
			ChangeNodeStatus(NodeStatus.Secondary, edge.getSourceNode());
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
	
	public static ArrayList<Node> GetConectedNodesFromNode(Node node)
	{
		ArrayList<Node> nodes = new ArrayList<Node>();
		nodes = new GraphUtils().GetPosibleNodesFromNode(node);	
		GraphController.instance.HideAllNodes();
		GraphController.instance.ChangeNodesStatus(NodeStatus.Default, nodes);
		return nodes;
	}
	
	
}
