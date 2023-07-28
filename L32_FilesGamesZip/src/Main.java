import java.io.*;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class Main {
    private static final String BASE_PATH = "./Games/";
    private static final String SAVE_PATH = BASE_PATH + "savegames";

    public static void main(String[] args) {
        List<Pair<File, GameProgress>> gameProgresses = List.of(
                new Pair<>(
                        new File(SAVE_PATH, "progress1.dat"),
                        new GameProgress(20, 11, 1, 10.19)
                ),
                new Pair<>(
                        new File(SAVE_PATH, "progress2.dat"),
                        new GameProgress(40, 12, 5, 15.19)
                ),
                new Pair<>(
                        new File(SAVE_PATH, "progress3.dat"),
                        new GameProgress(60, 13, 9, 18.19)
                )
        );
        gameProgresses.forEach(Main::saveGame);

        File zipFile = new File(SAVE_PATH, "out.zip");
        zipFiles(zipFile, gameProgresses.stream().map(Pair::key).toList());

        gameProgresses.forEach((gamePair) -> gamePair.key().delete());
        System.out.println("Done!");
    }

    private static void saveGame(Pair<File, GameProgress> gameData) {
        try (FileOutputStream fileStream = new FileOutputStream(gameData.key());
             ObjectOutputStream outputStream = new ObjectOutputStream(fileStream);) {

            outputStream.writeObject(gameData.value());

        } catch (IOException e) {
            System.out.println(e.getMessage());
            ;
        }
    }

    private static void zipFiles(File outFile, List<File> gameFiles) {
        try (
                FileOutputStream file = new FileOutputStream(outFile);
                ZipOutputStream zip = new ZipOutputStream(file)) {

            for (File inFile : gameFiles) {
                FileInputStream inputStream = new FileInputStream(inFile);
                ZipEntry entry = new ZipEntry(inFile.getName());
                zip.putNextEntry(entry);
                zip.write(inputStream.readAllBytes());
                zip.closeEntry();
                inputStream.close();
            }

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}