package bg.bbanchev.challenges.tset.api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.hamcrest.core.StringContains;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import bg.bbanchev.challenges.tset.data.Release;
import bg.bbanchev.challenges.tset.dto.ServiceDeployment;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class DeployControllerTest {

	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private ObjectMapper mapper;
	@Autowired
	private MongoTemplate template;

	@BeforeEach
	void setup() throws Exception {
		template.createCollection(Release.class);
	}

	@AfterEach
	void clean() {
		template.dropCollection(Release.class);
	}

	@Test
	public void testDeployValidScenario() throws Exception {
		mockMvc.perform(post("/deploy").content(mapper.writeValueAsString(new ServiceDeployment("Service A", 1)))
				.header("Content-Type", MediaType.APPLICATION_JSON_VALUE)).andExpect(status().isCreated())
				.andExpect(content().string("1"));

		mockMvc.perform(post("/deploy").content(mapper.writeValueAsString(new ServiceDeployment("Service B", 1)))
				.header("Content-Type", MediaType.APPLICATION_JSON_VALUE)).andExpect(status().isCreated())
				.andExpect(content().string("2"));

		mockMvc.perform(post("/deploy").content(mapper.writeValueAsString(new ServiceDeployment("Service A", 2)))
				.header("Content-Type", MediaType.APPLICATION_JSON_VALUE)).andExpect(status().isCreated())
				.andExpect(content().string("3"));

		mockMvc.perform(post("/deploy").content(mapper.writeValueAsString(new ServiceDeployment("Service B", 1)))
				.header("Content-Type", MediaType.APPLICATION_JSON_VALUE)).andExpect(status().isOk())
				.andExpect(content().string("3"));

	}

	@Test
	public void testDeployInvalidService() throws Exception {
		mockMvc.perform(post("/deploy").content(mapper.writeValueAsString(new ServiceDeployment(null, 1)))
				.header("Content-Type", MediaType.APPLICATION_JSON_VALUE)).andExpect(status().isBadRequest())
				.andExpect(content().string(new StringContains("Invalid service deployment information")));

	}

	@Test
	public void testDeployOfPreviousVersionService() throws Exception {

		mockMvc.perform(post("/deploy").content(mapper.writeValueAsString(new ServiceDeployment("Service C", 3)))
				.header("Content-Type", MediaType.APPLICATION_JSON_VALUE)).andExpect(status().isCreated())
				.andExpect(content().string("1"));

		mockMvc.perform(post("/deploy").content(mapper.writeValueAsString(new ServiceDeployment("Service C", 2)))
				.header("Content-Type", MediaType.APPLICATION_JSON_VALUE)).andExpect(status().isConflict())
				.andExpect(content().string(new StringContains("Newer version [3] already deployed!")));

	}
}
