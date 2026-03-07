package ru.nsu.dizmestev;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Простой парсер для извлечения данных из JSON конфигурации.
 */
public class JsonParser {

    /**
     * Извлекает массив целых чисел по ключу из JSON строки.
     *
     * @param json исходная строка JSON
     * @param key ключ, массив которого нужно найти
     * @return массив целых чисел
     */
    public static int[] getIntArray(String json, String key) {
        Pattern pattern = Pattern.compile("\"" + key + "\":\\s*\\[([^\\]]*)\\]");
        Matcher matcher = pattern.matcher(json);
        if (matcher.find()) {
            String content = matcher.group(1).trim();
            if (content.isEmpty()) {
                return new int[0];
            }
            String[] parts = content.split(",");
            int[] result = new int[parts.length];
            for (int i = 0; i < parts.length; i++) {
                result[i] = Integer.parseInt(parts[i].trim());
            }
            return result;
        }
        return new int[0];
    }

    /**
     * Извлекает одиночное целое число по ключу из JSON строки.
     *
     * @param json исходная строка JSON
     * @param key ключ поля
     * @return значение поля
     */
    public static int getInt(String json, String key) {
        Pattern pattern = Pattern.compile("\"" + key + "\":\\s*(\\d+)");
        Matcher matcher = pattern.matcher(json);
        if (matcher.find()) {
            return Integer.parseInt(matcher.group(1));
        }
        return 0;
    }
}
