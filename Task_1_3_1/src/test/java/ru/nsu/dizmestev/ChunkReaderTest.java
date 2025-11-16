package ru.nsu.dizmestev;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.BufferedInputStream;
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
    void testReadChunk() throws FileReadException, IOException {
        ChunkReader reader = new ChunkReader(testFile.getAbsolutePath(), 1024);
        try (BufferedInputStream stream = reader.openStream()) {
            String chunk = reader.readChunk(stream);
            assertEquals("line1\nline2\nline3", chunk);
        }
    }

    @Test
    void testReadEmptyFile() throws IOException, FileReadException {
        File emptyFile = new File(tempDir, "empty.txt");
        emptyFile.createNewFile();

        ChunkReader reader = new ChunkReader(emptyFile.getAbsolutePath(), 1024);
        try (BufferedInputStream stream = reader.openStream()) {
            assertNull(reader.readChunk(stream));
        }
    }

    @Test
    void testReadSmallChunks() throws FileReadException, IOException {
        ChunkReader reader = new ChunkReader(testFile.getAbsolutePath(), 1);
        try (BufferedInputStream stream = reader.openStream()) {
            String chunk1 = reader.readChunk(stream);
            String chunk2 = reader.readChunk(stream);
            String chunk3 = reader.readChunk(stream);

            assertEquals("l", chunk1);
            assertEquals("i", chunk2);
            assertEquals("n", chunk3);
        }
    }

    @Test
    void testOpenStreamNonExistentFile() {
        ChunkReader reader = new ChunkReader("nonexistent.txt", 1024);
        FileReadException exception = assertThrows(FileReadException.class, reader::openStream);
        assertEquals("Не удалось открыть файл.", exception.getMessage());
        assertNotNull(exception.getCause());
    }
}