package co.com.psl.googlevehicletracking;

import co.com.psl.googlevehicletracking.classes.VehicleItineraryStatus;
import co.com.psl.googlevehicletracking.interfaces.IVehicleDispatcher;
import co.com.psl.googlevehicletracking.spring.SpringAppContextSingleton;

/**
 * Main class of the program
 *
 */
public class App {
	/**
	 * Initial point of execution
	 * @param args
	 */
	public static void main(String[] args) {

		programInstructions();
		SpringAppContextSingleton springAppContextSingleton = null;
		try {
			springAppContextSingleton = SpringAppContextSingleton.getSpringAppContextSingleton();
			IVehicleDispatcher vehicleDispatcher = (IVehicleDispatcher) springAppContextSingleton
					.getBean("IVehicleDispatcher");

			// Scheduling the trips
			VehicleItineraryStatus[] vehicleItineraryStatus = vehicleDispatcher.executeVehiclesTraveling();
			// Closing spring context
			springAppContextSingleton.closeApplicationContext();

			// Display trips status
			vehicleItinerarySummary(vehicleItineraryStatus);

		} catch (Exception e) {

			System.out.print(e.getMessage());
		} finally {

		}
	}

	/**
	 * Display a summary of the vehicles itinerary after execution
	 * @param vehicleItineraryStatus array of vehicles itinerary status
	 */
	private static void vehicleItinerarySummary(VehicleItineraryStatus[] vehicleItineraryStatus) {
		System.out.println("***********************************************************************");
		System.out.println("VEHICLES SUMMARY:");
		System.out.println("***********************************************************************");
		if (vehicleItineraryStatus.length > 0) {
			for (int vehicle = 0; vehicle < vehicleItineraryStatus.length; vehicle++) {
				System.out.printf("itinerary vehicle(%d):%s\n",vehicleItineraryStatus[vehicle].getVehicleID() ,
						vehicleItineraryStatus[vehicle].getItineraryExecutingSummary());
			}
		}
		else{
			System.out.println("No vehicle itinerary available.");
		}
		System.out.println("***********************************************************************");
	}

	/**
	 * Display set of message on the console, so the app user khows how it works
	 */
	private static void programInstructions() {
		System.out.println("This App can schedule up to 20 vehicles to perform passenger trips");
		System.out.println(
				"To specify the routes a specific vehicle must perfom, create a file named in[Number of Vehicle].txt following the next conventions:");
		System.out.println(
				"1 - The [Number of Vehicle] is a number between 1 and 20, numbers outside this range wont be scheduled");
		System.out.println("2 - Each route must be a single line in the file");
		System.out.println("3 - Each route must contain the next format");
		System.out.println(
				"	* 'PassengerID','Passenger Name','Passenger Email {optional}, Vehicle Movements {F = FORWAR, L = LEFT, R = RIGHT}'");
		System.out.println("4 - Max number of routes per vehicle must be 10");
		System.out.println("The result of the vehicle travel will be recorded in the out[Number of Vehicle].txt file");
		System.out.println("");
		System.out.println("");
	}
}
