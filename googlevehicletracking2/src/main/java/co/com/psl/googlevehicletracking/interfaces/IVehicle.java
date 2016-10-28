package co.com.psl.googlevehicletracking.interfaces;

import co.com.psl.googlevehicletracking.classes.Route;
import co.com.psl.googlevehicletracking.exception.GoogleVehicleTrackingException;

/**
 * Define the actions and return value of a vehicle
 * @author Alejandro
 *
 */
public interface IVehicle {

	/**
	 * Execute the routes assigned the vehicle 
	 * @throws GoogleVehicleTrackingException
	 */
	void doScheduledRoutes() throws GoogleVehicleTrackingException;
	
	/**
	 * Gets the routes of the vehicle 
	 * @return Route[]
	 */
	Route[] getRoutes();	
}
