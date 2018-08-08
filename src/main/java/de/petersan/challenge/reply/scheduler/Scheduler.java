package de.petersan.challenge.reply.scheduler;

import de.petersan.challenge.reply.domain.Car;
import de.petersan.challenge.reply.domain.Demand;
import de.petersan.challenge.reply.domain.TimePosition;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;


public class Scheduler {

  private final Demand[] demands;

  private final List<Route> routes;

  public Scheduler(List<Car> cars, List<Demand> demands) {
    this.demands = demands.stream().sorted(this::compareDemands).toArray(Demand[]::new);
    this.routes = cars.stream().map(Route::new).collect(Collectors.toList());
  }

  public boolean schedule() {
    return step(0);
  }

  public List<Demand> getDemands(int carId) {
    return routes.stream().filter(r -> r.car.getId() == carId)
        .map(Route::getDemands).findFirst().orElseThrow(() -> new IllegalArgumentException("route not found!"));
  }

  private boolean step(int demandPosition) {
    if (demandPosition == demands.length) {
      return true;
    }

    Demand demand = demands[demandPosition];
    for (Route route : routes) {
      if (route.isSuitable(demand)) {
        route.demands.add(demand);

        if (step(++demandPosition)) {
          return true;
        }

        route.demands.remove(demand);
      }
    }
    return false;
  }

  private int compareDemands(Demand first, Demand second) {
    if (first.getPickUp().getTime() < second.getPickUp().getTime()) {
      return -1;
    }

    if (first.getPickUp().getTime() == second.getPickUp().getTime()) {
      return Integer.compare(first.getDropOff().getTime(), second.getDropOff().getTime());
    }

    return 1;
  }

  private static class Route {
    private final Car car;
    private LinkedList<Demand> demands = new LinkedList<>();

    public List<Demand> getDemands() {
      return demands;
    }

    private Route(Car car) {
      this.car = car;
    }

    private boolean isSuitable(Demand demand) {

      if (demand.getEngine() != car.getEngine()
          || demand.getModel() != car.getModel()
          || !car.getFeatures().containsAll(demand.getFeatures())) {
        return false;
      }

      if (demands.isEmpty()) {
        return car.getPosition() == demand.getPickUp().getPosition();
      }

      return isSuccessor(demands.getLast().getDropOff(), demand.getPickUp());
    }

    private boolean isSuccessor(TimePosition first, TimePosition second) {
      return first.getPosition() == second.getPosition()
          && first.getTime() <= second.getTime();
    }

    @Override
    public String toString() {
      return car.getId() + "<>" + demands;
    }
  }
}
