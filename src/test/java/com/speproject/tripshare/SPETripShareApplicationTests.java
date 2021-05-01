package com.speproject.tripshare;
import com.speproject.tripshare.web.MainController;
import org.codehaus.jettison.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class SPETripShareApplicationTests {

	private static Logger log = LoggerFactory.getLogger(SPETripShareApplicationTests.class);
	String authToken;

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private MainController controller;

	@Test
	public void contextLoads() throws Exception {
		assertThat(controller).isNotNull();
	}

	@Test
	public void unauthorizedApiRequestsTest() throws Exception{
		log.info("Unauthorized API Request test");
		mockMvc.perform(get("/user/getdetails"))
				.andExpect(status().is4xxClientError());
	}

	@Test
	public void userRegistrationLoginAuthenticationDeleteTests() throws Exception{
		log.info("User Registration test");
		mockMvc.perform(post("/registration")
				.contentType(APPLICATION_JSON_VALUE)
				.content("{\"firstName\":\"Foo\", " +
						"\"lastName\":\"Bar\", " +
						"\"email\":\"foo@bar.com\", " +
						"\"password\":\"12345\"}"))
				.andExpect(status().isOk());

		log.info("Registration with Duplicate email test");
		mockMvc.perform(post("/registration")
				.contentType(APPLICATION_JSON_VALUE)
				.content("{\"firstName\":\"Foo\", " +
						"\"lastName\":\"Bar\", " +
						"\"email\":\"foo@bar.com\", " +
						"\"password\":\"12345\"}"))
				.andExpect(status().is4xxClientError());

		log.info("Login test");
		MvcResult mvcResult = mockMvc.perform(post("/user/authenticate")
										.contentType(APPLICATION_JSON_VALUE)
										.content("{\"email\":\"foo@bar.com\", " +
												"\"password\":\"12345\"}"))
										.andExpect(status().isOk())
										.andReturn();
		JSONObject resp = new JSONObject(mvcResult.getResponse().getContentAsString());
		authToken = resp.getString("token");

		log.info("User Get Details test");
		mockMvc.perform(get("/user/getdetails")
				.header("Authorization", authToken))
				.andExpect(status().isOk());

		log.info("Delete User test");
		mockMvc.perform(post("/user/delete")
				.header("Authorization", authToken))
				.andExpect(status().isOk());
	}

}
