package co.com.psl.googlevehicletracking;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;


import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import co.com.psl.googlevehicletracking.classes.Passenger;
import co.com.psl.googlevehicletracking.classes.Route;
import co.com.psl.googlevehicletracking.classes.SpatialVehicleLocation;
import co.com.psl.googlevehicletracking.enums.VehicleMovement;
import co.com.psl.googlevehicletracking.interfaces.IReadWriteFile;
import co.com.psl.googlevehicletracking.utilities.FileUtilities;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext.xml")
public class ReadWriteTest {

	@Autowired
	public ApplicationContext applicationContext;
	IReadWriteFile readWriteFile;
	public final String INPUT_FILE = "in1.txt";
	public final String OUTPUT_FILE = "out1.txt";

	/**
	 * Exception variable to catch specific test exceptions
	 */
	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Before
	public void injectIReadWriteFile() {
		readWriteFile = (IReadWriteFile) applicationContext.getBean("IReadWriteFile");
	}

	@Test
	public final void testReadThreeLines() throws FileNotFoundException, IOException {
		int NumberofLinesRead = 0;
		final int EXPECTED_NUMBER_OF_LINES_READ = 3;

		FileUtilities.createFile(INPUT_FILE, Arrays.asList("4785848123, JUAN MACHADO,juanMachado@psl.com.co,FFFFLFFR",
				"4325898712, MARIA DELGADO, RRFLFR", "7895871234,PEDRO HEREDIA,pedro@google.com,FFLFFFR"));

		try {
			NumberofLinesRead = readWriteFile.readInputFile(INPUT_FILE).length;
			assertEquals(EXPECTED_NUMBER_OF_LINES_READ, NumberofLinesRead);
		} finally {
			FileUtilities.deleteFile(INPUT_FILE);
		}

	}

	@Test
	public final void testReadEmptyFile() throws FileNotFoundException, IOException {

		final int EXPECTED_NUMBER_OF_LINES_READ = 0;

		FileUtilities.createFile(INPUT_FILE, Arrays.asList(""));

		try {
			Route[] routes = readWriteFile.readInputFile(INPUT_FILE);
			assertEquals(EXPECTED_NUMBER_OF_LINES_READ, routes.length);
		} finally {
			FileUtilities.deleteFile(INPUT_FILE);
		}

	}

	@Test
	public final void testReadNonExistingFile() throws FileNotFoundException, IOException {

		FileUtilities.deleteFile(INPUT_FILE);
		thrown.expect(FileNotFoundException.class);
		thrown.expectMessage(String.format("File not found [%s]", INPUT_FILE));

		readWriteFile.readInputFile(INPUT_FILE);

	}

	@Test
	public final void testReadBadFormatPassengerID() throws FileNotFoundException, IOException {

		FileUtilities.createFile(INPUT_FILE, Arrays.asList("4785848123, JUAN MACHADO,juanMachado@psl.com.co,FFFFLFFR",
				"4.3258.987, MARIA DELGADO, RRFLFR", "7895871234,PEDRO HEREDIA,pedro@google.com,FFLFDFR"));

		thrown.expect(NumberFormatException.class);
		thrown.expectMessage("passenger ID [4.3258.987] must contain only numbers");

		try {
			readWriteFile.readInputFile(INPUT_FILE);
		} finally {
			FileUtilities.deleteFile(INPUT_FILE);
		}

	}

	@Test
	public final void testReadTooShortPassengerID() throws FileNotFoundException, IOException {

		FileUtilities.createFile(INPUT_FILE, Arrays.asList("4785848, JUAN MACHADO,juanMachado@psl.com.co,FFFFLFFR",
				"4325898712, MARIA DELGADO, RRFLFR", "789587,PEDRO HEREDIA,pedro@google.com,FFLFDFR"));

		thrown.expect(IllegalArgumentException.class);
		thrown.expectMessage("Passenger ID must be between " + Passenger.ID_MIN_LENGHT + " and "
				+ Passenger.ID_MAX_LENGHT + " digits");

		try {
			readWriteFile.readInputFile(INPUT_FILE);
		} finally {
			FileUtilities.deleteFile(INPUT_FILE);
		}

	}

	@Test
	public final void testReadTooLongPassengerID() throws FileNotFoundException, IOException {

		FileUtilities.createFile(INPUT_FILE,
				Arrays.asList(
						"478584812347858481234785848123478584812347858481234, JUAN MACHADO  ,juanMachado@psl.com.co,FFFFLFFR",
						"43258987, MARIA DELGADO, RRFLFR", "789587,PEDRO HEREDIA,pedro@google.com,FFLFDFR"));

		thrown.expect(IllegalArgumentException.class);
		thrown.expectMessage("Passenger ID must be between " + Passenger.ID_MIN_LENGHT + " and "
				+ Passenger.ID_MAX_LENGHT + " digits");

		try {
			readWriteFile.readInputFile(INPUT_FILE);
		} finally {
			FileUtilities.deleteFile(INPUT_FILE);
		}

	}

	@Test
	public final void testReadTooLongPassengerName() throws FileNotFoundException, IOException {

		FileUtilities.createFile(INPUT_FILE,
				Arrays.asList(
						"4785848123, JUAN MACHADO RRRRRRRRRR RRRRRRRRRR RRRRRRRRRR RRRRRRRRRR RRRRRRRRRR RRRRRRRRRR RRRRRRRRRR RRRRRRRRRR RRRRRRRRRR RRRRRRRRRR RRRRRRRRRR RRRRRRRRRR RRRRRRRRRR RRRRRRRRRR RRRRRRRRRR RRRRRRRRRR RRRRRRRRRR RRRRRRRRRR RRRRRRRRRR RRRRRRRRRR ,juanMachado@psl.com.co,FFFFLFFR",
						"4325898712, MARIA DELGADO, RRFLFR", "789587,PEDRO HEREDIA,pedro@google.com,FFLFFFR"));

		thrown.expect(IllegalArgumentException.class);
		thrown.expectMessage("Passenger name lenght must be max " + Passenger.NAME_MAX_LENGHT + " characters");

		try {
			readWriteFile.readInputFile(INPUT_FILE);
		} finally {
			FileUtilities.deleteFile(INPUT_FILE);
		}

	}

	@Test
	public final void testOptionalPassengerEmail() throws FileNotFoundException, IOException {

		final int EXPECTED_NUMBER_OF_LINES_READ = 2;

		FileUtilities.createFile(INPUT_FILE, Arrays.asList("4785848123, JUAN MACHADO ,juanMachado@psl.com.co,FFFFLFFR",
				"4325898712, MARIA DELGADO, RRFLFR"));

		try {
			Route[] routes = readWriteFile.readInputFile(INPUT_FILE);

			assertEquals(EXPECTED_NUMBER_OF_LINES_READ, routes.length);
			assertEquals(4785848123L, routes[0].getPassenger().getID());
			assertEquals("JUAN MACHADO", routes[0].getPassenger().getName());
			assertEquals("juanMachado@psl.com.co", routes[0].getPassenger().getEmail());
			assertEquals(4325898712L, routes[1].getPassenger().getID());
			assertEquals("MARIA DELGADO", routes[1].getPassenger().getName());
			assertNull(routes[1].getPassenger().getEmail());
		} finally {
			FileUtilities.deleteFile(INPUT_FILE);
		}

	}

	@Test
	public final void testInvalidPassengerEmail() throws FileNotFoundException, IOException {

		FileUtilities.createFile(INPUT_FILE, Arrays.asList("4785848123, JUAN MACHADO ,juanMachadoApsl.com.co,FFFFLFFR",
				"4325898712, MARIA DELGADO, RRFLFR", "789587,PEDRO HEREDIA,pedro@google.com,FFLFDFR"));

		thrown.expect(IllegalArgumentException.class);
		thrown.expectMessage("Invalid email address [juanMachadoApsl.com.co]");

		try {
			readWriteFile.readInputFile(INPUT_FILE);
		} finally {
			FileUtilities.deleteFile(INPUT_FILE);
		}

	}

	@Test
	public final void testRouteInvalidMovementToken() throws FileNotFoundException, IOException {

		FileUtilities.createFile(INPUT_FILE, Arrays.asList("4785848123, JUAN MACHADO ,juanMachado@psl.com.co,FFFFLFFR",
				"4325898712, MARIA DELGADO, RRFLFR", "7895871234,PEDRO HEREDIA,pedro@google.com,FFFFFY"));

		thrown.expect(IllegalArgumentException.class);
		thrown.expectMessage("Invalid vehicle movement [Y]");

		try {
			readWriteFile.readInputFile(INPUT_FILE);
		} finally {
			FileUtilities.deleteFile(INPUT_FILE);
		}

	}

	@Test
	public final void testRouteEmptyTokens() throws FileNotFoundException, IOException {

		FileUtilities.createFile(INPUT_FILE, Arrays.asList("4785848123, JUAN MACHADO ,juanMachado@psl.com.co, ",
				"4325898712, MARIA DELGADO, RRFLFR", "789587,PEDRO HEREDIA,pedro@google.com,FFLFFFR"));

		thrown.expect(IllegalArgumentException.class);
		thrown.expectMessage("White spaces are invalid tokens for a route");

		try {
			readWriteFile.readInputFile(INPUT_FILE);
		} finally {
			FileUtilities.deleteFile(INPUT_FILE);
		}

	}

	@Test
	public final void testRouteOnlyTwoTokens() throws FileNotFoundException, IOException {

		FileUtilities.createFile(INPUT_FILE, Arrays.asList("4785848, JUAN MACHADO", "43258987, MARIA DELGADO, RRFLFR",
				"789587,PEDRO HEREDIA,pedro@google.com,FFLFDFR"));

		thrown.expect(IllegalArgumentException.class);
		thrown.expectMessage("The number of tokens for the route [4785848, JUAN MACHADO] must be between 3 and 4");

		try {
			readWriteFile.readInputFile(INPUT_FILE);
		} finally {
			FileUtilities.deleteFile(INPUT_FILE);
		}

	}

	@Test
	public final void testRouteFiveTokens() throws FileNotFoundException, IOException {

		FileUtilities.createFile(INPUT_FILE, Arrays.asList("4785848123, JUAN MACHADO ,juanMachado@psl.com.co,FFFFLFFR",
				"4325898712, MARIA DELGADO, RRFLFR", "789587,PEDRO HEREDIA,pedro@google.com,FFLFFR,FFFFF"));

		thrown.expect(IllegalArgumentException.class);
		thrown.expectMessage(
				"The number of tokens for the route [789587,PEDRO HEREDIA,pedro@google.com,FFLFFR,FFFFF] must be between 3 and 4");

		try {
			readWriteFile.readInputFile(INPUT_FILE);
		} finally {
			FileUtilities.deleteFile(INPUT_FILE);
		}

	}

	@Test
	public final void testWriteOutputFile() throws IOException {

		Route route1 = new Route(new Passenger(79949875, "Jose Alejandro Nino Mora"),
				new VehicleMovement[] { VehicleMovement.FORWARD, VehicleMovement.FORWARD, VehicleMovement.RIGHT,
						VehicleMovement.FORWARD, VehicleMovement.FORWARD, VehicleMovement.LEFT,
						VehicleMovement.FORWARD });
		route1.setSpatialVehicleLocation(new SpatialVehicleLocation());

		Route route2 = new Route(new Passenger(52705168, "Alejandra Mendez"),
				new VehicleMovement[] { VehicleMovement.RIGHT, VehicleMovement.FORWARD, VehicleMovement.RIGHT,
						VehicleMovement.FORWARD, VehicleMovement.FORWARD, VehicleMovement.LEFT,
						VehicleMovement.FORWARD });
		route2.setSpatialVehicleLocation(new SpatialVehicleLocation());

		Route route3 = new Route(new Passenger(899765234, "Wanda Maria"),
				new VehicleMovement[] { VehicleMovement.FORWARD, VehicleMovement.FORWARD, VehicleMovement.LEFT,
						VehicleMovement.FORWARD, VehicleMovement.FORWARD, VehicleMovement.RIGHT,
						VehicleMovement.FORWARD });
		route3.setSpatialVehicleLocation(new SpatialVehicleLocation());

		Route route4 = new Route(new Passenger(1234567654, "Brandy Liliana"),
				new VehicleMovement[] { VehicleMovement.FORWARD, VehicleMovement.FORWARD, VehicleMovement.LEFT,
						VehicleMovement.FORWARD, VehicleMovement.FORWARD, VehicleMovement.RIGHT,
						VehicleMovement.FORWARD });
		route4.setSpatialVehicleLocation(new SpatialVehicleLocation());

		Route[] routes = new Route[] { route1, route2, route3, route4 };

		readWriteFile.writeOutputFile(OUTPUT_FILE, routes);

	}
}
