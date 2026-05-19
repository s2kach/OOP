package ru.nsu.dizmestev;

import java.io.File;
import java.io.FileWriter;
import java.util.List;

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
            new DslDelegate(config).include("run.groovy");

            RepositoryChecker checker = new RepositoryChecker(runner, config);
            List<CheckResult> results = checker.runAll();

            HtmlReportGenerator generator = new HtmlReportGenerator();
            String html = generator.generate(results, config.getCheckpoints());

            try (FileWriter writer = new FileWriter(new File("report.html"))) {
                writer.write(html);
            }

            System.out.println("Проверка завершена. Отчет сохранен в report.html");

        } catch (Exception e) {
            throw new CheckerException("Ошибка в основном цикле программы.", e);
        }
    }
}