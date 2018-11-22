package ba.enox.rest.webservices.roomoptimization.roomconfigurator;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import ba.enox.rest.webservices.roomoptimization.model.request.InitializeAvailableRoomsRequest;
import ba.enox.rest.webservices.roomoptimization.services.OccupancyOptimizationService;
import util.JsonHelper;

/**
 * 
 * @author eno This is Integration test and we willin this example use real
 *         service methods because it is singletoon instance shared between
 *         clients. For simplicity reasons we will cover one example
 */
@RunWith(SpringRunner.class)
@WebMvcTest(value = RoomAvailabilityAPI.class)
public class RoomAvailabilityAPIIntegrationTest {

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
	public void testInitializationAndCalculationEndpontSuccessCase() throws Exception {
		// Prepare request
		ObjectMapper objectMapper = new ObjectMapper();
		InitializeAvailableRoomsRequest initializeAvailableRoomsRequest = null;
		initializeAvailableRoomsRequest = objectMapper.readValue(configureAvailableRoomsRequest.getFile(),
				InitializeAvailableRoomsRequest.class);

		// test initialization
		mockMvc.perform(post("/availableRooms/configure")
				.content(JsonHelper.asJsonString(initializeAvailableRoomsRequest)).contentType(APPLICATION_JSON))
				.andExpect(status().isOk());

		float[] calculateOptimalRevenueRequest = objectMapper.readValue(this.samplePaymentsArrayRequest.getFile(),
				float[].class);

		// test invocation
		mockMvc.perform(post("/availableRooms/revenue").content(JsonHelper.asJsonString(calculateOptimalRevenueRequest))
				.contentType(APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$[0].numberOfRooms", is(6))).andExpect(jsonPath("$[0].paymentAmount", is(1054.0)))
				.andExpect(jsonPath("$[0].roomType", is("PREMIUM_ROOM")))
				.andExpect(jsonPath("$[1].numberOfRooms", is(4))).andExpect(jsonPath("$[1].paymentAmount", is(189.0)))
				.andExpect(jsonPath("$[1].roomType", is("ECONOMY_ROOM")));

	}
}
