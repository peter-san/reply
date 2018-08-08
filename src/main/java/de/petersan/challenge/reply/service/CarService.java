package de.petersan.challenge.reply.service;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import de.petersan.challenge.reply.domain.Car;
import de.petersan.challenge.reply.exception.ResourceNotExistingException;
import de.petersan.challenge.reply.webgui.PositionChangedEvent;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.annotation.PreDestroy;
import javax.inject.Inject;

/** Car service manages the cars. */
@RestController
@RequestMapping("/api/cars")
public class CarService {
  @Inject
  private ApplicationEventPublisher applicationEventPublisher;

  private final Map<Integer, Car> cars = new HashMap<>();

  @GetMapping("/{id}")
  public Car getCar(@PathVariable Integer id) {
    return findCar(id);
  }

  @GetMapping
  public Collection<Car> getAllCars() {
    return cars.values();
  }

  @PreDestroy
  public void cleanUp() {
    cars.clear();
  }

  @PostMapping
  public ResponseEntity<?> createCar(@RequestBody Car car) {
    if (car.getId() != null) {
      updateCar(car.getId(), car);
      return ResponseEntity.noContent().build();
    } else {
      int key = getNextKey(cars.keySet());
      car.setId(key);
      cars.put(key, car);

      return ResponseEntity.created(linkTo(methodOn(CarService.class).getCar(key)).toUri()).build();
    }
  }

  private Car findCar(int id) {
    if (!cars.containsKey(id)) {
      throw new ResourceNotExistingException();
    }
    return cars.get(id);
  }

  private int getNextKey(Set<Integer> keys) {
    return keys.stream().max(Integer::compareTo).orElse(0) + 1;
  }

  @ResponseStatus(HttpStatus.NO_CONTENT)
  @PutMapping("/{id}")
  public void updateCar(@PathVariable Integer id, @RequestBody Car car) {
    Car existing = findCar(id);
    existing.setEngine(car.getEngine());
    existing.setModel(car.getModel());
    existing.setFeatures(car.getFeatures());

    if (existing.getPosition() != car.getPosition()) {
      updatePosition(id, car.getPosition());
    }
  }

  @ResponseStatus(HttpStatus.NO_CONTENT)
  @DeleteMapping("/{id}")
  public void deleteCar(@PathVariable Integer id) {
    findCar(id);
    cars.remove(id);
  }

  @PutMapping("/{id}/position")
  public void updatePosition(@PathVariable Integer id, @RequestBody int position) {
    Car car = findCar(id);
    int lastPosition = car.getPosition();
    car.setPosition(position);
    applicationEventPublisher.publishEvent(new PositionChangedEvent(id, lastPosition, position));
  }

  @GetMapping("/{id}/position")
  public int getPosition(@PathVariable Integer id) {
    return findCar(id).getPosition();
  }

}
