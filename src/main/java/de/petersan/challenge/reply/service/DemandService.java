package de.petersan.challenge.reply.service;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import de.petersan.challenge.reply.domain.Demand;
import de.petersan.challenge.reply.domain.User;
import de.petersan.challenge.reply.exception.InconsistentDataException;
import de.petersan.challenge.reply.exception.ResourceNotExistingException;

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

@RestController
@RequestMapping("/api")
public class DemandService {
  private final Map<Integer, Demand> demands = new HashMap<>();
  private final Map<String, User> users = new HashMap<>();

  @GetMapping("/users/{username}")
  public User getUser(@PathVariable String username) {
    return findUser(username);
  }

  private User findUser(String username) {
    if (!users.containsKey(username)) {
      throw new ResourceNotExistingException();
    }
    return users.get(username);
  }

  @ResponseStatus(HttpStatus.NO_CONTENT)
  @PutMapping("/users/{username}")
  public void updateUser(@PathVariable String username, @RequestBody User user) {
    user.setUsername(username);
    users.put(username, user);
  }

  @ResponseStatus(HttpStatus.NO_CONTENT)
  @DeleteMapping("/users/{username}")
  public void deleteUser(@PathVariable String username) {
    findUser(username);

    if (demands.values().stream().map(Demand::getUsername).anyMatch(username::equals)) {
      throw new InconsistentDataException(username + " can't be deleted");
    }

    users.remove(username);
  }

  @GetMapping("/demands/{id}")
  public Demand getDemand(@PathVariable Integer id) {
    return findDemand(id);
  }

  @GetMapping("/api/demands")
  public Collection<Demand> getAllDemands() {
    return demands.values();
  }

  @PreDestroy
  public void cleanUp() {
    users.clear();
    demands.clear();
  }

  @PostMapping("/demands")
  public ResponseEntity<?> createDemand(@RequestBody Demand demand) {

    if (demand.getId() != null) {
      updateDemand(demand.getId(), demand);
      return ResponseEntity.noContent().build();
    } else {
      int key = getNextKey(demands.keySet());
      demand.setId(key);

      if (!users.containsKey(demand.getUsername())) {
        throw new ResourceNotExistingException(demand.getUsername() + " is unknown");
      }

      demands.put(key, demand);

      return ResponseEntity.created(linkTo(methodOn(DemandService.class).getDemand(key)).toUri()).build();
    }
  }

  @ResponseStatus(HttpStatus.NO_CONTENT)
  @PutMapping("/demands/{id}")
  public void updateDemand(@PathVariable Integer id, @RequestBody Demand demand) {
    demand.setId(id);
    demands.put(id, demand);
  }

  private Demand findDemand(int id) {
    if (!demands.containsKey(id)) {
      throw new ResourceNotExistingException();
    }
    return demands.get(id);
  }

  private int getNextKey(Set<Integer> keys) {
    return keys.stream().max(Integer::compareTo).orElse(0) + 1;
  }

  @ResponseStatus(HttpStatus.NO_CONTENT)
  @DeleteMapping("/demands/{id}")
  public void deleteDemand(@PathVariable Integer id) {
    findDemand(id);
    demands.remove(id);
  }


}
