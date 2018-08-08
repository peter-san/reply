package de.petersan.challenge.reply.scheduler;

import org.apache.commons.lang3.Validate;
import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import java.util.function.Function;

public class ChildElementMatcher<T, K> extends TypeSafeMatcher<T> {
  private final Matcher<K> matcher;
  private final Function<T, K> getter;

  public ChildElementMatcher(Function<T, K> getter, Matcher<K> matcher) {
    this.getter = Validate.notNull(getter);
    this.matcher = Validate.notNull(matcher);
  }

  @Override
  public void describeTo(Description description) {
    description.appendText("el ").appendDescriptionOf(matcher);
  }

  @Override
  protected void describeMismatchSafely(T item, Description mismatchDescription) {
    matcher.describeMismatch(getter.apply(item), mismatchDescription);
  }

  @Factory
  public static <T, K> Matcher<T> hasChild(Function<T, K> getter, Matcher<K> matcher) {
    return new ChildElementMatcher<T, K>(getter, matcher);
  }

  @Override
  protected boolean matchesSafely(T item) {
    K childElement = getter.apply(item);
    boolean toReturn = matcher.matches(childElement);

    return toReturn;
  }
}
