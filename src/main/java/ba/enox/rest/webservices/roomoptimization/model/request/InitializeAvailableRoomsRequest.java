package ba.enox.rest.webservices.roomoptimization.model.request;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class InitializeAvailableRoomsRequest {
	@NotNull(message = "Number of Premium rooms required")
	@Min(value = 1, message = "Minimum available premium rooms is 1")
	int numberOfPremiumRooms;
	@NotNull(message = "Number of Economy rooms required")
	@Min(value = 1, message = "Minimum available economy rooms is 1")
	int numberOfEconomyRooms;
	
	@Min(value = 20, message = "Minimum treshold is 20 and default is 100")
	float premiumThreshold = 100;
	
	public InitializeAvailableRoomsRequest() {}
	
	/**
	 * Full constructor
	 * @param numberOfPremiumRooms
	 * @param numberOfEconomyRooms
	 * @param premiumTreshold 
	 */
	public InitializeAvailableRoomsRequest(int numberOfPremiumRooms, int numberOfEconomyRooms, float premiumThreshold) {
		this.numberOfPremiumRooms = numberOfPremiumRooms;
		this.numberOfEconomyRooms = numberOfEconomyRooms;
		this.premiumThreshold = premiumThreshold;
	}
	
	public int getNumberOfPremiumRooms() {
		return numberOfPremiumRooms;
	}
	public int getNumberOfEconomyRooms() {
		return numberOfEconomyRooms;
	}
	public float getPremiumThreshold() {
		return premiumThreshold;
	}
}
