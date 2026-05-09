package ru.nsu.dizmestev;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

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

    @Test
    void testStyleParserWithRealXml(@TempDir Path tempDir) throws IOException {
        File reportDir = tempDir.resolve("build/reports/checkstyle").toFile();
        reportDir.mkdirs();
        File reportFile = new File(reportDir, "main.xml");

        try (FileWriter writer = new FileWriter(reportFile)) {
            writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
                    + "<checkstyle version=\"8.42\">\n"
                    + "  <file name=\"Main.java\">\n"
                    + "    <error line=\"1\" severity=\"error\" message=\"msg\" />\n"
                    + "    <error line=\"2\" severity=\"error\" message=\"msg\" />\n"
                    + "  </file>\n"
                    + "</checkstyle>");
        }

        StyleParser parser = new StyleParser();
        int errors = parser.countStyleErrors(tempDir.toFile());
        assertEquals(2, errors);
    }

    @Test
    void testTestParserWithRealXml(@TempDir Path tempDir) throws Exception {
        File testResultsDir = tempDir.resolve("build/test-results/test").toFile();
        testResultsDir.mkdirs();
        File xmlFile = new File(testResultsDir, "TEST-test.xml");

        try (FileWriter writer = new FileWriter(xmlFile)) {
            writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
                    + "<testsuite tests=\"10\" failures=\"1\" errors=\"1\" skipped=\"1\">\n"
                    + "</testsuite>");
        }

        TestParser parser = new TestParser();
        TestResult result = parser.parse(tempDir.toFile());

        assertNotNull(result);
        assertEquals("7/2/1", result.toString());
    }
}