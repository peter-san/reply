package de.petersan.challenge.reply.domain;

/** TimePosition encapsulates time point and location. Both are represented with simple integers. */
public class TimePosition {
  private int position;
  private int time;

  public int getPosition() {
    return position;
  }

  public void setPosition(int position) {
    this.position = position;
  }

  public int getTime() {
    return time;
  }

  public void setTime(int time) {
    this.time = time;
  }

  @Override
  public String toString() {
    return String.format("(%s,%s)", time, position);
  }
}
