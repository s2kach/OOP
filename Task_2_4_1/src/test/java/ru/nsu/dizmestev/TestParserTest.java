package ru.nsu.dizmestev;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.File;
import org.junit.jupiter.api.Test;

class TestParserTest {

    @Test
    void testTestParserWithMissingDir() throws Exception {
        TestParser parser = new TestParser();
        File nonExistent = new File("non_existent_path_123");

        TestResult result = parser.parse(nonExistent);
        assertNotNull(result);
        assertEquals("0/0/0", result.toString());
    }

    @Test
    void testStyleParserWithMissingDir() {
        StyleParser parser = new StyleParser();
        File nonExistent = new File("non_existent_path_123");

        int errors = parser.countStyleErrors(nonExistent);
        assertEquals(0, errors);
    }
}