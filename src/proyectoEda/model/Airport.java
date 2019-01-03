package proyectoEda.model;

import java.util.ArrayList;
import java.util.List;

import proyectoEda.utils.CsvUtils;
import proyectoEda.utils.FileReader;

public class Airport {

	private String id;
	private String name;
	private String city;
	private String country;
	private String iata;
	private String icao;
	private double latitud;
	private double longitud;
	private int altitud;
	private float timeZone;
	private String dst;
	private String tz;
	private String type;
	private String source;
	
	
	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getCity() {
		return city;
	}

	public String getCountry() {
		return country;
	}

	public double getLatitud() {
		return latitud;
	}
	
	public double getLongitud() {
		return longitud;
	}
	
	public int getAltitud() {
		return altitud;
	}
	
	

	public void PrintInfo()
	{
		System.out.println(id + " - " + name + " - " + city + " - " + country + " - " + iata + " - " + icao + " - " + latitud + " - "+ longitud +
				" - "+altitud + " - " + timeZone + " - " + dst + " - " + tz + " - " + type + " - " + source);
	}
	
	
	
	
	
	
	public static Airport GetAirportFromCsvLine(String lineInput)
	{
		List<String> line = CsvUtils.parseLine(lineInput);
		Airport outAirport = new Airport();
		
		Float timeZone = -0.11f;
		try {
			timeZone = Float.parseFloat(line.get(9));
		} catch (Exception e) {}
		
		//outAirport.id = Integer.parseInt(line.get(0));
		outAirport.id = line.get(0);
		outAirport.name = line.get(1);
		outAirport.city = line.get(2);
		outAirport.country = line.get(3);
		outAirport.iata = line.get(4);
		outAirport.icao = line.get(5);
		outAirport.latitud = Double.parseDouble(line.get(6));
		outAirport.longitud = Double.parseDouble(line.get(7));
		outAirport.altitud = Integer.parseInt(line.get(8));
		outAirport.timeZone = timeZone;
		outAirport.dst = line.get(10);
		outAirport.tz = line.get(11);
		outAirport.type = line.get(12);
		outAirport.source = line.get(13);	
		
		return outAirport;
	}
	
	public static List<Airport> GetListAirportsFromCsv(String csvPath)
	{
		ArrayList<String> csvLines = FileReader.ReadFile(csvPath);
		ArrayList<Airport> airports = new ArrayList<Airport>();
		
		for (String line : csvLines) {
			Airport newAirport= GetAirportFromCsvLine(line);
			airports.add(newAirport);
		}
		
		return airports;
	}
	
	
	
}
