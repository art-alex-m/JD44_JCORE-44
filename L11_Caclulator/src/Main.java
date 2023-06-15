public class Main {
    public static void main(String[] args) {
        Calculator calc = Calculator.instance.get();

        int a = calc.plus.apply(1,2);
        int b = calc.minus.apply(1,1);

        // Код не работает, потому что происходит деление на 0
        // Можно обернуть в try/catch и отлавливать ArithmeticException.
        // Можно сделать предварительную, проверку, что b не равно нулю. Этот вариант работает быстрее
        if (b != 0) {
            int c = calc.devide.apply(a, b);
            calc.println.accept(c);
        } else {
            System.out.println("Деление на ноль!");
        }
    }
}