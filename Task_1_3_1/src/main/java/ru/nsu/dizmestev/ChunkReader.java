package ru.nsu.dizmestev;

import java.io.BufferedInputStream;
import java.io.Closeable;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * Класс для чтения файла небольшими частями.
 */
public class ChunkReader implements Closeable {

    private final BufferedInputStream stream;
    private final int chunkSize;

    /**
     * Создаёт объект чтения файла частями.
     *
     * @param filename   Имя файла.
     * @param chunkSize Размер чанка.
     */
    public ChunkReader(String filename, int chunkSize) throws TaskException {
        if (chunkSize <= 0) {
            throw new TaskException("Размер чанка должен быть положительным");
        }

        try {
            this.stream = new BufferedInputStream(new FileInputStream(filename));
            this.chunkSize = chunkSize;
        } catch (IOException e) {
            throw new TaskException("Не удалось открыть файл: " + filename, e);
        }
    }

    /**
     * Читает очередной блок данных.
     *
     * @return массив байт, либо null если файл закончился
     * @throws TaskException при ошибке чтения
     */
    public byte[] readChunk() throws TaskException {
        try {
            byte[] buffer = new byte[chunkSize];
            int read = stream.read(buffer);

            if (read == -1) {
                return null;
            }

            if (read < chunkSize) {
                byte[] exact = new byte[read];
                System.arraycopy(buffer, 0, exact, 0, read);
                return exact;
            }

            return buffer;

        } catch (IOException e) {
            throw new FileReadException("Не удалось прочитать файл.", e);
        }
    }

    @Override
    public void close() throws IOException {
        stream.close();
    }
}