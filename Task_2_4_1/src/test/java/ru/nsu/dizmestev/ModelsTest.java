package ru.nsu.dizmestev;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class ModelsTest {

    static class GradleMockRunner extends SystemRunner {
        private final boolean buildResult;

        GradleMockRunner(boolean buildResult) {
            this.buildResult = buildResult;
        }

        @Override
        public boolean runGradleChecks(File taskDir) throws CheckerException {
            File gradlewFile = new File(taskDir, "gradlew.bat");
            if (!gradlewFile.exists()) {
                throw new CheckerException("Пропуск: " + taskDir.getName() + " не найдена");
            }
            return buildResult;
        }
    }

    @Test
    void testRunGradleChecksSuccess(@TempDir File tempDir) throws Exception {
        new File(tempDir, "gradlew.bat").createNewFile();
        SystemRunner runner = new GradleMockRunner(true);
        assertTrue(runner.runGradleChecks(tempDir));
    }

    @Test
    void testRunGradleChecksFailure(@TempDir File tempDir) throws Exception {
        new File(tempDir, "gradlew.bat").createNewFile();
        SystemRunner runner = new GradleMockRunner(false);
        assertFalse(runner.runGradleChecks(tempDir));
    }

    @Test
    void testStudentGetters() {
        Student student = new Student("s2kach", "Denis Izmestev", "https://github.com/s2kach/repo");
        assertEquals("s2kach", student.getGithub());
        assertEquals("Denis Izmestev", student.getName());
        assertEquals("https://github.com/s2kach/repo", student.getRepoUrl());
    }

    @Test
    void testCheckpointCreation() {
        LocalDate date = LocalDate.of(2025, 10, 20);
        Checkpoint cp = new Checkpoint("КТ 1", date);
        assertEquals("КТ 1", cp.getName());
        assertEquals(date, cp.getDate());
    }

    @Test
    void testToStringFormattingAllPassed() {
        TestResult result = new TestResult(10, 0, 0, 0);
        assertEquals("10/0/0", result.toString());
    }

    @Test
    void testTaskGetters() {
        LocalDate soft = LocalDate.of(2025, 9, 13);
        LocalDate hard = LocalDate.of(2025, 9, 20);
        Task task = new Task("T1", "Name", 1, soft, hard);

        assertEquals("T1", task.getId());
        assertEquals(1, task.getMaxPoints());
        assertEquals(soft, task.getSoftDeadline());
        assertEquals(hard, task.getHardDeadline());
    }

    @Test
    void testCheckResultDataIntegrity() {
        Student student = new Student("gh", "Name", "url");
        TestResult tr = new TestResult(10, 0, 0, 0);
        LocalDate now = LocalDate.now();
        CheckResult result = new CheckResult(student, "T1", true, tr, 0, now, 1.5);

        assertEquals(student, result.getStudent());
        assertEquals("T1", result.getTaskId());
        assertTrue(result.isBuildSuccess());
        assertEquals(tr, result.getTestResult());
        assertEquals(0, result.getStyleErrors());
        assertEquals(now, result.getSubmissionDate());
        assertEquals(1.5, result.getTotalScore());
    }

    @Test
    void testCheckerException() {
        Exception cause = new RuntimeException("root");
        CheckerException ex = new CheckerException("msg", cause);

        assertEquals("msg", ex.getMessage());
        assertEquals(cause, ex.getCause());
    }

    @Test
    void testCheckerInitialization() {
        CourseConfig config = new CourseConfig();
        SystemRunner runner = new SystemRunner();
        RepositoryChecker checker = new RepositoryChecker(runner, config);

        assertNotNull(checker);
    }

    @Test
    void testRunnerInstance() {
        SystemRunner runner = new SystemRunner();
        assertNotNull(runner);
    }

    @Test
    void testGetCommitDateWithEmptyDir(@TempDir File tempDir) {
        SystemRunner runner = new SystemRunner();
        runner.getCommitDate(tempDir);
    }

    @Test
    void testCloneRepoSkipsIfMarkerExists(@TempDir Path tempDir) throws IOException {
        File taskDir = tempDir.resolve("Task_1_1_1").toFile();
        taskDir.mkdirs();
        new File(taskDir, "gradlew.bat").createNewFile();

        SystemRunner runner = new SystemRunner();

        assertDoesNotThrow(() -> runner.cloneRepo("any_url", tempDir.toFile()));
    }

    @Test
    void testCloneRepoFailsWithInvalidUrl(@TempDir File tempDir) {
        SystemRunner runner = new SystemRunner();
        File targetDir = new File(tempDir, "invalid_repo");

        assertThrows(CheckerException.class, () ->
                runner.cloneRepo("https://invalid-url-at-all.com/repo.git", targetDir)
        );
    }

    @Test
    void testCloneRepoCreatesParentDirs(@TempDir File tempDir) {
        SystemRunner runner = new SystemRunner();
        File targetDir = new File(tempDir, "sub1/sub2/myrepo");

        assertThrows(CheckerException.class, () -> {
            runner.cloneRepo("invalid_url", targetDir);
        });

        assertTrue(new File(tempDir, "sub1/sub2").exists(), "Parent directories should be created");
    }

    @Test
    void testGetCommitDateWithInvalidDir(@TempDir File tempDir) {
        SystemRunner runner = new SystemRunner();
        LocalDateTime date = runner.getCommitDate(tempDir);
        assertNotNull(date);
        assertTrue(date.isBefore(LocalDateTime.now().plusSeconds(1)));
    }

    @Test
    void testRunGradleChecksFailsOnMissingDir() {
        SystemRunner runner = new SystemRunner();
        File fake = new File("completely_wrong_path");
        assertThrows(CheckerException.class, () -> runner.runGradleChecks(fake));
    }

    @Test
    void testCheckGitAuthless() {
        SystemRunner runner = new SystemRunner();
        assertDoesNotThrow(() -> runner.checkGitAuthless());
    }

    @Test
    void testCloneRepoSuccess(@TempDir Path tempDir) throws Exception {
        File bareRepo = tempDir.resolve("bare.git").toFile();
        bareRepo.mkdirs();
        new ProcessBuilder("git", "init", "--bare", bareRepo.getAbsolutePath())
                .start().waitFor();

        SystemRunner runner = new SystemRunner();
        File targetDir = tempDir.resolve("clone_target").toFile();

        assertDoesNotThrow(()
                -> runner.cloneRepo("file://" + bareRepo.getAbsolutePath(), targetDir));
        assertTrue(new File(targetDir, ".git").exists());
    }

    @Test
    void testGetCommitDateSuccess(@TempDir File tempDir) throws Exception {
        new ProcessBuilder("git", "init").directory(tempDir).start().waitFor();
        new ProcessBuilder("git", "config", "user.email", "test@test.com")
                .directory(tempDir).start().waitFor();
        new ProcessBuilder("git", "config", "user.name", "Test")
                .directory(tempDir).start().waitFor();
        Files.writeString(tempDir.toPath().resolve("file.txt"), "data");
        new ProcessBuilder("git", "add", ".").directory(tempDir).start().waitFor();
        new ProcessBuilder("git", "commit", "-m", "init").directory(tempDir).start().waitFor();

        SystemRunner runner = new SystemRunner();
        LocalDateTime date = runner.getCommitDate(tempDir);

        assertNotNull(date);
        assertTrue(date.isBefore(LocalDateTime.now().plusSeconds(2)));
        assertTrue(date.isAfter(LocalDateTime.now().minusMinutes(1)));
    }
}