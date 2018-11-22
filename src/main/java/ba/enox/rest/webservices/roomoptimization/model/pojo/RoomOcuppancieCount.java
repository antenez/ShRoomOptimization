package ba.enox.rest.webservices.roomoptimization.model.pojo;

import ba.enox.rest.webservices.roomoptimization.model.enums.RoomTypes;

public class RoomOcuppancieCount {

	RoomTypes roomType;
	int numberOfRooms;
	float paymentAmount;

	public RoomOcuppancieCount(RoomTypes roomType) {
		this.roomType = roomType;
	}

	public RoomTypes getRoomType() {
		return roomType;
	}

	public int getNumberOfRooms() {
		return numberOfRooms;
	}

	public float getPaymentAmount() {
		return paymentAmount;
	}

	public void addAnotherClientRoom(float amount) {
		this.numberOfRooms++;
		this.paymentAmount += amount;
	}

}
