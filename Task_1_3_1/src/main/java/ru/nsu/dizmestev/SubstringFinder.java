package ru.nsu.dizmestev;

import java.io.BufferedInputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Класс для поиска всех вхождений подстроки в большом файле.
 */
public class SubstringFinder {

    private static final int DEFAULT_BUFFER_SIZE = 4096;

    /**
     * Ищет все вхождения подстроки в файле.
     *
     * @param filename Имя файла.
     * @param target   Подстрока, которую нужно найти.
     * @return Список индексов начала всех вхождений.
     * @throws SearchException Ошибка поиска или чтения данных.
     */
    public List<Integer> find(String filename, String target) throws SearchException {
        if (target == null || target.isEmpty()) {
            throw new SearchException("Строка поиска не может быть пустой.");
        }

        List<Integer> result = new ArrayList<>();
        ChunkReader reader = new ChunkReader(filename, DEFAULT_BUFFER_SIZE);

        String leftover = "";
        int offset = 0;

        try (BufferedInputStream stream = reader.openStream()) {

            while (true) {
                String chunk = reader.readChunk(stream);
                if (chunk == null) {
                    break;
                }

                String text = leftover + chunk;

                int index = text.indexOf(target);
                while (index >= 0) {
                    result.add(offset + index);
                    index = text.indexOf(target, index + 1);
                }

                int overlap = target.length() - 1;
                if (text.length() >= overlap) {
                    leftover = text.substring(text.length() - overlap);
                } else {
                    leftover = text;
                }

                offset += chunk.length();
            }

        } catch (Exception e) {
            throw new SearchException("Ошибка при выполнении поиска.", e);
        }

        return result;
    }
}
