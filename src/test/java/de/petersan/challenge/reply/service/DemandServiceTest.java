package de.petersan.challenge.reply.service;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import de.petersan.challenge.reply.domain.Demand;
import de.petersan.challenge.reply.domain.EngineType;
import de.petersan.challenge.reply.domain.Gender;
import de.petersan.challenge.reply.domain.Model;
import de.petersan.challenge.reply.domain.User;
import de.petersan.challenge.reply.scheduler.TestFixture;

import org.junit.Before;
import org.junit.Test;
import org.springframework.hateoas.MediaTypes;

import java.time.LocalDate;

import javax.inject.Inject;

public class DemandServiceTest extends ServiceIntegrationTestBase {

  @Inject
  private DemandService testee;

  @Before
  public void setUp() {
    testee.cleanUp();
  }

  private User user(String username) {
    User user = new User();
    user.setUsername(username);
    user.setGender(Gender.OTHER);
    user.setBirthdate(LocalDate.now());
    return user;
  }

  private Demand demand(String username) {
    Demand demand = new Demand();
    demand.setUsername(username);
    demand.setModel(Model.COMPACT);
    demand.setEngine(EngineType.DIESEL);
    demand.setDropOff(TestFixture.timePos(0, 0));
    demand.setPickUp(TestFixture.timePos(10, 10));
    return demand;
  }

  @Test
  public void testGetUser() throws Exception {
    testee.updateUser("test", user("test"));
    mockMvc.perform(get("/api/users/test")).andExpect(status().isOk());
  }

  @Test
  public void testCreateUser() throws Exception {
    mockMvc.perform(get("/api/users/test")).andExpect(status().isNotFound());

    mockMvc.perform(put("/api/users/test").content(serialize(user("test"))).contentType(MediaTypes.HAL_JSON))
        .andExpect(status().isNoContent());

    mockMvc.perform(get("/api/users/test")).andExpect(status().isOk());
  }

  @Test
  public void testDeleteUser() throws Exception {
    testee.updateUser("first-user", user("first-user"));
    testee.updateUser("second-user", user("second-user"));

    testee.createDemand(demand("second-user"));

    mockMvc.perform(delete("/api/users/first-user")).andExpect(status().isNoContent());

    mockMvc.perform(delete("/api/users/second-user")).andExpect(status().isConflict());

    mockMvc.perform(delete("/api/users/third-user")).andExpect(status().isNotFound());
  }

  @Test
  public void testCreateDemand() throws Exception {
    mockMvc.perform(post("/api/demands").content(serialize(demand("unknown"))).contentType(MediaTypes.HAL_JSON))
        .andExpect(status().isNotFound());

    testee.updateUser("test", user("test"));

    mockMvc.perform(post("/api/demands").content(serialize(demand("test"))).contentType(MediaTypes.HAL_JSON))
        .andExpect(status().isCreated());

  }

}
