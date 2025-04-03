package com.example.fooddiary.service;

import com.example.fooddiary.exception.InvalidInputException;
import com.example.fooddiary.exception.NotFoundException;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;

@Service
public class LogService {

    private static final String LOG_FILE_PATH = "log/app.log";

    public Resource downloadLogs(String date) {
        LocalDate logDate = parseDate(date);

        Path logFilePath = Paths.get(LOG_FILE_PATH);
        validateLogFileExists(logFilePath);

        String formattedDate = logDate.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));

        Path tempFile = createTempFile(logDate);
        filterAndWriteLogsToTempFile(logFilePath, formattedDate, tempFile);

        return createResourceFromTempFile(tempFile, date);
    }

    private LocalDate parseDate(String date) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            return LocalDate.parse(date, formatter);
        } catch (DateTimeParseException e) {
            throw new InvalidInputException("Неверный формат даты. Необходимый dd-MM-yyyy");
        }
    }

    private void validateLogFileExists(Path path) {
        if (!Files.exists(path)) {
            throw new NotFoundException("Файл не существует: " + LOG_FILE_PATH);
        }
    }

    private Path createTempFile(LocalDate logDate) {
        try {
            return Files.createTempFile("log-" + logDate, ".log");
        } catch (IOException e) {
            throw new IllegalStateException("Ошибка создания временного файла: " + e.getMessage());
        }
    }

    private void filterAndWriteLogsToTempFile(Path logFilePath, String formattedDate,
                                              Path tempFile) {
        try (BufferedReader reader = Files.newBufferedReader(logFilePath)) {
            Files.write(tempFile, reader.lines()
                    .filter(line -> line.contains(formattedDate))
                    .toList());
        } catch (IOException e) {
            throw new IllegalStateException("Ошибка обработки файла лога: " + e.getMessage());
        }
    }

    private Resource createResourceFromTempFile(Path tempFile, String date) {
        try {
            if (Files.size(tempFile) == 0) {
                throw new NotFoundException("Не обнаружено логов для указанной даты: " + date);
            }
            Resource resource = new UrlResource(tempFile.toUri());
            tempFile.toFile().deleteOnExit();
            return resource;
        } catch (IOException e) {
            throw new IllegalStateException("Ошибка создания из временного файла: "
                    + e.getMessage());
        }
    }
}