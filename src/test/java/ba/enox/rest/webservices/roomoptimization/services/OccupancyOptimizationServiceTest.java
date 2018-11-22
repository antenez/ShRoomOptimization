package ba.enox.rest.webservices.roomoptimization.services;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import ba.enox.rest.webservices.roomoptimization.model.enums.RoomTypes;
import ba.enox.rest.webservices.roomoptimization.model.pojo.RoomOcuppancieCount;

/*
 * This is unit test
 */

@RunWith(PowerMockRunner.class)
@PrepareForTest
public class OccupancyOptimizationServiceTest {

	private OccupancyOptimizationService occupancyOptimizationService = new OccupancyOptimizationService();

	private static final float[] amounts = new float[] { 23, 45, 155, 374, 22, 99, 100, 101, 115, 209 };
	private int numberOfPremiumRooms;
	private int numberOfEconomyRooms;

	@Test
	public void testPremium3Economy3RegularCount() {
		numberOfPremiumRooms = 3;
		numberOfEconomyRooms = 3;

		occupancyOptimizationService.initialize(numberOfPremiumRooms, numberOfEconomyRooms, 100);
		RoomOcuppancieCount[] roomOccupancieCount = occupancyOptimizationService.getOptimizedProfit(amounts);

		assertEquals(RoomTypes.PREMIUM_ROOM, roomOccupancieCount[0].getRoomType());
		assertEquals("Expected used premium rooms not matched!", 3, roomOccupancieCount[0].getNumberOfRooms());
		assertEquals("Expected used premium amount not matched!", new Float(738),
				new Float(roomOccupancieCount[0].getPaymentAmount()));

		assertEquals(RoomTypes.ECONOMY_ROOM, roomOccupancieCount[1].getRoomType());
		assertEquals("Expected used economy rooms not matched!", 3, roomOccupancieCount[1].getNumberOfRooms());
		assertEquals("Expected used economy amount not matched!", new Float(167),
				new Float(roomOccupancieCount[1].getPaymentAmount()));

	}

	@Test
	public void testPremium7Economy5ExpectOccupied6and4() {
		numberOfPremiumRooms = 7;
		numberOfEconomyRooms = 5;

		occupancyOptimizationService.initialize(numberOfPremiumRooms, numberOfEconomyRooms, 100);
		RoomOcuppancieCount[] roomOccupancieCount = occupancyOptimizationService.getOptimizedProfit(amounts);

		assertEquals(RoomTypes.PREMIUM_ROOM, roomOccupancieCount[0].getRoomType());
		assertEquals("Expected used premium rooms not matched!", 6, roomOccupancieCount[0].getNumberOfRooms());
		assertEquals("Expected used premium amount not matched!", new Float(1054),
				new Float(roomOccupancieCount[0].getPaymentAmount()));

		assertEquals(RoomTypes.ECONOMY_ROOM, roomOccupancieCount[1].getRoomType());
		assertEquals("Expected used economy rooms not matched!", 4, roomOccupancieCount[1].getNumberOfRooms());
		assertEquals("Expected used economy amount not matched!", new Float(189),
				new Float(roomOccupancieCount[1].getPaymentAmount()));

	}

	@Test
	public void testPremium2Economy7AllPremiumOcupiedAndRestNotCountedEconomyRooms4() {
		numberOfPremiumRooms = 2;
		numberOfEconomyRooms = 7;

		occupancyOptimizationService.initialize(numberOfPremiumRooms, numberOfEconomyRooms, 100);
		RoomOcuppancieCount[] roomOccupancieCount = occupancyOptimizationService.getOptimizedProfit(amounts);

		assertEquals(RoomTypes.PREMIUM_ROOM, roomOccupancieCount[0].getRoomType());
		assertEquals("Expected used premium rooms not matched!", 2, roomOccupancieCount[0].getNumberOfRooms());
		assertEquals("Expected used premium amount not matched!", new Float(583),
				new Float(roomOccupancieCount[0].getPaymentAmount()));

		assertEquals(RoomTypes.ECONOMY_ROOM, roomOccupancieCount[1].getRoomType());
		assertEquals("Expected used economy rooms not matched!", 4, roomOccupancieCount[1].getNumberOfRooms());
		assertEquals("Expected used economy amount not matched!", new Float(189),
				new Float(roomOccupancieCount[1].getPaymentAmount()));
	}

	/*
	 * Free Premium rooms: 7 one will be empty and filled from economy class Free
	 * Economy rooms: 1 will be second highest Usage Premium: 7 (EUR 1153) Usage
	 * Economy: 1 (EUR 45)
	 */
	@Test
	public void testPremium7Economy1OnePremiumWillBeFilledWithHighestFromEconomyClasAndEconomyClassWillTakeNextHighest() {
		numberOfPremiumRooms = 7;
		numberOfEconomyRooms = 1;

		occupancyOptimizationService.initialize(numberOfPremiumRooms, numberOfEconomyRooms, 100);
		RoomOcuppancieCount[] roomOccupancieCount = occupancyOptimizationService.getOptimizedProfit(amounts);

		assertEquals(RoomTypes.PREMIUM_ROOM, roomOccupancieCount[0].getRoomType());
		assertEquals("Expected used premium rooms not matched!", 7, roomOccupancieCount[0].getNumberOfRooms());
		assertEquals("Expected used premium amount not matched!", new Float(1153),
				new Float(roomOccupancieCount[0].getPaymentAmount()));

		assertEquals(RoomTypes.ECONOMY_ROOM, roomOccupancieCount[1].getRoomType());
		assertEquals("Expected used economy rooms not matched!", 1, roomOccupancieCount[1].getNumberOfRooms());
		assertEquals("Expected used economy amount not matched!", new Float(45),
				new Float(roomOccupancieCount[1].getPaymentAmount()));
	}

}
