package de.petersan.challenge.reply.simulator;

import de.petersan.challenge.reply.domain.Car;
import de.petersan.challenge.reply.domain.EngineType;
import de.petersan.challenge.reply.domain.Feature;
import de.petersan.challenge.reply.domain.Model;
import de.petersan.challenge.reply.service.CarService;

import org.apache.commons.lang3.Validate;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.Optional;
import java.util.stream.IntStream;

import javax.annotation.PostConstruct;

/** Simulator creates several cars and moves them regularly. */
@Configuration
@EnableScheduling
@ConditionalOnProperty(value = "simulator.active", havingValue = "true")
public class Simulator {
  public static final int FIELD_SIZE = 100;

  private CarService carService;

  public Simulator(CarService carService) {
    this.carService = Validate.notNull(carService);
  }

  @PostConstruct
  public void create() {
    IntStream.range(0, 7).forEach(i -> carService.createCar(createCar(i)));
  }

  @Scheduled(fixedRate = 500)
  private void move() {
    for (Car car : carService.getAllCars()) {
      nextPosition(car.getPosition()).ifPresent(p -> carService.updatePosition(car.getId(), p));
    }
  }

  private Optional<Integer> nextPosition(int currentPosition) {
    int toReturn = currentPosition + (int) Math.round(10 * (Math.random() - 0.5));

    if (toReturn < 0 || toReturn >= FIELD_SIZE || toReturn == currentPosition) {
      return Optional.empty();
    } else {

      return Optional.of(toReturn);
    }
  }

  private Car createCar(int position) {
    Car car = new Car();

    car.setPosition(FIELD_SIZE / 2);

    car.setEngine(EngineType.ELECTRIC);
    car.setModel(Model.MID_SIZE);
    car.getFeatures().add(Feature.NAVIGATION_SYSTEM);
    return car;

  }
}
