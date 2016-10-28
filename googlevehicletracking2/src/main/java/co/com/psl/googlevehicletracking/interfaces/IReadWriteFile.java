package co.com.psl.googlevehicletracking.interfaces;

import java.io.FileNotFoundException;
import java.io.IOException;
import co.com.psl.googlevehicletracking.classes.Route;

/**
 * Define a read and write operation
 * @author Alejandro
 *
 */
public interface IReadWriteFile {

	/**
	 * Read the implementation configured file and return a Route for each valid line
	 * @param the path of the file to read from
	 * @return Route[]
	 * @throws FileNotFoundException
	 * @throws IOException
	 */	
	Route[] readInputFile(String filePath) throws FileNotFoundException, IOException;

	/**
	 * Write to the implementation configured file each Route log
	 * @param the path of the file to write to.
	 * @param routes
	 * @throws IOException
	 */
	void writeOutputFile(String filePath,Route[] routes) throws IOException;
}
