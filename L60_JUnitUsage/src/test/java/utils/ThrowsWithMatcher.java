package utils;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import java.util.concurrent.Callable;

public class ThrowsWithMatcher extends TypeSafeMatcher<Callable<?>> {

    private final Matcher<?> nextMatcher;

    public ThrowsWithMatcher(Matcher<?> nextMatcher) {
        this.nextMatcher = nextMatcher;
    }

    @Override
    protected boolean matchesSafely(Callable<?> callable) {
        try {
            callable.call();
            return false;
        } catch (Throwable ex) {
            return nextMatcher.matches(ex);
        }
    }

    @Override
    public void describeTo(Description description) {

    }

    public static ThrowsWithMatcher throwsWith(Matcher<?> nextMatcher) {
        return new ThrowsWithMatcher(nextMatcher);
    }
}
