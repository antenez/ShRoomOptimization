package ba.enox.rest.webservices.roomoptimization.roomconfigurator;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.startsWith;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import ba.enox.rest.webservices.roomoptimization.model.enums.RoomTypes;
import ba.enox.rest.webservices.roomoptimization.model.pojo.RoomOcuppancieCount;
import ba.enox.rest.webservices.roomoptimization.model.request.InitializeAvailableRoomsRequest;
import ba.enox.rest.webservices.roomoptimization.services.OccupancyOptimizationService;
import util.JsonHelper;

/**
 * 
 * @author eno This is unit test for ResourceController All interaction with
 *         services is mocked
 */
@RunWith(SpringRunner.class)
@WebMvcTest(value = RoomAvailabilityAPI.class)
public class RoomAvailabilityAPITest {

	/*
	 * Used to simulate full end to end case.
	 */
	@SpyBean
	OccupancyOptimizationService occupancyOptimizationService;

	@Autowired
	private ResourceLoader resourceLoader;

	@Autowired
	private MockMvc mockMvc;

	@Value("classpath:gistSample.json")
	Resource samplePaymentsArrayRequest;
	@Value("classpath:configureAvailableRoomsRequest.json")
	Resource configureAvailableRoomsRequest;

	@Test
	public void testInitializationEndpontSuccessCase() throws Exception {
		// Prepare request
		ObjectMapper objectMapper = new ObjectMapper();
		InitializeAvailableRoomsRequest initializeAvailableRoomsRequest = null;
		initializeAvailableRoomsRequest = objectMapper.readValue(configureAvailableRoomsRequest.getFile(),
				InitializeAvailableRoomsRequest.class);

		// test invocation
		mockMvc.perform(post("/availableRooms/configure")
				.content(JsonHelper.asJsonString(initializeAvailableRoomsRequest)).contentType(APPLICATION_JSON))
				.andExpect(status().isOk());

		Mockito.verify(occupancyOptimizationService).initialize(
				initializeAvailableRoomsRequest.getNumberOfPremiumRooms(),
				initializeAvailableRoomsRequest.getNumberOfEconomyRooms(),
				initializeAvailableRoomsRequest.getPremiumThreshold());
	}

	@Test
	public void testInitializationEndpontWithInvalidValues() throws Exception {
		InitializeAvailableRoomsRequest initializeAvailableRoomsRequest = new InitializeAvailableRoomsRequest(0, 0, 10);
		// test invocation
		mockMvc.perform(post("/availableRooms/configure")
				.content(JsonHelper.asJsonString(initializeAvailableRoomsRequest)).contentType(APPLICATION_JSON))
				.andExpect(status().isBadRequest()).andExpect(jsonPath("$.message", is("Validation Failed")))
				.andExpect(jsonPath("$.details",
						startsWith("org.springframework.validation.BeanPropertyBindingResult: 3 errors")));
	}

	@Test
	public void testcalculateOptimalRevenue() throws Exception {
		// Prepare request
		ObjectMapper objectMapper = new ObjectMapper();
		float[] calculateOptimalRevenueRequest = objectMapper.readValue(this.samplePaymentsArrayRequest.getFile(),
				float[].class);
		RoomOcuppancieCount[] expectedRespponse = new RoomOcuppancieCount[] {
				new RoomOcuppancieCount(RoomTypes.PREMIUM_ROOM), new RoomOcuppancieCount(RoomTypes.ECONOMY_ROOM) };
		expectedRespponse[0].addAnotherClientRoom(999);
		expectedRespponse[1].addAnotherClientRoom(666);

		Mockito.when(occupancyOptimizationService.getOptimizedProfit(calculateOptimalRevenueRequest))
				.thenReturn(expectedRespponse);

		// test invocation
		mockMvc.perform(post("/availableRooms/revenue").content(JsonHelper.asJsonString(calculateOptimalRevenueRequest))
				.contentType(APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$[0].numberOfRooms", is(1))).andExpect(jsonPath("$[0].paymentAmount", is(999.0)))
				.andExpect(jsonPath("$[0].roomType", is("PREMIUM_ROOM")))
				.andExpect(jsonPath("$[1].numberOfRooms", is(1))).andExpect(jsonPath("$[1].paymentAmount", is(666.0)))
				.andExpect(jsonPath("$[1].roomType", is("ECONOMY_ROOM")));

		Mockito.verify(occupancyOptimizationService).getOptimizedProfit(calculateOptimalRevenueRequest);
	}

}
