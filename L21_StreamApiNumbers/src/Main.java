import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

/**
 * Решение задачи стандартными средствами Java
 */
public class Main {
    public static void main(String[] args) {
        List<Integer> numbers = Arrays.asList(1, 2, 5, 16, -1, -2, 0, 32, 3, 5, 8, 23, 4);
        PriorityQueue<Integer> sorted = new PriorityQueue<>(Comparator.naturalOrder());
        for (int current : numbers) {
            if (current > 0 && current % 2 == 0) {
                sorted.add(current);
            }
        }
        while (!sorted.isEmpty()) {
            System.out.println(sorted.poll());
        }
    }
}
