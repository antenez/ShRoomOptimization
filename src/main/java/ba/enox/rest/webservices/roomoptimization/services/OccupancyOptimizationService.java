package ba.enox.rest.webservices.roomoptimization.services;

import java.util.Comparator;
import java.util.LinkedList;

import org.springframework.stereotype.Service;

import ba.enox.rest.webservices.roomoptimization.model.enums.RoomTypes;
import ba.enox.rest.webservices.roomoptimization.model.pojo.RoomOcuppancieCount;

/**
 * @author eno
 * 
 * Main implemented logic for optimized incomes for initialized configuration.
 * Configuration is stored here as singletoon shared bean and is available to all invocations
 * This will satisfy rule that after initialization calculated response should be returned.
 * I was considering that if anybody invoke new initialization, that it would be shared/used
 * by other optimization consumers.
 *
 */
@Service
public class OccupancyOptimizationService {

	int numberOfPremiumRooms;
	int numberOfEconomyRooms;
	float premiumThreshold;
	
	public OccupancyOptimizationService() {
	}

	public void initialize(int numberOfPremiumRooms, 	int numberOfEconomyRooms, 	float premiumThreshold) {
		this.numberOfPremiumRooms = numberOfPremiumRooms;
		this.numberOfEconomyRooms = numberOfEconomyRooms;
		this.premiumThreshold = premiumThreshold;
	}
	
	/**
	 * @param paymentAmounts
	 * @return RoomOcupancieCount array with Premium on index 0  and economy values on index 1
	 */
	public RoomOcuppancieCount[] getOptimizedProfit(float[] paymentAmounts) {
		RoomOcuppancieCount[] response = new RoomOcuppancieCount[] {new RoomOcuppancieCount(RoomTypes.PREMIUM_ROOM),new RoomOcuppancieCount(RoomTypes.ECONOMY_ROOM)};
		
		/*
		 * We are sorting out amounts according to premium Threshold criteria
		 * into two groups premium and economy.
		 * Later we sort list and after we calculate optimal earned money. 
		 */
		Comparator<? super Float> descendingComparator =  (Float o1 , Float o2) -> o2.compareTo(o1);
		LinkedList<Float> premiumAmounts = new LinkedList<>();
		LinkedList<Float> economyAmounts = new LinkedList<>();
		
		for(float p : paymentAmounts ) {
			if( p >= this.premiumThreshold ) {premiumAmounts.add(p);}
			else {	economyAmounts.add(p); }
		}
		
		premiumAmounts.sort(descendingComparator);
		economyAmounts.sort(descendingComparator);
		
		int economyOverloadedRequests = 0;
		int premiumUnusedRooms = 0;
		
		if(economyAmounts.size()>numberOfEconomyRooms) { economyOverloadedRequests = economyAmounts.size() - this.numberOfEconomyRooms; }
		if(premiumAmounts.size()<numberOfPremiumRooms) { premiumUnusedRooms = numberOfPremiumRooms - premiumAmounts.size(); }
		
		//Prepare premium
		for(int i = 0 ; i < numberOfPremiumRooms && !premiumAmounts.isEmpty() ; i++) {
			response[0].addAnotherClientRoom(premiumAmounts.poll());
		}
		//Reward highest in economy class to use premium if there are more then expected requests
		for(int i = 0 ; premiumUnusedRooms > i  && economyOverloadedRequests > i ; i++) {
				response[0].addAnotherClientRoom(economyAmounts.poll());
		}
		//Prepare economy 
		for(int i = 0 ; i < numberOfEconomyRooms && !economyAmounts.isEmpty() ; i++) {
			response[1].addAnotherClientRoom(economyAmounts.poll());
		}
		
		return response;
	} 
}
