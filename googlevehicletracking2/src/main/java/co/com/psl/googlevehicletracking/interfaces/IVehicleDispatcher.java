package co.com.psl.googlevehicletracking.interfaces;

import co.com.psl.googlevehicletracking.classes.VehicleItineraryStatus;
import co.com.psl.googlevehicletracking.exception.GoogleVehicleTrackingException;

/**
 * Define a vehicle Dispatcher operator
 * @author Alejandro
 *
 */
public interface IVehicleDispatcher {
	
	/**
	 * Dispatch as many vehicles as necessary
	 * @return VehicleItineraryStatus[]
	 * @throws GoogleVehicleTrackingException
	 */
	VehicleItineraryStatus[] executeVehiclesTraveling() throws GoogleVehicleTrackingException;	
}
