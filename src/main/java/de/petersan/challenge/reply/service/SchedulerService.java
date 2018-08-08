package de.petersan.challenge.reply.service;

import de.petersan.challenge.reply.domain.Car;
import de.petersan.challenge.reply.domain.Demand;
import de.petersan.challenge.reply.exception.InconsistentDataException;
import de.petersan.challenge.reply.scheduler.Scheduler;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.inject.Inject;

@RestController
@RequestMapping("/api/scheduler")
public class SchedulerService {

  @Inject
  private CarService carService;

  @Inject
  private DemandService demandService;

  @GetMapping
  public Callable<Map<Car, List<Demand>>> getSchedule() {
    return this::schedule;
    // return schedule();
  }

  private Map<Car, List<Demand>> schedule() {
    List<Car> cars = new ArrayList<>(carService.getAllCars());

    Scheduler scheduler = new Scheduler(cars, new ArrayList<>(demandService.getAllDemands()));

    boolean success = scheduler.schedule();

    if (!success) {
      throw new InconsistentDataException("current demands can't be scheduled");
    }

    return cars.stream().collect(Collectors.toMap(Function.identity(), c -> scheduler.getDemands(c.getId())));
  }
}
