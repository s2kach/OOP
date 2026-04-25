package ru.nsu.dizmestev;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Класс для работы с внешними процессами (Git, Gradle).
 */
public class SystemRunner {

    /**
     * Проверяет, настроен ли git на работу без пароля (dry-run).
     *
     * @throws CheckerException Если git требует аутентификацию или недоступен.
     */
    public void checkGitAuthless() throws CheckerException {
        try {
            Process process = new ProcessBuilder("git", "config",
                    "--global", "credential.helper").start();
            process.waitFor();
        } catch (IOException | InterruptedException e) {
            throw new CheckerException("Ошибка проверки настроек Git.", e);
        }
    }

    /**
     * Скачивает репозиторий студента.
     *
     * @param repoUrl Ссылка на репозиторий.
     * @param targetDir Папка для скачивания.
     * @throws CheckerException При ошибке клонирования.
     */
    public void cloneRepo(String repoUrl, File targetDir) throws CheckerException {
        if (new File(targetDir, "Task_1_1_1/gradlew.bat").exists()) {
            return;
        }

        try {
            File parent = targetDir.getParentFile();
            if (!parent.exists()) {
                parent.mkdirs();
            }

            System.out.println(">>> Клонируем репозиторий: " + repoUrl);
            ProcessBuilder pb = new ProcessBuilder("git", "clone",
                    repoUrl, targetDir.getAbsolutePath());
            pb.inheritIO();
            int exitCode = pb.start().waitFor();

            if (exitCode != 0) {
                throw new CheckerException("Git не смог склонировать репозиторий. Код: "
                        + exitCode, null);
            }
        } catch (IOException | InterruptedException e) {
            throw new CheckerException("Ошибка при клонировании репозитория.", e);
        }
    }

    /**
     * Запускает Gradle проверки.
     *
     * @param taskDir Папка с лабой.
     * @return true если сборка прошла успешно.
     * @throws CheckerException Ошибка ввода-вывода.
     */
    public boolean runGradleChecks(File taskDir) throws CheckerException {
        File gradlewFile = new File(taskDir, "gradlew.bat");

        if (!gradlewFile.exists()) {
            System.err.println("!!! Пропуск: " + taskDir.getName()
                    + " не найдена (не склонировалась или путь неверный)");
            return false;
        }
        try {
            ProcessBuilder pb = new ProcessBuilder(gradlewFile.getAbsolutePath());
            pb.command().add("build");
            pb.command().add("--init-script");
            pb.command().add("../../init.gradle");
            pb.command().add("checkstyleMain");
            pb.command().add("javadoc");

            pb.directory(taskDir);

            String javaHome = System.getProperty("java.home");
            pb.environment().put("JAVA_HOME", javaHome);

            pb.inheritIO();

            Process process = pb.start();
            int exitCode = process.waitFor();

            return exitCode == 0;
        } catch (IOException | InterruptedException e) {
            throw new CheckerException("Не удалось запустить Gradle в: "
                    + taskDir.getAbsolutePath(), e);
        }
    }

    /**
     * Получить момент даты и времени коммита.
     *
     * @param taskDir директория
     * @return дата в классе LocalDateTime.
     */
    public LocalDateTime getCommitDate(File taskDir) {
        try {
            ProcessBuilder pb = new ProcessBuilder("git", "log", "-1", "--format=%ai", ".");
            pb.directory(taskDir);
            Process p = pb.start();
            try (BufferedReader r = new BufferedReader(
                    new InputStreamReader(p.getInputStream()))) {
                String line = r.readLine();
                if (line != null && line.length() >= 19) {
                    return LocalDateTime.parse(line.substring(0, 19),
                            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                }
            }
        } catch (Exception e) {
            System.err.println("Ошибка получения даты Git для " + taskDir.getName());
        }
        return LocalDateTime.now();
    }
}
