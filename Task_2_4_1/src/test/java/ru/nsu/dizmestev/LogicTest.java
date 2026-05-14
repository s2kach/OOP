package ru.nsu.dizmestev;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class LogicTest {

    static class FakeSystemRunner extends SystemRunner {
        public LocalDateTime mockDate = LocalDateTime.now();
        public boolean mockBuildResult = true;

        @Override
        public void cloneRepo(String url, File dir) {
            dir.mkdirs();
        }

        @Override
        public LocalDateTime getCommitDate(File dir) {
            return mockDate;
        }

        @Override
        public boolean runGradleChecks(File dir) {
            return mockBuildResult;
        }

        @Override
        public void checkGitAuthless() {
        }
    }

    @Test
    void testScoreCalculationWithDeadlines() {
        LocalDate soft = LocalDate.now().minusDays(1);
        LocalDate hard = LocalDate.now().plusDays(1);
        Task task = new Task("T1", "Lab", 2, soft, hard);

        LocalDateTime submission = LocalDateTime.now();
        double score = calculateTestScore(task, submission, 1.0);
        assertEquals(1.0, score);
    }

    private double calculateTestScore(Task task, LocalDateTime commitDate, double testRatio) {
        double multiplier = 1.0;
        if (commitDate.toLocalDate().isAfter(task.getHardDeadline())) {
            multiplier = 0.0;
        } else if (commitDate.toLocalDate().isAfter(task.getSoftDeadline())) {
            multiplier = 0.5;
        }
        return task.getMaxPoints() * testRatio * multiplier;
    }

    @Test
    void testRepositoryCheckerFullCycle(@TempDir File tempDir) throws CheckerException {
        FakeSystemRunner fakeRunner = new FakeSystemRunner();
        Student s = new Student("test_user", "Test Student", "http://fake.url");
        Task t = new Task("Task_1", "Lab 1", 10, LocalDate.now().minusDays(5),
                LocalDate.now().plusDays(5));

        CourseConfig config = new CourseConfig();
        config.addStudent(s);
        config.addTask(t);
        config.assignCheck("Task_1", List.of("test_user"));
        config.addExtraPoints("test_user", "Task_1", 2.0);

        File studentDir = new File("workspaces/test_user/Task_1");
        studentDir.mkdirs();

        RepositoryChecker checker = new RepositoryChecker(fakeRunner, config);
        List<CheckResult> results = checker.runAll();

        assertFalse(results.isEmpty());
        CheckResult res = results.get(0);

        assertEquals(7.0, res.getTotalScore());
        assertTrue(res.isBuildSuccess());

        studentDir.delete();
    }
}