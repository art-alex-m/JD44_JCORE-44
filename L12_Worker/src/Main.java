public class Main {
    public static void main(String[] args) {
        Worker worker = new Worker(System.out::println, System.out::println);
        worker.start();
    }
}