package de.petersan.challenge.reply.service;

import static de.petersan.challenge.reply.scheduler.TestFixture.car;
import static de.petersan.challenge.reply.scheduler.TestFixture.demand;
import static de.petersan.challenge.reply.scheduler.TestFixture.timePos;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.asyncDispatch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.request;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.springframework.test.web.servlet.MvcResult;

import javax.inject.Inject;

public class SchedulerServiceTest extends ServiceIntegrationTestBase {

  @Inject
  private CarService carService;

  @Inject
  private DemandService demandService;

  @Before
  public void setUp() {
    carService.cleanUp();
    demandService.cleanUp();
  }


  @Test
  public void testSchedule() throws Exception {
    carService.createCar(car(null, 0));
    demandService.updateDemand(1, demand(1, timePos(0, 0), timePos(2, 2)));
    MvcResult ra = mockMvc.perform(get("/api/scheduler")).andExpect(request().asyncStarted()).andReturn();

    mockMvc.perform(asyncDispatch(ra)).andExpect(status().isOk());
  }

  @Test
  public void testImpossibleSchedule() throws Exception {
    carService.createCar(car(null, 0));
    demandService.updateDemand(1, demand(1, timePos(0, 3), timePos(2, 2)));

    // there is no car on position 3
    MvcResult ra = mockMvc.perform(get("/api/scheduler")).andExpect(request().asyncStarted()).andReturn();

    mockMvc.perform(asyncDispatch(ra)).andExpect(status().isConflict());
  }
}
