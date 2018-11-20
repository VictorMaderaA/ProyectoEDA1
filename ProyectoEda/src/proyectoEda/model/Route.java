package proyectoEda.model;

import java.util.ArrayList;
import java.util.List;

import proyectoEda.utils.CsvUtils;
import proyectoEda.utils.FileReader;

public class Route {

	private String airline;
	private int airlineId;
	private String sourceRoute;
	private int sourceRouteId;
	private String destinationRoute;
	private int destinationRouteId;
	private String codeshare;
	private int stops;
	private String equipment;
	
	
	public String getAirline() {
		return airline;
	}

	public int getAirlineId() {
		return airlineId;
	}

	public String getSourceRoute() {
		return sourceRoute;
	}

	public int getSourceRouteId() {
		return sourceRouteId;
	}

	public String getDestinationRoute() {
		return destinationRoute;
	}

	public int getDestinationRouteId() {
		return destinationRouteId;
	}


	public String getEquipment() {
		return equipment;
	}

	
	
	
	public void PrintInfo()
	{
		System.out.println(airline + " - " + airlineId + " - " + sourceRoute + " - " + sourceRouteId + " - " 
	+ destinationRoute + " - " + destinationRouteId + " - " + codeshare + " - "+ stops + " - "+ equipment);
	}
	
	public void PrintInfo2()
	{
		System.out.println(sourceRouteId + " - " + destinationRouteId );
	}
	
	
	
	public static Route GetRouteFromCsvLine(String lineInput)
	{
		List<String> line = CsvUtils.parseLine(lineInput);
		Route outRoute = new Route();
		
		int airlineId = -1;
		try {
			airlineId = Integer.parseInt(line.get(1));
		} catch (Exception e) {}
		
		int sourceRouteId = -1;
		try {
			sourceRouteId = Integer.parseInt(line.get(3));
		} catch (Exception e) {}
		
		int destinationRouteId = -1;
		try {
			destinationRouteId = Integer.parseInt(line.get(5));
		} catch (Exception e) {}
		
		int stops = -1;
		try {
			stops = Integer.parseInt(line.get(7));
		} catch (Exception e) {}
		
		outRoute.airline = line.get(0);
		outRoute.airlineId = airlineId;
		outRoute.sourceRoute = line.get(2);
		outRoute.sourceRouteId = sourceRouteId;
		outRoute.destinationRoute = line.get(4);
		outRoute.destinationRouteId = destinationRouteId;
		outRoute.codeshare = line.get(6);
		outRoute.stops = stops;
		outRoute.equipment = line.get(8);

		
		return outRoute;
	}
	
	public static List<Route> GetListRoutesFromCsv(String csvPath)
	{
		ArrayList<String> csvLines = FileReader.ReadFile(csvPath);
		ArrayList<Route> Routes = new ArrayList<Route>();
		
		for (String line : csvLines) {
			Route newRoute= GetRouteFromCsvLine(line);
			Routes.add(newRoute);
		}
		
		return Routes;
	}
	
}
