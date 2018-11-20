package proyectoEda;

import java.util.ArrayList;
import java.util.List;

import org.graphstream.graph.*;
import org.graphstream.graph.implementations.*;
import org.graphstream.stream.file.FileSourceGEXF.GEXFConstants.GRAPHAttribute;

import proyectoEda.model.Airport;
import proyectoEda.model.Route;
import proyectoEda.utils.FileReader;

public class Main {

	public static void main(String[] args) {
		
		System.setProperty("gs.ui.renderer", "org.graphstream.ui.j2dviewer.J2DGraphRenderer");
		
		String filePathAirplanes = "src/files/airports.txt";
		String filePathRoutes = "src/files/routes.txt";
	
		List<Airport> airports = Airport.GetListAirportsFromCsv(filePathAirplanes);
		List<Route> routes = Route.GetListRoutesFromCsv(filePathRoutes);
				
		Graph graph = new SingleGraph("Tutorial 1");
		
		graph.setStrict(true);
		graph.setAutoCreate( true );
		
		for (Airport a : airports) {
			if(a.getId() < 0)
				continue;
			graph.addNode(Integer.toString(a.getId()));
			Node n = graph.getNode(graph.getNodeCount()-1);
			n.addAttribute("xyz", a.getLongitud(), a.getLatitud(), a.getAltitud());
			
		}
		
		for (Route r : routes) {
			if(r.getSourceRouteId() < 0 || r.getDestinationRouteId() < 0)
				continue;
			
			String sourceId = Integer.toString(r.getSourceRouteId());
			String destinationId = Integer.toString(r.getDestinationRouteId());			
			String edgeId = sourceId + destinationId;
			
			try {
				graph.addEdge(edgeId, sourceId, destinationId);
			} catch (Exception e) {
				// TODO: handle exception
			}
			
		}
		
		graph.display(false);
		
		
	}

}
