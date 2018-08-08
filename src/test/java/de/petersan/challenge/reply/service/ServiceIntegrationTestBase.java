package de.petersan.challenge.reply.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.inject.Inject;

@RunWith(SpringRunner.class)
@SpringBootTest

@TestPropertySource(properties = "simulator.active=false")

public abstract class ServiceIntegrationTestBase {

  @Inject
  private WebApplicationContext webApplicationContext;

  protected MockMvc mockMvc;

  @Before
  public void serviceIntegrationTestBaseSetUp() {
    mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
        .alwaysDo(MockMvcResultHandlers.print()).build();
  }

  protected String serialize(Object object) throws JsonProcessingException {
    ObjectMapper mapper = new ObjectMapper();
    mapper.registerModule(new JavaTimeModule());
    mapper.registerModule(new Jdk8Module());
    mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    return mapper.writeValueAsString(object);
  }

}
