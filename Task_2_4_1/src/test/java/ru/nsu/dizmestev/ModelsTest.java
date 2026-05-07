package ru.nsu.dizmestev;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import org.junit.jupiter.api.Test;

class ModelsTest {

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
}