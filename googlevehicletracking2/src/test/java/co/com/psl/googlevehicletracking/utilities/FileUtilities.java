package co.com.psl.googlevehicletracking.utilities;

import static org.junit.Assert.fail;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class FileUtilities {

	/**
	 * Helper class which creates a file with the specify name and lines of
	 * content
	 * 
	 * @param fileName
	 * @param lstLine
	 */
	public static void createFile(String fileName, List<String> lstLine) {
		Path file = Paths.get(fileName);
		try {
			Files.write(file, lstLine, Charset.forName("UTF-8"));
		} catch (IOException e) {
			fail(e.getMessage());
		}
	}

	/**
	 * Helper class which delete the specify file
	 * 
	 * @param fileName
	 */
	public static void deleteFile(String fileName) {
		Path file = Paths.get(fileName);
		try {
			Files.deleteIfExists(file);
		} catch (IOException e) {
			fail(e.getMessage());
		}
	}
}
