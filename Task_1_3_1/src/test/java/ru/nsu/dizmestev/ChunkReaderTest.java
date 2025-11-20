package ru.nsu.dizmestev;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class ChunkReaderTest {

    @TempDir
    File tempDir;
    private File testFile;

    @BeforeEach
    void setUp() throws IOException {
        testFile = new File(tempDir, "test.txt");
        String content = "line1\nline2\nline3";
        try (FileOutputStream fos = new FileOutputStream(testFile)) {
            fos.write(content.getBytes(StandardCharsets.UTF_8));
        }
    }

    @Test
    void testReadChunk() throws TaskException, IOException {
        try (ChunkReader reader = new ChunkReader(testFile.getAbsolutePath(), 1024)) {
            byte[] chunk = reader.readChunk();
            String content = new String(chunk, StandardCharsets.UTF_8);
            assertEquals("line1\nline2\nline3", content);
        }
    }

    @Test
    void testReadEmptyFile() throws IOException, TaskException {
        File emptyFile = new File(tempDir, "empty.txt");
        emptyFile.createNewFile();

        try (ChunkReader reader = new ChunkReader(emptyFile.getAbsolutePath(), 1024)) {
            assertNull(reader.readChunk());
        }
    }

    @Test
    void testReadSmallChunks() throws TaskException, IOException {
        try (ChunkReader reader = new ChunkReader(testFile.getAbsolutePath(), 1)) {
            byte[] chunk1 = reader.readChunk();
            byte[] chunk2 = reader.readChunk();
            byte[] chunk3 = reader.readChunk();

            assertEquals('l', (char) chunk1[0]);
            assertEquals('i', (char) chunk2[0]);
            assertEquals('n', (char) chunk3[0]);
        }
    }

    @Test
    void testNonExistentFile() {
        TaskException exception = assertThrows(TaskException.class,
                () -> new ChunkReader("nonexistent.txt", 1024));
        assertEquals("Не удалось открыть файл: nonexistent.txt", exception.getMessage());
        assertNotNull(exception.getCause());
    }
}