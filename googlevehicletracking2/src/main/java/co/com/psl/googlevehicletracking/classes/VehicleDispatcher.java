package co.com.psl.googlevehicletracking.classes;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import co.com.psl.googlevehicletracking.exception.GoogleVehicleTrackingException;
import co.com.psl.googlevehicletracking.interfaces.IVehicleDispatcher;

public class VehicleDispatcher implements IVehicleDispatcher {

	private static VehicleDispatcher vehicleDispatcher;

	/**
	 * Private contructor
	 */
	private VehicleDispatcher() {
	}

	/**
	 * Create an return a singleton VechicleDispatcher
	 * 
	 * @return VehicleDispatcher
	 */
	public static VehicleDispatcher getVehicleDispatcher() {
		if (vehicleDispatcher == null) {
			vehicleDispatcher = new VehicleDispatcher();
		}
		return vehicleDispatcher;
	}

	@Override
	public VehicleItineraryStatus[] executeVehiclesTraveling() throws GoogleVehicleTrackingException {

		List<VehicleItineraryStatus> vehiclesItineraryStatus = new ArrayList<>();		
		List<Future<VehicleItineraryStatus>> vehiclesItinerary = new ArrayList<>();

		final int AWAIT_SECONDS_TO_VEHICLE_COMPLETITION = 10000;
		ExecutorService vehicleDispatcherOperator;
		
		// Find out how many itinerary files are in the classpath
		Integer[] vehicleItineraryFiles = obtainItineraryNumber();

		if (vehicleItineraryFiles.length > 0) {
			vehicleDispatcherOperator = Executors.newFixedThreadPool(20);

			for (int fileItinerary = 0; fileItinerary < vehicleItineraryFiles.length; fileItinerary++) {
				VehicleItinerary vehicleItinerary = new VehicleItinerary(vehicleItineraryFiles[fileItinerary]);
				vehiclesItinerary.add(vehicleDispatcherOperator.submit(vehicleItinerary));
			}

			vehicleDispatcherOperator.shutdown();

			try {
				for (Future<VehicleItineraryStatus> vehicleItineraryStatus : vehiclesItinerary) {
					vehiclesItineraryStatus.add(vehicleItineraryStatus.get());
				}

				vehicleDispatcherOperator.awaitTermination(AWAIT_SECONDS_TO_VEHICLE_COMPLETITION, TimeUnit.SECONDS);
			} catch (InterruptedException | ExecutionException e) {
				throw new GoogleVehicleTrackingException("Exception in wait for itinerary results", e);
			}

		}

		return vehiclesItineraryStatus.toArray(new VehicleItineraryStatus[vehiclesItineraryStatus.size()]);
	}

	/**
	 * Verify the itineraries available in the program path
	 * 
	 * @return int[]
	 */
	private Integer[] obtainItineraryNumber() {
		final int MAX_NUMBER_OF_ITINERARIES = 20;
		List<Integer> filesNumber = new ArrayList<>();

		for (int fileNum = 1; fileNum <= MAX_NUMBER_OF_ITINERARIES; fileNum++) {
			String path = String.format("in%d.txt", fileNum);
			if (Files.exists(Paths.get(path))) {
				filesNumber.add(fileNum);
			}
		}

		return filesNumber.toArray(new Integer[filesNumber.size()]);
	}
}
