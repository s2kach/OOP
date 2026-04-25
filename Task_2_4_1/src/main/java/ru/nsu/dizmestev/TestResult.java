package ru.nsu.dizmestev;

/**
 * Класс для хранения статистики выполнения тестов.
 */
public class TestResult {
    private final int errors;
    private final int failures;
    private final int skipped;
    private final int total;

    /**
     * Конструктор результатов.
     *
     * @param total Общее количество тестов.
     * @param failures Количество проваленных тестов.
     * @param errors Количество тестов с ошибками.
     * @param skipped Количество пропущенных тестов.
     */
    public TestResult(int total, int failures, int errors, int skipped) {
        this.total = total;
        this.failures = failures;
        this.errors = errors;
        this.skipped = skipped;
    }

    /**
     * Формирует строку в формате Успешно/Провалено/Пропущено.
     *
     * @return Форматированная строка.
     */
    @Override
    public String toString() {
        int passed = total - failures - errors - skipped;
        return String.format("%d/%d/%d", passed, (failures + errors), skipped);
    }

    public int getFailures() {
        return failures;
    }

    public int getTotal() {
        return total;
    }
}
