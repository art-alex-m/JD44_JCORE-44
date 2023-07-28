import logger.Logger;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class Main {
    private static final String BASE_PATH = "./Games/";
    private static final Logger logger = new Logger(new File(BASE_PATH + "temp", "temp.txt"));

    public static void main(String[] args) {
        List<String> dirs = List.of(
                "src/main",
                "src/test",
                "res/drawables",
                "res/vectors",
                "res/icons",
                "savegames",
                "temp"
        );
        dirs.forEach(Main::makeDirectory);

        List<Pair<String, String>> files = List.of(
                new Pair<>("src/main", "Main.java"),
                new Pair<>("src/main", "Utils.java"),
                new Pair<>("temp", "temp.txt")
        );
        files.forEach(Main::makeFile);

        logger.flush();
        System.out.println("Готово!");
    }

    public static void makeDirectory(String dirName) {
        File dir = new File(BASE_PATH + dirName);
        if (dir.mkdirs()) {
            logger.info("Directory was created: " + dir);
        } else {
            logger.error("Directory creation error: " + dir);
        }
    }

    public static void makeFile(Pair<String, String> fileName) {
        File file = new File(BASE_PATH + fileName.key(), fileName.value());
        try {
            if (file.createNewFile()) {
                logger.info("File was created: " + file);
            } else {
                logger.error("File creation error: " + file);
            }
        } catch (IOException ex) {
            logger.error(ex.getMessage());
        }
    }
}