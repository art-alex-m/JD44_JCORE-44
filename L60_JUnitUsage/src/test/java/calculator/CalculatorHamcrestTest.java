package calculator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.concurrent.Callable;
import java.util.stream.Stream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static utils.Decorator.exceptionOf;
import static utils.ThrowsWithMatcher.throwsWith;

/**
 * Тестовый класс с использованием Hamcrest
 * <p>
 * <a href="https://hamcrest.org/JavaHamcrest/tutorial">Hamcrest Tutorial</a>
 * <a href="https://www.baeldung.com/java-junit-hamcrest-guide">Testing with Hamcrest</a>
 * </p>
 */
class CalculatorHamcrestTest {
    private Calculator sut;

    @BeforeEach
    void setUp() {
        sut = new Calculator();
    }

    @Test
    public void testInstance() {
        Calculator result = Calculator.instance.get();

        assertThat(result, notNullValue());
    }

    @ParameterizedTest
    @MethodSource("testPlusDataProvider")
    public void testPlus(int a, int b, int expected) {
        int result = sut.plus.apply(a, b);

        assertThat(result, equalTo(expected));
    }

    @ParameterizedTest
    @MethodSource
    public void testAbs(int a, int expected) {
        int result = sut.abs.apply(a);

        assertThat(result, equalTo(expected));
    }

    @ParameterizedTest
    @CsvFileSource(resources = "testIsPositiveTestResource.csv")
    public void testIsPositiveTest(int a, boolean expected) {
        boolean result = sut.isPositive.test(a);

        assertThat(result, equalTo(expected));
    }

    @Test
    public void testDivideByZero() {
        int a = 1;
        int b = 0;

        Callable<?> result = () -> sut.divide.apply(a, b);

        /// Вариант из https://stackoverflow.com/questions/27724660/how-to-use-hamcrest-in-java-to-test-for-a-exception
        assertThat(exceptionOf(result), instanceOf(ArithmeticException.class));
        /// Matcher расширение Hamcrest
        assertThat(result, throwsWith(instanceOf(ArithmeticException.class)));
    }

    public static Stream<Arguments> testAbs() {
        return Stream.of(
                Arguments.of(0, 0),
                Arguments.of(-1, 1),
                Arguments.of(1, 1)
        );
    }

    public static Stream<Arguments> testPlusDataProvider() {
        return Stream.of(
                Arguments.of(0, 0, 0),
                Arguments.of(-1, 1, 0),
                Arguments.of(1, 1, 2)
        );
    }
}