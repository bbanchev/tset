package bg.bbanchev.challenges.tset.api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import bg.bbanchev.challenges.tset.data.Service;
import bg.bbanchev.challenges.tset.service.ReleaseManager;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class ReleaseControllerTest {
	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private ReleaseManager manager;

	@BeforeEach
	void setup() throws Exception {
		manager.releaseService(new Service("Service A", 1));
		manager.releaseService(new Service("Service B", 1));
		manager.releaseService(new Service("Service A", 2));

	}

	@Test
	public void testDeployValidScenario() throws Exception {
		mockMvc.perform(get("/services").param("systemVersion", "1").header("Accept", MediaType.APPLICATION_JSON_VALUE))
				.andExpect(status().isOk()).andExpect(content().json("[{\"name\":\"Service A\",\"version\":1}]", false));
		mockMvc.perform(get("/services").param("systemVersion", "2").header("Accept", MediaType.APPLICATION_JSON_VALUE))
				.andExpect(status().isOk()).andExpect(content().json("[{\"name\":\"Service A\",\"version\":1},{\"name\":\"Service B\",\"version\":1}]", false));

		mockMvc.perform(get("/services").param("systemVersion", "3").header("Accept", MediaType.APPLICATION_JSON_VALUE))
				.andExpect(status().isOk()).andExpect(content().json("[{\"name\":\"Service A\",\"version\":2},{\"name\":\"Service B\",\"version\":1}]", false));

		mockMvc.perform(get("/services").param("systemVersion", "4").header("Accept", MediaType.APPLICATION_JSON_VALUE))
				.andExpect(status().isOk()).andExpect(content().json("[]", false));
	}

}
