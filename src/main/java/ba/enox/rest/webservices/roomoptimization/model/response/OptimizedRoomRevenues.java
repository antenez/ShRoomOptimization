package ba.enox.rest.webservices.roomoptimization.model.response;

import ba.enox.rest.webservices.roomoptimization.model.pojo.RoomOcuppancieCount;

public class OptimizedRoomRevenues {
	RoomOcuppancieCount[] optimizedRoomOccupanciesCount;

	public OptimizedRoomRevenues() {
	}

	public OptimizedRoomRevenues(RoomOcuppancieCount[] optimizedRoomOccupanciesCount) {
		this.optimizedRoomOccupanciesCount = optimizedRoomOccupanciesCount;
	}

	public RoomOcuppancieCount[] getOptimizedRoomOccupanciesCount() {
		return optimizedRoomOccupanciesCount;
	}
}
