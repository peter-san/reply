package de.petersan.challenge.reply.domain;

import java.util.ArrayList;
import java.util.List;

public class Demand {
  private Integer id;
  private List<Feature> features = new ArrayList<>();
  private EngineType engine;
  private Model model;
  private String username;
  private TimePosition pickUp;
  private TimePosition dropOff;

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public List<Feature> getFeatures() {
    return features;
  }

  public void setFeatures(List<Feature> features) {
    this.features = features;
  }

  public EngineType getEngine() {
    return engine;
  }

  public void setEngine(EngineType engine) {
    this.engine = engine;
  }

  public Model getModel() {
    return model;
  }

  public void setModel(Model model) {
    this.model = model;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public TimePosition getPickUp() {
    return pickUp;
  }

  public void setPickUp(TimePosition pickUp) {
    this.pickUp = pickUp;
  }

  public TimePosition getDropOff() {
    return dropOff;
  }

  public void setDropOff(TimePosition dropOff) {
    this.dropOff = dropOff;
  }

  @Override
  public String toString() {
    return String.format("%d<%s->%s>", id, pickUp, dropOff);
  }
}
