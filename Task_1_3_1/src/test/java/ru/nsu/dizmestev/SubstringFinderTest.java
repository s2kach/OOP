package ru.nsu.dizmestev;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class SubstringFinderTest {

    @TempDir
    File tempDir;
    private File testFile;

    @BeforeEach
    void setUp() throws IOException {
        testFile = new File(tempDir, "test.txt");
        String content = "абракадабра";
        try (FileOutputStream fos = new FileOutputStream(testFile)) {
            fos.write(content.getBytes(StandardCharsets.UTF_8));
        }
    }

    @Test
    void testFindMultipleOccurrences() throws TaskException {
        SubstringFinder finder = new SubstringFinder();
        List<Long> result = finder.find(testFile.getAbsolutePath(), "бра");
        assertEquals(List.of(1L, 8L), result);
    }

    @Test
    void testFindNoOccurrences() throws TaskException {
        SubstringFinder finder = new SubstringFinder();
        List<Long> result = finder.find(testFile.getAbsolutePath(), "нет");
        assertTrue(result.isEmpty());
    }

    @Test
    void testEmptyTarget() {
        SubstringFinder finder = new SubstringFinder();
        TaskException exception = assertThrows(TaskException.class,
                () -> finder.find(testFile.getAbsolutePath(), ""));
        assertEquals("Пустая подстрока не допускается", exception.getMessage());
    }

    @Test
    void testFindSingleCharacter() throws TaskException {
        SubstringFinder finder = new SubstringFinder();
        List<Long> result = finder.find(testFile.getAbsolutePath(), "а");
        assertEquals(List.of(0L, 3L, 5L, 7L, 10L), result);
    }

    @Test
    void testFindOverlappingOccurrences() throws TaskException, IOException {
        File file = new File(tempDir, "overlap.txt");
        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write("aaa".getBytes(StandardCharsets.UTF_8));
        }

        SubstringFinder finder = new SubstringFinder();
        List<Long> result = finder.find(file.getAbsolutePath(), "aa");
        assertEquals(List.of(0L, 1L), result);
    }

    @Test
    void testLargeFile() throws TaskException, IOException {
        File largeFile = new File(tempDir, "large_test.txt");

        String filler = "x".repeat(100 * 1024 * 1024); // 100MB
        String target = "target";
        String largeContent = filler + target;

        try (FileOutputStream fos = new FileOutputStream(largeFile)) {
            fos.write(largeContent.getBytes(StandardCharsets.UTF_8));
        }

        SubstringFinder finder = new SubstringFinder();
        List<Long> result = finder.find(largeFile.getAbsolutePath(), target);

        assertEquals(List.of(104857600L), result);
    }
}