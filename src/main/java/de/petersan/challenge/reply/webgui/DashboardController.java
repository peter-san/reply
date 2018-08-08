package de.petersan.challenge.reply.webgui;

import de.petersan.challenge.reply.service.CarService;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import javax.inject.Inject;

/** DashboardController aggregating driven distances and provides them to the web ui. */
@Controller
public class DashboardController {
  @Inject
  private CarService carService;

  @Inject
  private SimpMessagingTemplate template;

  Map<Integer, Integer> aggregatedDistance = new HashMap<>();

  @RequestMapping("/")
  public String home() {
    return "redirect:dashboard";
  }

  @RequestMapping("/dashboard")
  public ModelAndView dashboard() {
    ModelAndView mav = new ModelAndView("dashboard.html");

    mav.addObject("cars", carService.getAllCars().stream()
        .map(car -> Pair.of(car, getAggregation(car.getId())))
        .collect(Collectors.toList()));


    return mav;
  }

  private int getAggregation(int carId) {
    return aggregatedDistance.getOrDefault(carId, 0);
  }

  @EventListener(PositionChangedEvent.class)
  public void onEvent(PositionChangedEvent event) {
    int current = getAggregation(event.getCarId());
    int aggregated = current + Math.abs(event.getCurrentPosition() - event.getLastPosition());
    aggregatedDistance.put(event.getCarId(), aggregated);

    template.convertAndSend("/topic/position-change",
        new Update(event.getCarId(), event.getCurrentPosition(), aggregated));
  }

  private static class Update {
    private final int carId;
    private final int currentPosition;
    private final int aggregatedValue;

    public Update(int carId, int currentPosition, int aggregatedValue) {
      super();
      this.carId = carId;
      this.currentPosition = currentPosition;
      this.aggregatedValue = aggregatedValue;
    }

    public int getCarId() {
      return carId;
    }

    public int getCurrentPosition() {
      return currentPosition;
    }

    public int getAggregatedValue() {
      return aggregatedValue;
    }
  }
}
