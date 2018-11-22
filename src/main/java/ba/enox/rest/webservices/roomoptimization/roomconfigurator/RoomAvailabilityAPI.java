package ba.enox.rest.webservices.roomoptimization.roomconfigurator;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import ba.enox.rest.webservices.roomoptimization.model.pojo.RoomOcuppancieCount;
import ba.enox.rest.webservices.roomoptimization.model.request.InitializeAvailableRoomsRequest;
import ba.enox.rest.webservices.roomoptimization.services.OccupancyOptimizationService;

@RestController
public class RoomAvailabilityAPI {

	@Autowired
	OccupancyOptimizationService occupancyOptimizationService;

	@PostMapping("/availableRooms/configure")
	public ResponseEntity<Object> configureAvailableRooms(
			@Valid @RequestBody InitializeAvailableRoomsRequest initializeAvailableRoomsRequest) {
		occupancyOptimizationService.initialize(initializeAvailableRoomsRequest.getNumberOfPremiumRooms(),
				initializeAvailableRoomsRequest.getNumberOfEconomyRooms(),
				initializeAvailableRoomsRequest.getPremiumThreshold());
		return ResponseEntity.ok().build();
	}

	@PostMapping("/availableRooms/revenue")
	public RoomOcuppancieCount[] calculateOptimalRevenue(@Valid @RequestBody float[] paymentAmounts) {
		return occupancyOptimizationService.getOptimizedProfit(paymentAmounts);
	}
}
