package co.com.psl.googlevehicletracking.classes;

import java.io.IOException;
import java.util.concurrent.Callable;

import co.com.psl.googlevehicletracking.exception.GoogleVehicleTrackingException;
import co.com.psl.googlevehicletracking.interfaces.IReadWriteFile;
import co.com.psl.googlevehicletracking.interfaces.IVehicle;
import co.com.psl.googlevehicletracking.spring.SpringAppContextSingleton;

/**
 * Represent the execution of a vehicle itinerary
 * @author Alejandro
 *
 */
public class VehicleItinerary implements Callable<VehicleItineraryStatus> {

	private Integer itineraryNumber;	
	private VehicleItineraryStatus vehicleItineraryStatus;
	private boolean itineraryExecutingSuccesfully;
	private String itineraryExecutingSummary; 
	

	/**
	 * Construct a vehicle itinerary
	 * @param itineraryNumber number of the itinerary
	 */
	public VehicleItinerary(Integer itineraryNumber) {
		this.itineraryNumber = itineraryNumber;
		itineraryExecutingSuccesfully = false;
		itineraryExecutingSummary = "Ok";
	}

	/**
	 * Execute the vehicle itinerary
	 * @throws GoogleVehicleTrackingException
	 */
	private void doVehicleItinerary() throws GoogleVehicleTrackingException {

		try {
			
			String itineraryInputFile =  String.format("in%d.txt", itineraryNumber);
			String itineraryOutputFile =  String.format("out%d.txt", itineraryNumber);
			
			SpringAppContextSingleton springAppContextSingleton = SpringAppContextSingleton
					.getSpringAppContextSingleton();

			IReadWriteFile readWriteFile = (IReadWriteFile) springAppContextSingleton.getBean("IReadWriteFile");
			Route[] routesToTravel = readWriteFile.readInputFile(itineraryInputFile);
			IVehicle vehicle = (IVehicle) springAppContextSingleton.getBean("IVehicle",
					new Object[] { routesToTravel });
			vehicle.doScheduledRoutes();
			Route[] routesToTraveled = vehicle.getRoutes();

			readWriteFile.writeOutputFile(itineraryOutputFile, routesToTraveled);
		} catch (IOException e) {
			throw new GoogleVehicleTrackingException(String.format("Exception on Vehicle(%d) Itinerary", itineraryNumber),
					e);
		}

	}

	@Override
	public VehicleItineraryStatus call()  {				
		try {
			doVehicleItinerary();
			itineraryExecutingSuccesfully = true;
		} catch (Exception e) {
			itineraryExecutingSummary = e.getMessage();			
		}
		finally{
			vehicleItineraryStatus = new VehicleItineraryStatus(itineraryNumber,itineraryExecutingSuccesfully, itineraryExecutingSummary);
		}
		return vehicleItineraryStatus;
	}

}
