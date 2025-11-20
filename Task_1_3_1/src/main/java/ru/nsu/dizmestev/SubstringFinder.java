package ru.nsu.dizmestev;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * Поиск подстроки в большом файле без загрузки файла полностью в память.
 * Возвращает позиции в символах, а не в байтах.
 */
public class SubstringFinder {

    private static final int DEFAULT_CHUNK_SIZE = 4096;

    /**
     * Ищет все вхождения подстроки в файле.
     *
     * @param filename Имя файла.
     * @param target   Подстрока, которую нужно найти.
     * @return Список индексов начала всех вхождений.
     * @throws SearchException Ошибка поиска или чтения данных.
     */
    public List<Long> find(String filename, String target) throws SearchException {
        if (target == null || target.isEmpty()) {
            throw new SearchException("Пустая подстрока не допускается");
        }

        byte[] pattern = target.getBytes(StandardCharsets.UTF_8);
        int m = pattern.length;

        if (m == 0) {
            throw new SearchException("Пустая подстрока не допускается");
        }

        List<Long> result = new ArrayList<>();

        try (ChunkReader reader = new ChunkReader(filename, DEFAULT_CHUNK_SIZE)) {

            byte[] overlap = new byte[0];
            long globalByteOffset = 0;
            long globalCharOffset = 0;

            while (true) {
                byte[] chunk = reader.readChunk();
                if (chunk == null) {
                    break;
                }

                byte[] buffer = new byte[overlap.length + chunk.length];
                System.arraycopy(overlap, 0, buffer, 0, overlap.length);
                System.arraycopy(chunk, 0, buffer, overlap.length, chunk.length);

                // Ищем совпадения в буфере
                for (int i = 0; i + m <= buffer.length; i++) {
                    boolean match = true;
                    for (int j = 0; j < m; j++) {
                        if (buffer[i + j] != pattern[j]) {
                            match = false;
                            break;
                        }
                    }
                    if (match) {
                        long bytePosition = globalByteOffset - overlap.length + i;
                        long charPosition = calculateCharPosition(filename, bytePosition);
                        result.add(charPosition);
                    }
                }

                globalByteOffset += chunk.length;
                globalCharOffset += new String(chunk, StandardCharsets.UTF_8).length();

                if (buffer.length >= m - 1) {
                    overlap = new byte[m - 1];
                    System.arraycopy(buffer, buffer.length - (m - 1), overlap, 0, m - 1);
                } else {
                    overlap = buffer;
                }
            }
        } catch (Exception e) {
            throw new SearchException("Ошибка поиска подстроки", e);
        }

        return result;
    }

    /**
     * Конвертирует позицию в байтах в позицию в символах для UTF-8.
     */
    private long calculateCharPosition(String filename, long bytePosition) throws TaskException {
        try (ChunkReader reader = new ChunkReader(filename, DEFAULT_CHUNK_SIZE)) {
            long currentBytePos = 0;
            long charCount = 0;

            while (true) {
                byte[] chunk = reader.readChunk();
                if (chunk == null) {
                    break;
                }

                if (currentBytePos + chunk.length > bytePosition) {
                    // Нашли чанк, содержащий позицию
                    int remaining = (int) (bytePosition - currentBytePos);
                    return charCount + new String(chunk, 0,
                            remaining, StandardCharsets.UTF_8).length();
                }

                charCount += new String(chunk, StandardCharsets.UTF_8).length();
                currentBytePos += chunk.length;
            }

            return charCount;
        } catch (Exception e) {
            throw new TaskException("Ошибка конвертации позиции", e);
        }
    }
}