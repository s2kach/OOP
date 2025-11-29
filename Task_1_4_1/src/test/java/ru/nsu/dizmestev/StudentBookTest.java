package ru.nsu.dizmestev;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class StudentBookTest {

    private StudentBook studentBook;
    private StudentBook budgetStudentBook;

    @BeforeEach
    void setUp() {
        studentBook = new StudentBook("12345", "Иванов Иван", StudyForm.PAID, 40);
        budgetStudentBook = new StudentBook("67890", "Петров Петр", StudyForm.BUDGET, 12);
    }

    @Test
    void testStudentInfo() {
        assertEquals("12345", studentBook.getStudentId());
        assertEquals("Иванов Иван", studentBook.getStudentName());
        assertEquals(StudyForm.PAID, studentBook.getStudyForm());
    }

    @Test
    void testCalculateAverageWithNoRecords() {
        assertThrows(AcademicBaseException.class, () -> studentBook.calculateAverage());
    }

    @Test
    void testCalculateAverage() throws Exception {
        studentBook.addRecord(new AcademicRecord(1, ControlType.EXAM, Grade.EXCELLENT));
        studentBook.addRecord(new AcademicRecord(1, ControlType.EXAM, Grade.GOOD));
        studentBook.addRecord(new AcademicRecord(1, ControlType.DIFF_CREDIT, Grade.EXCELLENT));

        assertEquals(4.67, studentBook.calculateAverage(), 0.01);
    }

    @Test
    void testCalculateAverageWithDifferentGrades() throws Exception {
        studentBook.addRecord(new AcademicRecord(1, ControlType.EXAM, Grade.EXCELLENT));
        studentBook.addRecord(new AcademicRecord(1, ControlType.EXAM, Grade.GOOD));
        studentBook.addRecord(new AcademicRecord(1, ControlType.EXAM, Grade.SATISFACTORY));

        assertEquals(4.0, studentBook.calculateAverage(), 0.01);
    }

    @Test
    void testCanTransferToBudgetWhenAlreadyBudget() throws Exception {
        assertFalse(budgetStudentBook.canTransferToBudget());
    }

    @Test
    void testCanTransferToBudgetWithSatisfactoryInLastSemesters() throws Exception {
        studentBook.addRecord(new AcademicRecord(3, ControlType.EXAM, Grade.SATISFACTORY));
        studentBook.addRecord(new AcademicRecord(2, ControlType.EXAM, Grade.EXCELLENT));

        assertFalse(studentBook.canTransferToBudget());
    }

    @Test
    void testCanTransferToBudgetWithSatisfactoryInPreviousSemester() throws Exception {
        studentBook.addRecord(new AcademicRecord(3, ControlType.EXAM, Grade.EXCELLENT));
        studentBook.addRecord(new AcademicRecord(2, ControlType.EXAM, Grade.SATISFACTORY));
        studentBook.addRecord(new AcademicRecord(1, ControlType.EXAM, Grade.EXCELLENT));

        assertFalse(studentBook.canTransferToBudget());
    }

    @Test
    void testCanTransferToBudgetWithGoodGrades() throws Exception {
        studentBook.addRecord(new AcademicRecord(3, ControlType.EXAM, Grade.EXCELLENT));
        studentBook.addRecord(new AcademicRecord(2, ControlType.EXAM, Grade.GOOD));
        studentBook.addRecord(new AcademicRecord(1, ControlType.EXAM, Grade.EXCELLENT));

        assertTrue(studentBook.canTransferToBudget());
    }

    @Test
    void testCanTransferToBudgetWithOnlyExams() throws Exception {
        studentBook.addRecord(new AcademicRecord(3, ControlType.DIFF_CREDIT, Grade.GOOD));
        studentBook.addRecord(new AcademicRecord(3, ControlType.EXAM, Grade.EXCELLENT));
        studentBook.addRecord(new AcademicRecord(2, ControlType.EXAM, Grade.GOOD));

        assertTrue(studentBook.canTransferToBudget());
    }

    @Test
    void testCanGetRedDiplomaWithNoRecords() throws Exception {
        assertTrue(studentBook.canGetRedDiploma());
    }

    @Test
    void testCanGetRedDiplomaWithSatisfactoryGrade() throws Exception {
        studentBook.addRecord(new AcademicRecord(4, ControlType.EXAM, Grade.SATISFACTORY));
        studentBook.setGraduationWorkGrade(Grade.EXCELLENT);

        assertFalse(studentBook.canGetRedDiploma());
    }

    @Test
    void testCanGetRedDiplomaWithBadGraduationWork() throws Exception {
        studentBook.addRecord(new AcademicRecord(4, ControlType.EXAM, Grade.EXCELLENT));
        studentBook.setGraduationWorkGrade(Grade.GOOD);

        assertFalse(studentBook.canGetRedDiploma());
    }

    @Test
    void testCanGetRedDiplomaSuccessWithGoodGrades() throws Exception {
        studentBook.addRecord(new AcademicRecord(1, ControlType.EXAM, Grade.GOOD));
        studentBook.addRecord(new AcademicRecord(1, ControlType.DIFF_CREDIT, Grade.GOOD));

        assertTrue(studentBook.canGetRedDiploma());
    }

    @Test
    void testCanGetRedDiplomaSuccessWithExcellentGrades() throws Exception {
        studentBook.addRecord(new AcademicRecord(4, ControlType.EXAM, Grade.EXCELLENT));
        studentBook.addRecord(new AcademicRecord(4, ControlType.DIFF_CREDIT, Grade.EXCELLENT));
        studentBook.addRecord(new AcademicRecord(4, ControlType.EXAM, Grade.GOOD));
        studentBook.setGraduationWorkGrade(Grade.EXCELLENT);

        assertTrue(studentBook.canGetRedDiploma());
    }

    @Test
    void testCanGetRedDiplomaWithNoGraduationWorkGrade() throws Exception {
        studentBook.addRecord(new AcademicRecord(4, ControlType.EXAM, Grade.EXCELLENT));
        studentBook.addRecord(new AcademicRecord(4, ControlType.DIFF_CREDIT, Grade.GOOD));

        assertTrue(studentBook.canGetRedDiploma());
    }

    @Test
    void testRedDiplomaExactly75PercentExcellent() {
        for (int i = 0; i < 9; i++) {
            budgetStudentBook.addRecord(new AcademicRecord(1, ControlType.EXAM, Grade.EXCELLENT));
        }
        for (int i = 0; i < 3; i++) {
            budgetStudentBook.addRecord(new AcademicRecord(1, ControlType.EXAM, Grade.GOOD));
        }

        budgetStudentBook.setGraduationWorkGrade(Grade.EXCELLENT);
        assertTrue(budgetStudentBook.canGetRedDiploma());
    }

    @Test
    void testRedDiplomaBelow75Percent() {
        for (int i = 0; i < 8; i++) {
            budgetStudentBook.addRecord(new AcademicRecord(1, ControlType.EXAM, Grade.EXCELLENT));
        }
        for (int i = 0; i < 4; i++) {
            budgetStudentBook.addRecord(new AcademicRecord(1, ControlType.EXAM, Grade.GOOD));
        }

        budgetStudentBook.setGraduationWorkGrade(Grade.EXCELLENT);
        assertFalse(budgetStudentBook.canGetRedDiploma());
    }

    @Test
    void testRedDiplomaExactly75PercentExcellentButGraduationBookNotExcellent() {
        for (int i = 0; i < 9; i++) {
            budgetStudentBook.addRecord(new AcademicRecord(1, ControlType.EXAM, Grade.EXCELLENT));
        }
        for (int i = 0; i < 3; i++) {
            budgetStudentBook.addRecord(new AcademicRecord(1, ControlType.EXAM, Grade.GOOD));
        }

        budgetStudentBook.setGraduationWorkGrade(Grade.GOOD);
        assertFalse(budgetStudentBook.canGetRedDiploma());
    }

    @Test
    void testCanGetIncreasedScholarshipForPaidStudent() throws Exception {
        studentBook.addRecord(new AcademicRecord(1, ControlType.EXAM, Grade.EXCELLENT));

        assertFalse(studentBook.canGetIncreasedScholarship(1));
    }

    @Test
    void testCanGetIncreasedScholarshipWithBadGrades() throws Exception {
        budgetStudentBook.addRecord(new AcademicRecord(1, ControlType.EXAM, Grade.GOOD));
        budgetStudentBook.addRecord(new AcademicRecord(1,
                ControlType.DIFF_CREDIT, Grade.EXCELLENT));

        assertFalse(budgetStudentBook.canGetIncreasedScholarship(1));
    }

    @Test
    void testCanGetIncreasedScholarshipWithNotAllExams() throws Exception {
        budgetStudentBook.addRecord(new AcademicRecord(1, ControlType.EXAM, Grade.EXCELLENT));

        assertTrue(budgetStudentBook.canGetIncreasedScholarship(1));
    }

    @Test
    void testGetRecordsImmutable() {
        studentBook.addRecord(new AcademicRecord(1, ControlType.EXAM, Grade.EXCELLENT));

        var records = studentBook.getRecords();
        assertEquals(1, records.size());

        records.clear();
        assertEquals(1, studentBook.getRecords().size());
    }

    @Test
    void testGetGraduationWorkGrade() {
        assertNull(studentBook.getGraduationWorkGrade());

        studentBook.setGraduationWorkGrade(Grade.EXCELLENT);
        assertEquals(Grade.EXCELLENT, studentBook.getGraduationWorkGrade());
    }

    @Test
    void testMultipleSemesters() throws Exception {
        studentBook.addRecord(new AcademicRecord(1, ControlType.EXAM, Grade.GOOD));
        studentBook.addRecord(new AcademicRecord(1, ControlType.EXAM, Grade.GOOD));
        studentBook.addRecord(new AcademicRecord(2, ControlType.EXAM, Grade.EXCELLENT));
        studentBook.addRecord(new AcademicRecord(2, ControlType.EXAM, Grade.EXCELLENT));
        studentBook.addRecord(new AcademicRecord(3, ControlType.EXAM, Grade.EXCELLENT));
        studentBook.addRecord(new AcademicRecord(3, ControlType.EXAM, Grade.GOOD));

        assertEquals(4.5, studentBook.calculateAverage(), 0.01);
        assertTrue(studentBook.canGetRedDiploma());
    }

    @Test
    void testTransferToBudgetSuccess() throws Exception {
        StudentBook sb = new StudentBook("1", "A", StudyForm.PAID, 10);

        sb.addRecord(new AcademicRecord(2, ControlType.EXAM, Grade.EXCELLENT));
        sb.addRecord(new AcademicRecord(3, ControlType.EXAM, Grade.EXCELLENT));

        sb.transferToBudget();
        assertEquals(StudyForm.BUDGET, sb.getStudyForm());
    }

    @Test
    void testTransferToBudgetFailure() {
        StudentBook sb = new StudentBook("1", "A", StudyForm.PAID, 10);

        sb.addRecord(new AcademicRecord(2, ControlType.EXAM, Grade.SATISFACTORY));
        sb.addRecord(new AcademicRecord(3, ControlType.EXAM, Grade.EXCELLENT));

        assertThrows(AcademicBaseException.class, sb::transferToBudget);
    }
}
