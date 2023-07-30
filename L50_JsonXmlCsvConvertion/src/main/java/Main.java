import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
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
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        List<Employee> employeeList;
        /// 1. CSV -> JSON
        String[] positionColumnCsvMap = {"id", "firstName", "lastName", "country", "age"};
        employeeList = parseCsv("data.csv", positionColumnCsvMap);
        createAndWriteJsonToFile(employeeList, "data.json");

        /// 2. XML -> JSON
        employeeList = parseXml("data.xml");
        createAndWriteJsonToFile(employeeList, "data2.json");
    }

    private static void createAndWriteJsonToFile(List<Employee> employeeList, String outJsonFileName) {

        if (employeeList == null) {
            System.out.println("Empty result of parsing resource file");
            return;
        }
        String employeeJson = listToJson(employeeList);
        System.out.println(employeeJson);
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

    private static List<Employee> parseXml(String inputXmlFileName) {
        try {
            DocumentBuilder xmlBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document doc = xmlBuilder.parse(inputXmlFileName);

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
            if (fieldNode.getNodeType() == Element.ELEMENT_NODE) {
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
        }
        return employee;
    }
}
