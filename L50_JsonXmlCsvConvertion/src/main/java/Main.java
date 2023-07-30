import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.opencsv.CSVReader;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        List<Employee> employeeList;
        /// 1. CSV -> JSON
        String[] positionColumnCsvMap = {"id", "firstName", "lastName", "country", "age"};
        employeeList = parseCsv("data.csv", positionColumnCsvMap);
        writeJsonToFile(employeeList, "data.json");

        /// 2. XML -> JSON
        employeeList = parseXml("data.xml");
        writeJsonToFile(employeeList, "data2.json");
    }

    private static List<Employee> parseXml(String inputXmlFileName) {
        return null;
    }

    private static void writeJsonToFile(List<Employee> employeeList, String outJsonFileName) {

        if (employeeList == null) {
            System.out.println("Empty result of parsing csv file");
            return;
        }
        String employeeJson = listToJson(employeeList);
        System.out.println(employeeJson);
        if (writeString(outJsonFileName, employeeJson)) {
            System.out.println("Write employee list to data.json successful");
        } else {
            System.out.println("CSV Json write error");
        }
    }

    private static boolean writeString(String outJsonFile, String employeeJson) {
        try (FileWriter writer = new FileWriter(outJsonFile)) {
            writer.write(employeeJson);
            return true;
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    private static String listToJson(List<Employee> employees) {
        // return new GsonBuilder().create().toJson(employees);
        Gson gson = new GsonBuilder().create();
        return gson.toJson(employees);
    }

    private static List<Employee> parseCsv(String inputCsvFileName, String[] positionColumnCsvMap) {
        ColumnPositionMappingStrategy<Employee> mappingStrategy = new ColumnPositionMappingStrategy<>();
        mappingStrategy.setType(Employee.class);
        mappingStrategy.setColumnMapping(positionColumnCsvMap);

        try (CSVReader csvReader = new CSVReader(new FileReader(inputCsvFileName))) {
            CsvToBean<Employee> csvToEmployee = new CsvToBeanBuilder<Employee>(csvReader)
                    .withMappingStrategy(mappingStrategy)
                    .build();

            return csvToEmployee.parse();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        return null;
    }
}
