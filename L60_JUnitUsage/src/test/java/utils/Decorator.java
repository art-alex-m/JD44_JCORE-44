package utils;

import java.util.concurrent.Callable;

public class Decorator {
    public static Throwable exceptionOf(Callable<?> callable) {
        try {
            callable.call();
            return null;
        } catch (Throwable e) {
            return e;
        }
    }
}
