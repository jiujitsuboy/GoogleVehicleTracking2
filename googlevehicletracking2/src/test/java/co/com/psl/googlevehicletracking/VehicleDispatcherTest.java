package co.com.psl.googlevehicletracking;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;
import co.com.psl.googlevehicletracking.classes.VehicleItineraryStatus;
import co.com.psl.googlevehicletracking.exception.GoogleVehicleTrackingException;
import co.com.psl.googlevehicletracking.interfaces.IVehicleDispatcher;
import co.com.psl.googlevehicletracking.utilities.FileUtilities;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext.xml")
public class VehicleDispatcherTest {

	@Autowired
	public ApplicationContext applicationContext;

	/**
	 * Exception variable to catch specific test exceptions
	 */
	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Test
	public void testExecuteVehiclesTravelingNoItineraryFile() throws GoogleVehicleTrackingException {

		final int NUMBER_OF_ITINERARIES_EXPECTED = 0;

		IVehicleDispatcher vehicleDispatcher = (IVehicleDispatcher) applicationContext.getBean("IVehicleDispatcher");
		VehicleItineraryStatus[] vehicleItineraryStatus = vehicleDispatcher.executeVehiclesTraveling();

		assertEquals(NUMBER_OF_ITINERARIES_EXPECTED, vehicleItineraryStatus.length);
	}

	@Test
	public void testExecuteVehiclesTravelingOneVehicles() throws GoogleVehicleTrackingException {

		final int NUMBER_OF_ITINERARIES_EXPECTED = 1;
		final String INPUT_FILE = "in20.txt";

		try {
			FileUtilities.createFile(INPUT_FILE,
					Arrays.asList("4785848123, JUAN MACHADO ,juanMachado@psl.com.co,FFFFLFFR",
							"4325898712, MARIA DELGADO, RRFLFR", "7895871234,PEDRO HEREDIA,pedro@google.com,FFLFFR"));

			IVehicleDispatcher vehicleDispatcher = (IVehicleDispatcher) applicationContext
					.getBean("IVehicleDispatcher");
			VehicleItineraryStatus[] vehicleItineraryStatus = vehicleDispatcher.executeVehiclesTraveling();

			assertEquals(NUMBER_OF_ITINERARIES_EXPECTED, vehicleItineraryStatus.length);
			assertTrue(vehicleItineraryStatus[0].isItineraryExecutingSuccesful());
		} finally {
			FileUtilities.deleteFile(INPUT_FILE);
		}

	}

	@Test
	public void testExecuteVehiclesTravelingTwentyOneVehicles() throws GoogleVehicleTrackingException {

		final int NUMBER_OF_ITINERARIES_FILES = 21;
		final int NUMBER_OF_ITINERARIES_FILES_EXPECTED = 20;

		try {

			for (int vehicleItinerary = 1; vehicleItinerary <= NUMBER_OF_ITINERARIES_FILES; vehicleItinerary++) {

				FileUtilities.createFile(String.format("in%d.txt", vehicleItinerary),
						Arrays.asList(
								String.format("4785848123, JUAN MACHADO%d,juanMachado%d@psl.com.co,FFFFLFFR",
										vehicleItinerary, vehicleItinerary),
								String.format("4325898712, MARIA DELGADO%d, RRFLFR", vehicleItinerary),
								String.format("7895871234,PEDRO HEREDIA%d,pedro%d@google.com,FFLFFR", vehicleItinerary,
										vehicleItinerary)));
			}

			IVehicleDispatcher vehicleDispatcher = (IVehicleDispatcher) applicationContext
					.getBean("IVehicleDispatcher");
			VehicleItineraryStatus[] vehicleItineraryStatus = vehicleDispatcher.executeVehiclesTraveling();

			assertEquals(NUMBER_OF_ITINERARIES_FILES_EXPECTED, vehicleItineraryStatus.length);

			for (int vehicle = 0; vehicle < vehicleItineraryStatus.length; vehicle++) {
				assertTrue(vehicleItineraryStatus[vehicle].isItineraryExecutingSuccesful());
			}

		} finally {
			for (int vehicleItinerary = 1; vehicleItinerary <= NUMBER_OF_ITINERARIES_FILES; vehicleItinerary++) {
				FileUtilities.deleteFile(String.format("in%d.txt", vehicleItinerary));
				FileUtilities.deleteFile(String.format("out%d.txt", vehicleItinerary));
			}

		}

	}

	@Test
	public void testExecuteVehiclesTravelingFourVehicles() throws GoogleVehicleTrackingException {

		final int NUMBER_OF_ITINERARIES_EXPECTED = 4;

		try {

			for (int vehicleItinerary = 1; vehicleItinerary <= NUMBER_OF_ITINERARIES_EXPECTED; vehicleItinerary++) {

				FileUtilities.createFile(String.format("in%d.txt", vehicleItinerary),
						Arrays.asList(
								String.format("4785848123, JUAN MACHADO%d,juanMachado%d@psl.com.co,FFFFLFFR",
										vehicleItinerary, vehicleItinerary),
								String.format("4325898712, MARIA DELGADO%d, RRFLFR", vehicleItinerary),
								String.format("7895871234,PEDRO HEREDIA%d,pedro%d@google.com,FFLFFR", vehicleItinerary,
										vehicleItinerary)));
			}

			IVehicleDispatcher vehicleDispatcher = (IVehicleDispatcher) applicationContext
					.getBean("IVehicleDispatcher");
			VehicleItineraryStatus[] vehicleItineraryStatus = vehicleDispatcher.executeVehiclesTraveling();

			assertEquals(NUMBER_OF_ITINERARIES_EXPECTED, vehicleItineraryStatus.length);

			for (int vehicle = 0; vehicle < vehicleItineraryStatus.length; vehicle++) {
				assertTrue(vehicleItineraryStatus[vehicle].isItineraryExecutingSuccesful());
			}

		} finally {

			for (int vehicleItinerary = 1; vehicleItinerary <= NUMBER_OF_ITINERARIES_EXPECTED; vehicleItinerary++) {
				FileUtilities.deleteFile(String.format("in%d.txt", vehicleItinerary));
				FileUtilities.deleteFile(String.format("out%d.txt", vehicleItinerary));
			}

		}

	}

	@Test
	public void onlyOneInstanceOfVehicleDispatcher() {

		IVehicleDispatcher vehicleDispatcher1 = (IVehicleDispatcher) applicationContext.getBean("IVehicleDispatcher");
		IVehicleDispatcher vehicleDispatcher2 = (IVehicleDispatcher) applicationContext.getBean("IVehicleDispatcher");

		assertEquals(vehicleDispatcher1, vehicleDispatcher2);
	}

}
