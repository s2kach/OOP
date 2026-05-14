package ru.nsu.dizmestev;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import groovy.lang.Closure;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

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

    @Test
    void testDelegateIncludeMethod(@TempDir Path tempDir) throws IOException, CheckerException {
        File scriptFile = tempDir.resolve("config.groovy").toFile();
        try (FileWriter writer = new FileWriter(scriptFile)) {
            writer.write("student(github: 'user', name: 'Ivan', repoUrl: 'url')");
        }

        delegate.include(scriptFile.getAbsolutePath());

        assertFalse(config.getStudents().isEmpty());
        assertEquals("user", config.getStudents().get(0).getGithub());
    }

    @Test
    void testDelegateTaskFull(@TempDir Path tempDir) {
        Map<String, Object> p = new HashMap<>();
        p.put("id", "Lab1");
        p.put("name", "Intro");
        p.put("maxPoints", 5);
        p.put("softDeadline", "01.01.2025");
        p.put("hardDeadline", "10.01.2025");

        delegate.task(p);

        Task task = config.getTasks().get(0);
        assertEquals(5, task.getMaxPoints());
        assertEquals(LocalDate.of(2025, 1, 1), task.getSoftDeadline());
    }

    @Test
    void testCheckpointsClosureExecution() {
        delegate.checkpoints(new Closure<Object>(delegate) {
            public Object doCall() {
                Map<String, Object> p = new HashMap<>();
                p.put("name", "KT 1");
                p.put("date", "01.01.2025");
                ((DslDelegate) getDelegate()).checkpoint(p);
                return null;
            }
        });

        assertFalse(config.getCheckpoints().isEmpty());
        assertEquals("KT 1", config.getCheckpoints().get(0).getName());
    }

    @Test
    void testExtraPointsWithIntegerType() {
        Map<String, Object> params = new HashMap<>();
        params.put("student", "nick");
        params.put("task", "T1");
        params.put("points", 10);

        delegate.extraPoints(params);

        assertEquals(10.0, config.getExtra("nick", "T1"));
    }

    @Test
    void testRunAllWithNoAssignments() throws CheckerException {
        CourseConfig config = new CourseConfig();
        RepositoryChecker checker = new RepositoryChecker(new SystemRunner(), config);

        List<CheckResult> results = checker.runAll();
        assertTrue(results.isEmpty());
    }

    @Test
    void testRunAllWithIncompleteData() {
        CourseConfig config = new CourseConfig();
        config.addStudent(new Student("user", "Name", "http://bad-url"));
        config.addTask(new Task("Task_1_1_1", "Lab", 2, null, null));
        config.assignCheck("Task_1_1_1", Collections.singletonList("user"));

        RepositoryChecker checker = new RepositoryChecker(new SystemRunner(), config);

        assertThrows(CheckerException.class, () -> checker.runAll());
    }

}