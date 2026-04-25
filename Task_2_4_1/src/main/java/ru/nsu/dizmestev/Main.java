package ru.nsu.dizmestev;

import java.io.File;
import java.io.FileWriter;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * Главный класс приложения для запуска проверки и генерации отчета.
 */
public class Main {

    /**
     * Точка входа в программу.
     *
     * @param args Аргументы командной строки.
     * @throws CheckerException В случае критической ошибки.
     */
    public static void main(String[] args) throws CheckerException {
        try {
            SystemRunner runner = new SystemRunner();
            runner.checkGitAuthless();

            CourseConfig config = new CourseConfig();
            DslDelegate delegate = new DslDelegate(config);

            delegate.include("run.groovy");

            String htmlReport = executeChecksAndGetHtml(config, runner);

            File reportFile = new File("report.html");
            try (FileWriter writer = new FileWriter(reportFile)) {
                writer.write(htmlReport);
            }

            System.out.println("Проверка завершена. Отчет сохранен в report.html");

        } catch (Exception e) {
            throw new CheckerException("Ошибка в основном цикле программы.", e);
        }
    }

    /**
     * Проводит тесты и формирует строку с HTML кодом.
     * Реализует логику проверки билда, тестов, стиля и подсчета баллов с учетом дедлайнов.
     *
     * @param config Конфигурация курса, содержащая списки задач и студентов.
     * @param runner Инструмент для запуска Git и Gradle команд.
     * @return Строка, содержащая полный HTML-код отчета.
     * @throws CheckerException Ошибка при работе с файлами или процессами.
     */
    private static String executeChecksAndGetHtml(CourseConfig config,
                                                  SystemRunner runner) throws CheckerException {
        StringBuilder sb = new StringBuilder();
        sb.append("<!DOCTYPE html><html><head><meta charset="
                + "'UTF-8'><title>OOP Report</title></head><body>");
        sb.append("<h2>Отчет о проверке репозиториев</h2>");
        sb.append("<table border='1' style='border-collapse: collapse; width: 100%;'>");
        sb.append("<tr style='background-color: #f2f2f2;'>")
                .append("<th>Студент</th><th>Задача</th><th>Статус</th><th>Тесты (П/Ф/С)</th>")
                .append("<th>Стиль</th><th>Дата сдачи</th><th>Итоговый балл</th></tr>");

        File workspacesDir = new File("workspaces");
        if (!workspacesDir.exists()) {
            workspacesDir.mkdir();
        }

        for (Map.Entry<String, List<String>> entry : config.getAssignments().entrySet()) {
            String taskId = entry.getKey();

            Task task = config.getTasks().stream()
                    .filter(t -> t.getId().equals(taskId))
                    .findFirst()
                    .orElse(null);

            if (task == null) {
                System.err.println("Предупреждение: Задача "
                        + taskId + " не описана в блоке tasks.");
                continue;
            }

            for (String github : entry.getValue()) {
                Student student = config.getStudents().stream()
                        .filter(s -> s.getGithub().equals(github))
                        .findFirst()
                        .orElse(null);

                if (student == null) continue;

                File studentDir = new File(workspacesDir, student.getGithub());
                runner.cloneRepo(student.getRepoUrl(), studentDir);

                File taskDir = new File(studentDir, taskId);

                System.out.println(">>> Проверяем: " + student.getName() + " | Задача: " + taskId);

                TestResult testResult = new TestResult(0, 0, 0, 0);
                boolean buildSuccess = false;

                if (taskDir.exists() && taskDir.isDirectory()) {
                    buildSuccess = runner.runGradleChecks(taskDir);

                    try {
                        TestParser parser = new TestParser();
                        testResult = parser.parse(taskDir);
                    } catch (Exception e) {
                        System.err.println("Не удалось распарсить тесты для " + taskId);
                    }
                }

                StyleParser styleParser = new StyleParser();
                int styleErrors = styleParser.countStyleErrors(taskDir);

                LocalDateTime commitDate = runner.getCommitDate(taskDir);
                LocalDate submissionDate = commitDate.toLocalDate();

                double multiplier = 1.0;
                if (submissionDate.isAfter(task.getHardDeadline())) {
                    multiplier = 0.0;
                } else if (submissionDate.isAfter(task.getSoftDeadline())) {
                    multiplier = 0.5;
                }

                double baseScore = (buildSuccess && testResult.toString().contains("/0/"))
                        ? task.getMaxPoints() : 0;

                double totalScore = (baseScore * multiplier)
                        + config.getExtra(student.getGithub(), taskId);

                sb.append("<tr>")
                        .append("<td>").append(student.getName()).append("</td>")
                        .append("<td>").append(taskId).append("</td>")
                        .append("<td style='color: ")
                        .append(buildSuccess ? "green" : "red").append(";'>")
                        .append(buildSuccess ? "BUILD SUCCESS" : "BUILD FAILED").append("</td>")
                        .append("<td>").append(testResult.toString()).append("</td>")
                        .append("<td>").append(styleErrors == 0 ? "OK" : "Errors: " + styleErrors)
                        .append("</td>")
                        .append("<td>").append(submissionDate).append("</td>")
                        .append("<td><b>").append(totalScore).append("</b></td>")
                        .append("</tr>");
            }
        }

        appendCheckpointsSummary(sb, config);

        sb.append("</table></body></html>");
        return sb.toString();
    }

    /**
     * Добавляет в отчет сводную информацию по контрольным точкам.
     */
    private static void appendCheckpointsSummary(StringBuilder sb, CourseConfig config) {
        sb.append("</table><br><h2>Контрольные точки и итоговая аттестация</h2>");
        sb.append("<table border='1' style='border-collapse: collapse;'>");
        sb.append("<tr style='background-color: #f2f2f2;'>");
        sb.append("<th>Контрольная точка</th><th>Дата</th></tr>");

        for (Checkpoint cp : config.getCheckpoints()) {
            sb.append("<tr><td>").append(cp.getName()).append("</td>")
                    .append("<td>").append(cp.getDate()).append("</td></tr>");
        }
    }
}