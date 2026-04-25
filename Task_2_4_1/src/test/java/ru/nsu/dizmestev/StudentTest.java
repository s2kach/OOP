package ru.nsu.dizmestev;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class StudentTest {

    @Test
    void testStudentGetters() {
        Student student = new Student("s2kach", "Denis Izmestev", "https://github.com/s2kach/repo");

        assertEquals("s2kach", student.getGithub());
        assertEquals("Denis Izmestev", student.getName());
        assertEquals("https://github.com/s2kach/repo", student.getRepoUrl());
    }
}