import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.opencsv.CSVReader;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Main {
    public static void main(String[] args) {
        List<Employee> employeeList;
        /// 1. CSV -> JSON
        String[] positionColumnCsvMap = {"id", "firstName", "lastName", "country", "age"};
        employeeList = parseCsv(getFileFromResources("/data/data.csv"), positionColumnCsvMap);
        createAndWriteJsonToFile(employeeList, "data1.json");

        /// 2. XML -> JSON
        employeeList = parseXml(getFileFromResources("/data/data.xml"));
        createAndWriteJsonToFile(employeeList, "data2.json");

        /// 3. JSON parser
        String json = readString("data1.json");
        Optional<List<Employee>> employees = jsonToList(json);
        employees.ifPresentOrElse(
                list -> list.forEach(System.out::println),
                () -> System.out.println("Empty result of parsing resource file")
        );
        System.out.println("Parsing from json is done");
    }

    private static InputStream getDataResource(String fileName) {
        try {
            return Main.class.getResourceAsStream(fileName);
        } catch (NullPointerException e) {
            throw new RuntimeException(e);
        }
    }

    private static File getFileFromResources(String resourceFileName) {
        File tempFile;
        try {
            tempFile = File.createTempFile("temp", null);
            tempFile.deleteOnExit();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try (FileOutputStream fileOutputStream = new FileOutputStream(tempFile)) {
            fileOutputStream.write(getDataResource(resourceFileName).readAllBytes());
            fileOutputStream.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return tempFile;
    }

    private static void createAndWriteJsonToFile(List<Employee> employeeList, String outJsonFileName) {

        if (employeeList == null) {
            System.out.println("Empty result of parsing resource file");
            return;
        }
        String employeeJson = listToJson(employeeList);
        System.out.println(employeeJson.replaceAll("[\\n\\s+]", ""));
        if (writeString(outJsonFileName, employeeJson)) {
            System.out.printf("Write employee list to %s successful%n", outJsonFileName);
        } else {
            System.out.println("Json write error");
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
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(employees);
    }

    private static List<Employee> parseCsv(File inputCsvFile, String[] positionColumnCsvMap) {
        ColumnPositionMappingStrategy<Employee> mappingStrategy = new ColumnPositionMappingStrategy<>();
        mappingStrategy.setType(Employee.class);
        mappingStrategy.setColumnMapping(positionColumnCsvMap);

        try (FileReader fileReader = new FileReader(inputCsvFile);
                CSVReader csvReader = new CSVReader(fileReader)) {
            CsvToBean<Employee> csvToEmployee = new CsvToBeanBuilder<Employee>(csvReader)
                    .withMappingStrategy(mappingStrategy)
                    .build();

            return csvToEmployee.parse();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static List<Employee> parseXml(File inputXmlFile) {
        try {
            DocumentBuilder xmlBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document doc = xmlBuilder.parse(inputXmlFile);

            NodeList staffList = doc.getElementsByTagName("employee");
            List<Employee> employeeList = new ArrayList<>(8);
            for (int i = 0; i < staffList.getLength(); i++) {
                Employee employee = createEmployeeFromXmlNode(staffList.item(i));
                employeeList.add(employee);
            }

            return employeeList;
        } catch (ParserConfigurationException | IOException | SAXException e) {
            System.out.println(e.getMessage());
        }

        return null;
    }

    private static Employee createEmployeeFromXmlNode(Node node) {
        Employee employee = new Employee();
        NodeList fieldsList = node.getChildNodes();
        for (int j = 0; j < fieldsList.getLength(); j++) {
            Node fieldNode = fieldsList.item(j);
            if (fieldNode.getNodeType() != Element.ELEMENT_NODE) {
                continue;
            }
            Element element = (Element) fieldNode;
            String context = element.getTextContent();
            switch (element.getTagName()) {
                case "id" -> employee.setId(Long.parseLong(context));
                case "firstName" -> employee.setFirstName(context);
                case "lastName" -> employee.setLastName(context);
                case "country" -> employee.setCountry(context);
                case "age" -> employee.setAge(Integer.parseInt(context));
            }
        }
        return employee;
    }

    private static String readString(String inputFileName) {

//        try (BufferedReader buffer = new BufferedReader(new FileReader(inputFileName))) {
//            return buffer.lines().reduce("", String::concat);
//        } catch (IOException e) {
//            System.out.println(e.getMessage());
//        }
//        return null;

        /// Такой вариант видиться компактнее для маленьких файлов
        try (FileInputStream inputStream = new FileInputStream(inputFileName)) {
            return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    private static Optional<List<Employee>> jsonToList(String json) {
        if (json == null || json.isEmpty()) {
            return Optional.empty();
        }

        Gson builder = new GsonBuilder().create();
        Type employeeListType = new TypeToken<List<Employee>>(){}.getType();
        List<Employee> employeeList = builder.fromJson(json, employeeListType);

        return Optional.of(employeeList);
    }
}
