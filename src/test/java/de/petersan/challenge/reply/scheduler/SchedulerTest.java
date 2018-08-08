package de.petersan.challenge.reply.scheduler;

import static de.petersan.challenge.reply.scheduler.TestFixture.car;
import static de.petersan.challenge.reply.scheduler.TestFixture.demand;
import static de.petersan.challenge.reply.scheduler.TestFixture.timePos;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.contains;
import static org.junit.Assert.assertThat;

import de.petersan.challenge.reply.domain.Demand;

import org.hamcrest.Matcher;
import org.junit.Test;

import java.util.Arrays;
import java.util.stream.Collectors;

public class SchedulerTest {

  @Test
  public void testSimpleSchedule() throws Exception {
    Scheduler scheduler = new Scheduler(
        Arrays.asList(car(1, 0)),
        Arrays.asList(
            demand(1, timePos(0, 0), timePos(2, 2)),
            demand(2, timePos(3, 2), timePos(4, 2)),
            demand(3, timePos(4, 2), timePos(6, 1))));

    assertThat(scheduler.schedule(), is(true));
    assertThat(scheduler.getDemands(1), hasDemands(1, 2, 3));
  }

  @Test
  public void testSchedule() throws Exception {
    Scheduler scheduler = new Scheduler(
        Arrays.asList(
            car(1, 0),
            car(2, 1),
            car(3, 2)),
        Arrays.asList(
            demand(1, timePos(0, 0), timePos(2, 2)),
            demand(2, timePos(3, 2), timePos(4, 2)),
            demand(3, timePos(4, 2), timePos(6, 1)),

            demand(4, timePos(1, 1), timePos(2, 1)),
            demand(5, timePos(2, 1), timePos(4, 0))));

    assertThat(scheduler.schedule(), is(true));
    assertThat(scheduler.getDemands(1), hasDemands(1, 2, 3));
    assertThat(scheduler.getDemands(2), hasDemands(4, 5));
  }

  @Test
  public void testSchedule1() throws Exception {
    Scheduler scheduler = new Scheduler(
        Arrays.asList(
            car(1, 1),
            car(2, 7),
            car(3, 0),
            car(4, 4)),
        Arrays.asList(
            demand(1, timePos(2, 1), timePos(4, 5)),
            demand(2, timePos(3, 7), timePos(5, 5)),
            demand(3, timePos(2, 0), timePos(4, 5)),
            demand(4, timePos(3, 4), timePos(5, 5)),
            demand(5, timePos(5, 5), timePos(7, 0)),
            demand(6, timePos(5, 5), timePos(7, 0)),
            demand(7, timePos(6, 5), timePos(7, 1)),
            demand(8, timePos(7, 1), timePos(8, 6))));

    assertThat(scheduler.schedule(), is(true));
    assertThat(scheduler.getDemands(1), hasDemands(1, 5));
    assertThat(scheduler.getDemands(2), hasDemands(2, 6));
    assertThat(scheduler.getDemands(3), hasDemands(3, 7, 8));
    assertThat(scheduler.getDemands(4), hasDemands(4));
  }

  private Matcher<Iterable<? extends Demand>> hasDemands(int... demandIds) {
    return contains(Arrays.stream(demandIds).boxed().map(this::hasDemand).collect(Collectors.toList()));
  }

  private Matcher<Demand> hasDemand(int id) {
    return ChildElementMatcher.hasChild(Demand::getId, is(id));
  }

}
