import java.util.*;
import java.util.function.Predicate;

public class Main {
    public static void main(String[] args) {
        List<Person> persons = generatePopulation();

        // Найти количество несовершеннолетних (т.е. людей младше 18 лет)
        long minorCount = persons.stream().filter(p -> p.getAge() < 18).count();
        System.out.printf("Количество несовершенолетних: %d%n", minorCount);

        Predicate<Person> ageGte18 = p -> p.getAge() >= 18;

        // Получить список фамилий призывников (т.е. мужчин от 18 и до 27 лет)
        List<String> conscriptFamily = persons.stream()
                .filter(ageGte18)
                .filter(p -> p.getAge() < 27)
                .map(Person::getFamily)
                .toList(); // .collect(Collectors.toList())
        System.out.println(conscriptFamily);

        // Получить отсортированный по фамилии список потенциально работоспособных людей с высшим образованием
        // в выборке (т.е. людей с высшим образованием от 18 до 60 лет для женщин и до 65 лет для мужчин)
        List<Person> working = persons.stream()
                .filter(ageGte18)
                .filter(p -> p.getEducation() == Education.HIGHER)
                .filter(p -> p.getSex() == Sex.WOMAN && p.getAge() < 60 || p.getSex() == Sex.MAN && p.getAge() < 65)
                .sorted(Comparator.comparing(Person::getFamily))
                .toList();
        System.out.println(working);
    }

    public static List<Person> generatePopulation() {
        List<String> names = Arrays.asList("Jack", "Connor", "Harry", "George", "Samuel", "John");
        List<String> families = Arrays.asList("Evans", "Young", "Harris", "Wilson", "Davies", "Adamson", "Brown");
        List<Person> persons = new ArrayList<>();
        for (int i = 0; i < 100_000; i++) {
            persons.add(new Person(
                    names.get(new Random().nextInt(names.size())),
                    families.get(new Random().nextInt(families.size())),
                    new Random().nextInt(100),
                    Sex.values()[new Random().nextInt(Sex.values().length)],
                    Education.values()[new Random().nextInt(Education.values().length)])
            );
        }

        return persons;
    }
}