package de.petersan.challenge.reply.service;

import static de.petersan.challenge.reply.domain.EngineType.ELECTRIC;
import static de.petersan.challenge.reply.domain.EngineType.HYBRID;
import static de.petersan.challenge.reply.domain.Feature.CLIMATE_CONTROL;
import static de.petersan.challenge.reply.domain.Feature.INFOTAINMENT_SYSTEM;
import static de.petersan.challenge.reply.domain.Feature.LEATHER_SEATS;
import static de.petersan.challenge.reply.domain.Model.COMPACT;
import static de.petersan.challenge.reply.domain.Model.MID_SIZE;
import static org.hamcrest.Matchers.endsWith;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import de.petersan.challenge.reply.domain.Car;
import de.petersan.challenge.reply.domain.EngineType;
import de.petersan.challenge.reply.domain.Feature;
import de.petersan.challenge.reply.domain.Model;

import org.junit.Before;
import org.junit.Test;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;

import java.util.Arrays;

import javax.inject.Inject;


public class CarServiceTest extends ServiceIntegrationTestBase {

  @Inject
  private CarService testee;

  @Before
  public void setUp() {
    testee.cleanUp();
  }

  private Car car(int position, EngineType engine, Model model, Feature... features) {
    Car car = new Car();
    car.setPosition(position);
    car.setModel(model);
    car.setEngine(engine);
    car.getFeatures().addAll(Arrays.asList(features));

    return car;
  }

  @Test
  public void testGetAllCars() throws Exception {

    testee.createCar(car(1, ELECTRIC, MID_SIZE, CLIMATE_CONTROL, INFOTAINMENT_SYSTEM));
    testee.createCar(car(2, HYBRID, COMPACT, CLIMATE_CONTROL, LEATHER_SEATS));

    mockMvc.perform(get("/api/cars"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.[0].id", is(1)))
        .andExpect(jsonPath("$.[0].position", is(1)))
        .andExpect(jsonPath("$.[0].engine", is("ELECTRIC")))
        .andExpect(jsonPath("$.[0].model", is("MID_SIZE")))
        .andExpect(jsonPath("$.[0].features[0]", is("CLIMATE_CONTROL")))
        .andExpect(jsonPath("$.[0].features[1]", is("INFOTAINMENT_SYSTEM")))
        .andExpect(jsonPath("$.[1].id", is(2)))
        .andExpect(jsonPath("$.[1].position", is(2)))
        .andExpect(jsonPath("$.[1].engine", is("HYBRID")))
        .andExpect(jsonPath("$.[1].model", is("COMPACT")))
        .andExpect(jsonPath("$.[1].features[0]", is("CLIMATE_CONTROL")))
        .andExpect(jsonPath("$.[1].features[1]", is("LEATHER_SEATS")));
  }

  @Test
  public void testCreateCar() throws Exception {
    mockMvc.perform(get("/api/cars/1")).andExpect(status().isNotFound());

    mockMvc.perform(post("/api/cars")
        .content(serialize(car(2, ELECTRIC, MID_SIZE, CLIMATE_CONTROL, INFOTAINMENT_SYSTEM)))
        .contentType(MediaTypes.HAL_JSON))
        .andExpect(status().isCreated())
        .andExpect(header().string(HttpHeaders.LOCATION, endsWith("/cars/1")));

    mockMvc.perform(get("/api/cars/1"))
        .andExpect(jsonPath("$.id", is(1)))
        .andExpect(jsonPath("$.position", is(2)))
        .andExpect(jsonPath("$.engine", is("ELECTRIC")))
        .andExpect(jsonPath("$.model", is("MID_SIZE")))
        .andExpect(jsonPath("$.features[0]", is("CLIMATE_CONTROL")))
        .andExpect(jsonPath("$.features[1]", is("INFOTAINMENT_SYSTEM")));
  }

  @Test
  public void testUpdateCar() throws Exception {
    testee.createCar(car(3, ELECTRIC, MID_SIZE, CLIMATE_CONTROL, INFOTAINMENT_SYSTEM));

    mockMvc.perform(get("/api/cars/1"))
        .andExpect(jsonPath("$.id", is(1)))
        .andExpect(jsonPath("$.position", is(3)))
        .andExpect(jsonPath("$.engine", is("ELECTRIC")))
        .andExpect(jsonPath("$.model", is("MID_SIZE")))
        .andExpect(jsonPath("$.features[0]", is("CLIMATE_CONTROL")))
        .andExpect(jsonPath("$.features[1]", is("INFOTAINMENT_SYSTEM")));

    mockMvc.perform(put("/api/cars/1")
        .content(serialize(car(5, HYBRID, COMPACT, CLIMATE_CONTROL, LEATHER_SEATS)))
        .contentType(MediaTypes.HAL_JSON)).andExpect(status().isNoContent());

    mockMvc.perform(get("/api/cars/1"))
        .andExpect(jsonPath("$.id", is(1)))
        .andExpect(jsonPath("$.position", is(5)))
        .andExpect(jsonPath("$.engine", is("HYBRID")))
        .andExpect(jsonPath("$.model", is("COMPACT")))
        .andExpect(jsonPath("$.features[0]", is("CLIMATE_CONTROL")))
        .andExpect(jsonPath("$.features[1]", is("LEATHER_SEATS")));
  }

  @Test
  public void testDeleteCar() throws Exception {
    mockMvc.perform(delete("/api/cars/1")).andExpect(status().isNotFound());

    testee.createCar(car(3, ELECTRIC, MID_SIZE, CLIMATE_CONTROL, INFOTAINMENT_SYSTEM));

    mockMvc.perform(delete("/api/cars/1")).andExpect(status().isNoContent());
    mockMvc.perform(delete("/api/cars/1")).andExpect(status().isNotFound());
  }

}
