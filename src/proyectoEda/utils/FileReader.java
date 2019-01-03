package proyectoEda.utils;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class FileReader {

	public static ArrayList<String> ReadFile(String filePath)
	{
		String[] arr= null;
		ArrayList<String> fileStringList = new ArrayList<String>();

	    try 
	    { 
	        FileInputStream fstream_school = new FileInputStream(filePath); 
	        DataInputStream data_input = new DataInputStream(fstream_school); 
	        BufferedReader buffer = new BufferedReader(new InputStreamReader(data_input)); 
	        String str_line; 

	        while ((str_line = buffer.readLine()) != null) 
	        { 
	            str_line = str_line.trim(); 
	            if ((str_line.length()!=0))  
	            { 
	            	fileStringList.add(str_line);
	            } 
	        }

	        arr = (String[])fileStringList.toArray(new String[fileStringList.size()]);
	    }
	    catch(Exception e)
	    {
	    	System.err.println("Failed to ReadFile <" + filePath + ">");
	    }
		return fileStringList;
	}
	
}
