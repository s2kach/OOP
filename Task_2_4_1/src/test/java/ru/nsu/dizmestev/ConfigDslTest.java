package ru.nsu.dizmestev;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDate;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ConfigDslTest {

    private CourseConfig config;
    private DslDelegate delegate;

    @BeforeEach
    void setUp() {
        config = new CourseConfig();
        delegate = new DslDelegate(config);
    }

    @Test
    void testDelegateStudentCreation() {
        Map<String, Object> params = new HashMap<>();
        params.put("github", "john");
        params.put("name", "John");
        params.put("repoUrl", "http://github.com/john/OOP");

        delegate.student(params);

        List<Student> students = config.getStudents();
        assertFalse(students.isEmpty());
        Student s = students.get(0);
        assertEquals("john", s.getGithub());
        assertEquals("John", s.getName());
        assertEquals("http://github.com/john/OOP", s.getRepoUrl());
    }

    @Test
    void testDelegateTaskCreation() {
        Map<String, Object> params = new HashMap<>();
        params.put("id", "Task_1_1_1");
        params.put("name", "Basics");
        params.put("maxPoints", 2);
        params.put("softDeadline", "10.09.2025");
        params.put("hardDeadline", "20.09.2025");

        delegate.task(params);

        assertFalse(config.getTasks().isEmpty());
        Task t = config.getTasks().get(0);
        assertEquals("Task_1_1_1", t.getId());
        assertEquals(2, t.getMaxPoints());
        assertEquals(LocalDate.of(2025, 9, 10), t.getSoftDeadline());
    }

    @Test
    void testDelegateCheckpointParsing() {
        Map<String, Object> params = new HashMap<>();
        params.put("name", "Middle Control");
        params.put("date", "20.10.2025");

        delegate.checkpoint(params);

        assertFalse(config.getCheckpoints().isEmpty());
        Checkpoint cp = config.getCheckpoints().get(0);
        assertEquals("Middle Control", cp.getName());
        assertEquals(LocalDate.of(2025, 10, 20), cp.getDate());
    }

    @Test
    void testDelegateExtraPoints() {
        Map<String, Object> params = new HashMap<>();
        params.put("student", "student1");
        params.put("task", "Task_1");
        params.put("points", 1.5);

        delegate.extraPoints(params);

        assertEquals(1.5, config.getExtra("student1", "Task_1"));
    }

    @Test
    void testDelegateAssign() {
        Map<String, Object> params = new HashMap<>();
        params.put("taskId", "Lab1");
        params.put("students", Collections.singletonList("st_nick"));

        delegate.assign(params);

        Map<String, List<String>> assignments = config.getAssignments();
        assertNotNull(assignments.get("Lab1"));
        assertEquals("st_nick", assignments.get("Lab1").get(0));
    }

    @Test
    void testCourseConfigDirectStorage() {
        config.addExtraPoints("user1", "task1", 2.0);
        assertEquals(2.0, config.getExtra("user1", "task1"));

        Checkpoint cp = new Checkpoint("KT1", LocalDate.now());
        config.addCheckpoint(cp);
        assertEquals("KT1", config.getCheckpoints().get(0).getName());
    }
}