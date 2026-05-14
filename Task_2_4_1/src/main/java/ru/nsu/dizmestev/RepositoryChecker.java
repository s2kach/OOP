package ru.nsu.dizmestev;

import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Сервис для выполнения проверок репозиториев.
 * Отвечает за клонирование, запуск сборки, парсинг результатов и расчет баллов.
 */
public class RepositoryChecker {
    private final SystemRunner runner;
    private final CourseConfig config;
    private final File workspacesDir;

    /**
     * Конструктор чекера.
     *
     * @param runner инструмент для выполнения системных команд (git/gradle)
     * @param config общая конфигурация курса
     */
    public RepositoryChecker(SystemRunner runner, CourseConfig config) {
        this.runner = runner;
        this.config = config;
        this.workspacesDir = new File("workspaces");
        if (!workspacesDir.exists()) {
            workspacesDir.mkdir();
        }
    }

    /**
     * Запускает полный цикл проверки всех назначенных задач для всех студентов.
     *
     * @return список результатов
     * @throws CheckerException при критических ошибках в процессе проверки
     */
    public List<CheckResult> runAll() throws CheckerException {
        List<CheckResult> results = new ArrayList<>();

        for (Map.Entry<String, List<String>> entry : config.getAssignments().entrySet()) {
            String taskId = entry.getKey();
            Task task = config.getTasks().stream()
                    .filter(t -> t.getId().equals(taskId)).findFirst().orElse(null);
            if (task == null) {
                continue;
            }

            for (String github : entry.getValue()) {
                Student student = config.getStudents().stream()
                        .filter(s -> s.getGithub().equals(github)).findFirst().orElse(null);
                if (student == null) {
                    continue;
                }

                results.add(processSingle(student, task));
            }
        }
        return results;
    }

    /**
     * Выполняет проверку одной конкретной работы.
     * Включает в себя клонирование, сборку, запуск тестов и расчет баллов по дедлайнам.
     *
     * @param student студент
     * @param task    задача
     * @return результат проверки
     * @throws CheckerException при системных ошибках (например, сбой Git)
     */
    private CheckResult processSingle(Student student, Task task) throws CheckerException {
        File studentDir = new File(workspacesDir, student.getGithub());
        runner.cloneRepo(student.getRepoUrl(), studentDir);
        File taskDir = new File(studentDir, task.getId());

        System.out.println(">>> Проверяем: " + student.getName() + " | Задача: " + task.getId());

        TestResult testResult = new TestResult(0, 0, 0, 0);
        boolean buildSuccess = false;

        if (taskDir.exists() && taskDir.isDirectory()) {
            buildSuccess = runner.runGradleChecks(taskDir);
            try {
                testResult = new TestParser().parse(taskDir);
            } catch (Exception e) {
                System.err.println("Could not parse test results in " + taskDir.getName());
            }
        }

        int styleErrors = new StyleParser().countStyleErrors(taskDir);
        LocalDate submissionDate = runner.getCommitDate(taskDir).toLocalDate();

        double multiplier = 1.0;
        if (submissionDate.isAfter(task.getHardDeadline())) {
            multiplier = 0.0;
        } else if (submissionDate.isAfter(task.getSoftDeadline())) {
            multiplier = 0.5;
        }

        double baseScore = (buildSuccess && testResult.toString().contains("/0/"))
                ? task.getMaxPoints() : 0;
        double totalScore = (baseScore * multiplier)
                + config.getExtra(student.getGithub(), task.getId());

        return new CheckResult(student, task.getId(), buildSuccess, testResult,
                styleErrors, submissionDate, totalScore);
    }
}