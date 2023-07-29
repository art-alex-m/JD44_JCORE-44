package logger;

public enum Level {
    INFO("INFO"),
    ERROR("ERROR");

    private final String level;

    Level(String level) {
        this.level = level;
    }

    public String getLevel() {
        return this.level;
    }
}
