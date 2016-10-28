package co.com.psl.googlevehicletracking.exception;

/**
 * Application exception which represent custom exceptions
 * @author Alejandro
 *
 */
public class GoogleVehicleTrackingException extends Exception {

	private static final long serialVersionUID = 5691543889226404144L;

	/**
	 * Construct a exception
	 * @param message exception message
	 */
	public GoogleVehicleTrackingException(String message){
		super(message);
	}
	
	/**
	 * 
	 * @param message exception message
	 * @param cause The exception which cause the failure 
	 */
	public GoogleVehicleTrackingException(String message, Throwable cause){
		super(message,cause);
	}
}
