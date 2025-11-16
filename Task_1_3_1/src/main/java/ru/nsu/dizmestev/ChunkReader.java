package ru.nsu.dizmestev;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * Класс для чтения файла небольшими частями.
 */
public class ChunkReader {

    private final String filename;
    private final int bufferSize;

    /**
     * Создаёт объект чтения файла частями.
     *
     * @param filename   Имя файла.
     * @param bufferSize Размер буфера в байтах.
     */
    public ChunkReader(String filename, int bufferSize) {
        this.filename = filename;
        this.bufferSize = bufferSize;
    }

    /**
     * Читает следующую часть файла.
     *
     * @param stream Поток чтения.
     * @return Строку из следующего куска файла или null, если файл закончился.
     * @throws FileReadException Ошибка чтения файла.
     */
    public String readChunk(BufferedInputStream stream) throws FileReadException {
        byte[] buffer = new byte[bufferSize];

        int read;
        try {
            read = stream.read(buffer);
        } catch (IOException e) {
            throw new FileReadException("Ошибка чтения файла.", e);
        }

        if (read == -1) {
            return null;
        }

        return new String(buffer, 0, read, StandardCharsets.UTF_8);
    }

    /**
     * Открывает поток для чтения файла.
     *
     * @return Поток чтения.
     * @throws FileReadException Ошибка открытия файла.
     */
    public BufferedInputStream openStream() throws FileReadException {
        try {
            return new BufferedInputStream(new FileInputStream(filename));
        } catch (IOException e) {
            throw new FileReadException("Не удалось открыть файл.", e);
        }
    }
}
