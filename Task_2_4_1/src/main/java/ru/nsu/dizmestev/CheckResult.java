package ru.nsu.dizmestev;

import java.time.LocalDate;

/**
 * Контейнер для хранения результатов проверки одной задачи конкретного студента.
 * Используется для передачи данных между логикой проверки и генератором отчета.
 */
public class CheckResult {
    private final Student student;
    private final String taskId;
    private final boolean buildSuccess;
    private final TestResult testResult;
    private final int styleErrors;
    private final LocalDate submissionDate;
    private final double totalScore;

    /**
     * Создает объект результата проверки.
     *
     * @param student        объект студента
     * @param taskId         идентификатор задачи
     * @param buildSuccess   флаг успешности сборки Gradle
     * @param testResult     объект с результатами тестов (пройдено/упало)
     * @param styleErrors    количество ошибок Checkstyle
     * @param submissionDate дата последнего коммита в папку задачи
     * @param totalScore     итоговый балл с учетом штрафов и бонусов
     */
    public CheckResult(Student student, String taskId, boolean buildSuccess, TestResult testResult,
                       int styleErrors, LocalDate submissionDate, double totalScore) {
        this.student = student;
        this.taskId = taskId;
        this.buildSuccess = buildSuccess;
        this.testResult = testResult;
        this.styleErrors = styleErrors;
        this.submissionDate = submissionDate;
        this.totalScore = totalScore;
    }

    public Student getStudent() { return student; }
    public String getTaskId() { return taskId; }
    public boolean isBuildSuccess() { return buildSuccess; }
    public TestResult getTestResult() { return testResult; }
    public int getStyleErrors() { return styleErrors; }
    public LocalDate getSubmissionDate() { return submissionDate; }
    public double getTotalScore() { return totalScore; }
}