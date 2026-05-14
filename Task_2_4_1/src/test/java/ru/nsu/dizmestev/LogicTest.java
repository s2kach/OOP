package ru.nsu.dizmestev;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;

class LogicTest {

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
}