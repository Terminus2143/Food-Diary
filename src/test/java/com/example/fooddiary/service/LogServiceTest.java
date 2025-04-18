package com.example.fooddiary.service;

import com.example.fooddiary.exception.InvalidInputException;
import com.example.fooddiary.exception.NotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.Resource;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class LogServiceTest {

    @InjectMocks
    private LogService logService;

    @Test
    void parseDate_validDate_returnsLocalDate() {
        String date = "15-01-2023";
        LocalDate result = logService.parseDate(date);
        assertEquals(LocalDate.of(2023, 1, 15), result);
    }

    @Test
    void parseDate_invalidFormat_throwsInvalidInputException() {
        String date = "2023-01-15";
        assertThrows(InvalidInputException.class, () -> logService.parseDate(date));
    }

    @Test
    void createTempFile_success_returnsPath() throws IOException {
        Path tempFile = Files.createTempFile("test", ".log");
        tempFile.toFile().deleteOnExit();

        Path result = logService.createTempFile(LocalDate.now());
        assertNotNull(result);
        assertTrue(Files.exists(result));
    }

    @Test
    void filterAndWriteLogsToTempFile_success_writesFilteredLogs() throws IOException {
        Path sourceFile = Files.createTempFile("source", ".log");
        Path tempFile = Files.createTempFile("temp", ".log");

        String testLog = "15-01-2023 Test log message";
        Files.writeString(sourceFile, testLog);

        logService.filterAndWriteLogsToTempFile(sourceFile, "15-01-2023", tempFile);

        String content = Files.readString(tempFile);
        assertTrue(content.contains(testLog));

        Files.deleteIfExists(sourceFile);
        Files.deleteIfExists(tempFile);
    }

    @Test
    void createResourceFromTempFile_withLogs_returnsResource() throws IOException {
        Path tempFile = Files.createTempFile("test", ".log");
        Files.writeString(tempFile, "Test log content");

        Resource resource = logService.createResourceFromTempFile(tempFile, "15-01-2023");
        assertNotNull(resource);
        assertTrue(resource.exists());

        Files.deleteIfExists(tempFile);
    }

    @Test
    void createResourceFromTempFile_emptyFile_throwsNotFoundException() throws IOException {
        Path tempFile = Files.createTempFile("empty", ".log");

        assertThrows(NotFoundException.class,
                () -> logService.createResourceFromTempFile(tempFile, "15-01-2023"));

        Files.deleteIfExists(tempFile);
    }

    @Test
    void downloadLogs_invalidDate_throwsInvalidInputException() {
        assertThrows(InvalidInputException.class,
                () -> logService.downloadLogs("2023-01-15"));
    }
}