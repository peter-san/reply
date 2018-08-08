package de.petersan.challenge.reply.domain;

import java.util.ArrayList;
import java.util.List;

public class Car {
  private Integer id;
  private Model model;
  private EngineType engine;
  private List<Feature> features = new ArrayList<>();
  private int position;

  public int getPosition() {
    return position;
  }

  public void setPosition(int currentPosition) {
    this.position = currentPosition;
  }

  public void setFeatures(List<Feature> features) {
    this.features = features;
  }

  public List<Feature> getFeatures() {
    return features;
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public Model getModel() {
    return model;
  }

  public void setModel(Model model) {
    this.model = model;
  }

  public EngineType getEngine() {
    return engine;
  }

  public void setEngine(EngineType engine) {
    this.engine = engine;
  }
}
