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
    void testFindMultipleOccurrences() throws SearchException {
        SubstringFinder finder = new SubstringFinder();
        List<Integer> result = finder.find(testFile.getAbsolutePath(), "бра");
        assertEquals(List.of(1, 8), result);
    }

    @Test
    void testFindNoOccurrences() throws SearchException {
        SubstringFinder finder = new SubstringFinder();
        List<Integer> result = finder.find(testFile.getAbsolutePath(), "нет");
        assertTrue(result.isEmpty());
    }

    @Test
    void testEmptyTarget() {
        SubstringFinder finder = new SubstringFinder();
        SearchException exception = assertThrows(SearchException.class,
                () -> finder.find(testFile.getAbsolutePath(), ""));
        assertEquals("Строка поиска не может быть пустой.", exception.getMessage());
    }

    @Test
    void testNullTarget() {
        SubstringFinder finder = new SubstringFinder();
        SearchException exception = assertThrows(SearchException.class,
                () -> finder.find(testFile.getAbsolutePath(), null));
        assertEquals("Строка поиска не может быть пустой.", exception.getMessage());
    }

    @Test
    void testFileNotFound() {
        SubstringFinder finder = new SubstringFinder();
        assertThrows(SearchException.class, () -> finder.find("nonexistent.txt", "бра"));
    }
}