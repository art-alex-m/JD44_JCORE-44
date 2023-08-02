package calculator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class CalculatorTest {
    private Calculator sut;

    @BeforeEach
    void setUp() {
        sut = new Calculator();
    }

    @Test
    public void testInstance() {
        Calculator result = Calculator.instance.get();

        assertNotNull(result);
    }

    @ParameterizedTest
    @MethodSource("testPlusDataProvider")
    public void testPlus(int a, int b, int expected) {
        int result = sut.plus.apply(a, b);

        assertEquals(expected, result);
    }

    @ParameterizedTest
    @MethodSource
    public void testAbs(int a, int expected) {
        int result = sut.abs.apply(a);

        assertEquals(expected, result);
    }

    @ParameterizedTest
    @CsvFileSource(resources = "testIsPositiveTestResource.csv")
    public void testIsPositiveTest(int a, boolean expected) {
        boolean result = sut.isPositive.test(a);

        assertEquals(expected, result);
    }

    @Test
    public void testDivideByZero() {
        int a = 1;
        int b = 0;

        Executable result = () -> sut.divide.apply(a, b);

        assertThrows(ArithmeticException.class, result);
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