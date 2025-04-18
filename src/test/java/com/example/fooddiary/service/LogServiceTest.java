package com.example.fooddiary.service;

import com.example.fooddiary.exception.InvalidInputException;
import com.example.fooddiary.exception.NotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.Resource;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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

    @Test
    void validateLogFileExists_fileExists_doesNotThrow() throws IOException {
        Path tempFile = Files.createTempFile("validate", ".log");
        assertDoesNotThrow(() -> logService.validateLogFileExists(tempFile));
        Files.deleteIfExists(tempFile);
    }

    @Test
    void filterAndWriteLogsToTempFile_noMatchingLogs_createsEmptyFile() throws IOException {
        Path sourceFile = Files.createTempFile("source", ".log");
        Path tempFile = Files.createTempFile("temp", ".log");

        Files.writeString(sourceFile, "16-01-2023 Test log message");

        assertDoesNotThrow(() ->
                logService.filterAndWriteLogsToTempFile(sourceFile, "15-01-2023", tempFile));

        assertEquals(0, Files.size(tempFile));

        Files.deleteIfExists(sourceFile);
        Files.deleteIfExists(tempFile);
    }

    @Test
    void filterAndWriteLogsToTempFile_multipleLogs_filtersCorrectly() throws IOException {
        Path sourceFile = Files.createTempFile("source", ".log");
        Path tempFile = Files.createTempFile("temp", ".log");

        String logContent = """
            15-01-2023 Log 1
            16-01-2023 Log 2
            15-01-2023 Log 3
            17-01-2023 Log 4
            """;
        Files.writeString(sourceFile, logContent);

        logService.filterAndWriteLogsToTempFile(sourceFile, "15-01-2023", tempFile);

        String content = Files.readString(tempFile);
        assertTrue(content.contains("15-01-2023 Log 1"));
        assertTrue(content.contains("15-01-2023 Log 3"));
        assertFalse(content.contains("16-01-2023 Log 2"));
        assertFalse(content.contains("17-01-2023 Log 4"));

        Files.deleteIfExists(sourceFile);
        Files.deleteIfExists(tempFile);
    }

    @Test
    void downloadLogs_validDateButNoLogs_throwsNotFoundException() throws IOException {
        Path tempLogFile = Files.createTempFile("app", ".log");
        Files.writeString(tempLogFile, "16-01-2023 Test log");

        LogService service = new LogService() {
            @Override
            public void validateLogFileExists(Path path) {
                // this method is empty
            }
        };

        assertThrows(NotFoundException.class,
                () -> service.downloadLogs("15-01-2023"));

        Files.deleteIfExists(tempLogFile);
    }

    @Test
    void parseDate_emptyInput_throwsInvalidInputException() {
        assertThrows(InvalidInputException.class,
                () -> logService.parseDate(""));
    }

    @Test
    void parseDate_invalidDate_throwsInvalidInputException() {
        assertThrows(InvalidInputException.class,
                () -> logService.parseDate("32-01-2023")); // Invalid day
    }

    @Test
    void filterAndWriteLogsToTempFile_ioException_throwsIllegalStateException() throws IOException {
        Path sourceFile = Files.createTempFile("source", ".log");
        Path tempFile = Files.createTempFile("temp", ".log");
        Files.writeString(sourceFile, "15-01-2023 Test log");

        // Мокаем Files.newBufferedReader чтобы выбросить IOException
        try (MockedStatic<Files> mockedFiles = Mockito.mockStatic(Files.class)) {
            mockedFiles.when(() -> Files.newBufferedReader(sourceFile))
                    .thenThrow(new IOException("Test IOException"));

            assertThrows(IllegalStateException.class,
                    () -> logService.filterAndWriteLogsToTempFile(sourceFile, "15-01-2023", tempFile));
        } finally {
            Files.deleteIfExists(sourceFile);
            Files.deleteIfExists(tempFile);
        }
    }

    @Test
    void filterAndWriteLogsToTempFile_writeException_throwsIllegalStateException() throws IOException {
        Path sourceFile = Files.createTempFile("source", ".log");
        Path tempFile = Paths.get("/invalid/path/temp.log"); // Невалидный путь

        Files.writeString(sourceFile, "15-01-2023 Test log");

        assertThrows(IllegalStateException.class,
                () -> logService.filterAndWriteLogsToTempFile(sourceFile, "15-01-2023", tempFile));

        Files.deleteIfExists(sourceFile);
    }

    @Test
    void createResourceFromTempFile_ioException_throwsIllegalStateException() throws IOException {
        Path tempFile = Files.createTempFile("test", ".log");
        Files.writeString(tempFile, "Test content");

        try (MockedStatic<Files> mockedFiles = Mockito.mockStatic(Files.class)) {
            mockedFiles.when(() -> Files.size(tempFile))
                    .thenThrow(new IOException("Test IOException"));

            assertThrows(IllegalStateException.class,
                    () -> logService.createResourceFromTempFile(tempFile, "15-01-2023"));
        } finally {
            Files.deleteIfExists(tempFile);
        }
    }

}