package ru.nsu.dizmestev;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Test;

class HtmlReportGeneratorTest {

    @Test
    void testHtmlStructure() {
        HtmlReportGenerator generator = new HtmlReportGenerator();
        Student student = new Student("gh", "Ivan", "url");
        TestResult tr = new TestResult(5, 0, 0, 0);

        CheckResult res = new CheckResult(student, "Task1", true, tr, 0, LocalDate.now(), 1.0);
        List<CheckResult> results = Collections.singletonList(res);
        List<Checkpoint> cps = new ArrayList<>();

        String html = generator.generate(results, cps);

        assertTrue(html.contains("<!DOCTYPE html>"));
        assertTrue(html.contains("Ivan"));
        assertTrue(html.contains("BUILD SUCCESS"));
        assertTrue(html.contains("Итоговый балл"));
    }
}