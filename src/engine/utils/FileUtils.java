package engine.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class FileUtils {
	private FileUtils() {
		// This is to make suire no one creates a new instance of this class.
	}
	
	public static String loadAsString(String path) {
		/**
		 * Loads a file with (path) as a string in memory.
		 * @param path - The path of the file you want to read
		 * */
		StringBuilder str = new StringBuilder(); // Create a string builder to append strings to
		try {
			File file = new File(path); // Get the file
			FileReader rFile  = new FileReader(file); //  
			BufferedReader rBuffer = new BufferedReader(rFile); // Method to read buffers
			
			String nLine;
			while((nLine = rBuffer.readLine()) != null) { 
				// if there is a new line that's readable then read it. (It's set up this way as the reader moves onto the next line each time it's called
				
				str.append(nLine).append("\n"); // append the new line and a new line break.
			}
			
			rFile.close(); // Close the file reader.
			
		} catch(IOException e) {
			System.err.println("Couldn't find the file at: " + path);
		}

		return str.toString();
		
		
	}
}
