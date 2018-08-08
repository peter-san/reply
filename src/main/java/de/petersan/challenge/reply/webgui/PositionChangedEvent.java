package de.petersan.challenge.reply.webgui;

public class PositionChangedEvent {
  private final int carId;
  private final int lastPosition;
  private final int currentPosition;

  public PositionChangedEvent(int carId, int lastPosition, int currentPosition) {
    super();
    this.carId = carId;
    this.currentPosition = currentPosition;
    this.lastPosition = lastPosition;
  }

  public int getCarId() {
    return carId;
  }

  public int getCurrentPosition() {
    return currentPosition;
  }

  public int getLastPosition() {
    return lastPosition;
  }

}
