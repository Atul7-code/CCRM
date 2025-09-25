package edu.ccrm.config;
import java.nio.file.Path;
import java.nio.file.Paths;
public class AppConfig {
    private static final AppConfig INSTANCE = new AppConfig();
    private final Path dataDirectory;
    private final Path backupDirectory;
    private AppConfig() {
        this.dataDirectory = Paths.get("data");
        this.backupDirectory = Paths.get("backups");
    }
    public static AppConfig getInstance() {
        return INSTANCE;
    }
    public Path getDataDirectory() {
        return dataDirectory;
    }

    public Path getBackupDirectory() {
        return backupDirectory;
    }
}