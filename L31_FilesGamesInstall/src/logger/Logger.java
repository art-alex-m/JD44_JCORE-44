package logger;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;

public class Logger {
    private final StringBuilder buffer;
    private final File resource;

    public Logger(File resource) {
        this.resource = resource;
        this.buffer = new StringBuilder();
    }

    public void log(String msg, Level level) {
        this.buffer.append(String.format("%-5s [%s] %s%n", LocalDateTime.now(), level.getLevel(), msg));
    }

    public void error(String msg) {
        this.log(msg, Level.ERROR);
    }

    public void info(String msg) {
        this.log(msg, Level.INFO);
    }

    public void flush() {
        try (FileOutputStream outputStream = new FileOutputStream(this.resource, true)) {
            outputStream.write(this.buffer.toString().getBytes());
            outputStream.flush();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
