package co.com.psl.googlevehicletracking.classes;

/**
 * Store the summary of a vehicle itinerary after completition
 * @author Alejandro
 *
 */
public class VehicleItineraryStatus {

	private boolean itineraryExecutingSuccesfully;
	private String itineraryExecutingSummary;
	private int vehicleID;
	
	/**
	 * Construct a VehicleItineraryStatus 
	 * @param vehicleID Id of the vehicle (1 to 20)
	 * @param itineraryExecutingSuccesfully itinerary was successful
	 * @param itineraryExecutingSummary summary of the execution
	 */
	public VehicleItineraryStatus(int vehicleID,boolean itineraryExecutingSuccesfully,String itineraryExecutingSummary){
		this.vehicleID = vehicleID;
		this.itineraryExecutingSuccesfully= itineraryExecutingSuccesfully;
		this.itineraryExecutingSummary = itineraryExecutingSummary;
	}

	
	/**
	 * Gets the vehicle ID
	 * @return int
	 */
	public int getVehicleID() {
		return vehicleID;
	}

	/**
	 * Is itinerary execution successful
	 * @return
	 */
	public boolean isItineraryExecutingSuccesful() {
		return itineraryExecutingSuccesfully;
	}

	/**
	 * Gets itinerary execution summary
	 * @return
	 */
	public String getItineraryExecutingSummary() {
		return itineraryExecutingSummary;
	}
	
	
}
