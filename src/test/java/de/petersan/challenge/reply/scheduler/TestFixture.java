package de.petersan.challenge.reply.scheduler;

import de.petersan.challenge.reply.domain.Car;
import de.petersan.challenge.reply.domain.Demand;
import de.petersan.challenge.reply.domain.TimePosition;

public class TestFixture {

  public static Car car(Integer carId, int position) {
    Car car = new Car();
    car.setId(carId);
    car.setPosition(position);
    return car;
  }

  public static TimePosition timePos(int time, int position) {
    TimePosition timePos = new TimePosition();
    timePos.setPosition(position);
    timePos.setTime(time);
    return timePos;
  }

  public static Demand demand(int id, TimePosition pickUp, TimePosition dropOff) {
    Demand demand = new Demand();
    demand.setId(id);
    demand.setPickUp(pickUp);
    demand.setDropOff(dropOff);
    return demand;
  }
}
