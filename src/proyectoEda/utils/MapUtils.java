package proyectoEda.utils;

public class MapUtils {
	
	public static double distanceToCordinates(Object lat1, Object lat2, Object lon1,
			Object lon2, Object el1, Object el2)
	{
		double[] v = new double[6];
		v[0] = Double.parseDouble(lat1.toString());
		v[1] = Double.parseDouble(lat2.toString());
		v[2] = Double.parseDouble(lon1.toString());
		v[3] = Double.parseDouble(lon2.toString());
		v[4] = Double.parseDouble(el1.toString());
		v[5] = Double.parseDouble(el2.toString());
		
		return distanceToCordinates(v[0],v[1],v[2],v[3],v[4],v[5]);
	}
	
	public static double distanceToCordinates(double lat1, double lat2, double lon1,
	        double lon2, double el1, double el2) {

	    final int R = 6371; // Radius of the earth

	    double latDistance = Math.toRadians(lat2 - lat1);
	    double lonDistance = Math.toRadians(lon2 - lon1);
	    double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
	            + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
	            * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
	    double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
	    double distance = R * c * 1000; // convert to meters

	    double height = el1 - el2;

	    distance = Math.pow(distance, 2) + Math.pow(height, 2);

	    return Math.round(Math.sqrt(distance));
	}
	
}
