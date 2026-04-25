package ru.nsu.dizmestev;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import org.junit.jupiter.api.Test;

class CheckpointTest {

    @Test
    void testCheckpointCreation() {
        LocalDate date = LocalDate.of(2025, 10, 20);
        Checkpoint cp = new Checkpoint("КТ 1", date);

        assertEquals("КТ 1", cp.getName());
        assertEquals(date, cp.getDate());
    }
}