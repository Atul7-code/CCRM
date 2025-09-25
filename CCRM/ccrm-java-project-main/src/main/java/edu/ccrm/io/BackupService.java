package edu.ccrm.io;

import edu.ccrm.config.AppConfig;
import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.Optional;
import java.util.stream.Stream;

public class BackupService {
    public void performBackup() {
        Path sourceDir = AppConfig.getInstance().getDataDirectory();
        if (!Files.exists(sourceDir)) {
            System.out.println("❌ Backup failed: The 'data' directory does not exist. Please export data first.");
            return;
        }
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"));
        Path backupDestination = AppConfig.getInstance().getBackupDirectory().resolve("backup-" + timestamp);
        try {
            Files.createDirectories(backupDestination);
            try (Stream<Path> paths = Files.walk(sourceDir)) {
                paths.filter(Files::isRegularFile)
                        .forEach(sourcePath -> {
                            try {
                                Path destinationPath = backupDestination.resolve(sourceDir.relativize(sourcePath));
                                Files.copy(sourcePath, destinationPath);
                            } catch (IOException e) {
                                System.err.println("Failed to copy file: " + sourcePath);
                            }
                        });
            }
            System.out.println("✅ Backup created successfully at: " + backupDestination);
        } catch (IOException e) {
            System.out.println("❌ Error creating backup: " + e.getMessage());
        }
    }

    public void displayLatestBackupSize() {
        try {
            Path backupRoot = AppConfig.getInstance().getBackupDirectory();
            Optional<Path> latestBackupDir = Files.list(backupRoot)
                    .filter(Files::isDirectory)
                    .max(Comparator.comparingLong(p -> p.toFile().lastModified()));

            if (latestBackupDir.isPresent()) {
                long totalSize = calculateDirectorySize(latestBackupDir.get());
                System.out.printf("Total size of latest backup (%s): %.2f KB\n",
                        latestBackupDir.get().getFileName(), (double) totalSize / 1024);
            } else {
                System.out.println("No backups found.");
            }
        } catch (IOException e) {
            System.out.println("❌ Error calculating backup size: " + e.getMessage());
        }
    }

    private long calculateDirectorySize(Path path) throws IOException {
        long size = 0;
        try (Stream<Path> walk = Files.walk(path)) {
            size = walk
                    .filter(Files::isRegularFile)
                    .mapToLong(p -> {
                        try {
                            return Files.size(p);
                        } catch (IOException e) {
                            return 0L;
                        }
                    })
                    .sum();
        }
        return size;
    }
}