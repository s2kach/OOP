package ru.nsu.dizmestev;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

class StudentBookTest {

    private StudentBook studentBook;

    @BeforeEach
    void setUp() {
        studentBook = new StudentBook(StudyForm.PAID);
    }

    @Test
    void testCalculateAverageWithNoRecords() {
        assertThrows(EmptyRecordException.class, () -> studentBook.calculateAverage());
    }

    @Test
    void testCalculateAverage() throws Exception {
        studentBook.addRecord(new AcademicRecord(1, ControlType.EXAM, Grade.EXCELLENT));
        studentBook.addRecord(new AcademicRecord(1, ControlType.EXAM, Grade.GOOD));

        assertEquals(4.5, studentBook.calculateAverage());
    }

    @Test
    void testCanTransferToBudgetWhenAlreadyBudget() throws Exception {
        StudentBook budgetBook = new StudentBook(StudyForm.BUDGET);
        assertFalse(budgetBook.canTransferToBudget());
    }

    @Test
    void testCanTransferToBudgetWithSatisfactoryInLastSemesters() throws Exception {
        studentBook.addRecord(new AcademicRecord(3, ControlType.EXAM, Grade.SATISFACTORY));
        studentBook.addRecord(new AcademicRecord(2, ControlType.EXAM, Grade.EXCELLENT));

        assertFalse(studentBook.canTransferToBudget());
    }

    @Test
    void testCanTransferToBudgetWithGoodGrades() throws Exception {
        studentBook.addRecord(new AcademicRecord(3, ControlType.EXAM, Grade.EXCELLENT));
        studentBook.addRecord(new AcademicRecord(2, ControlType.EXAM, Grade.GOOD));

        assertTrue(studentBook.canTransferToBudget());
    }

    @Test
    void testCanGetRedDiplomaWithNoRecords() {
        assertThrows(DiplomaCheckException.class, () -> studentBook.canGetRedDiploma());
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
    void testCanGetRedDiplomaSuccess() throws Exception {
        studentBook.addRecord(new AcademicRecord(4, ControlType.EXAM, Grade.EXCELLENT));
        studentBook.addRecord(new AcademicRecord(4, ControlType.DIFF_CREDIT, Grade.EXCELLENT));
        studentBook.addRecord(new AcademicRecord(4, ControlType.EXAM, Grade.EXCELLENT));
        studentBook.addRecord(new AcademicRecord(4, ControlType.DIFF_CREDIT, Grade.GOOD));
        studentBook.setGraduationWorkGrade(Grade.EXCELLENT);

        assertTrue(studentBook.canGetRedDiploma());
    }

    @Test
    void testCanGetIncreasedScholarshipForPaidStudent() throws Exception {
        studentBook.addRecord(new AcademicRecord(1, ControlType.EXAM, Grade.EXCELLENT));

        assertFalse(studentBook.canGetIncreasedScholarship(1));
    }

    @Test
    void testCanGetIncreasedScholarshipWithBadGrades() throws Exception {
        StudentBook budgetBook = new StudentBook(StudyForm.BUDGET);
        budgetBook.addRecord(new AcademicRecord(1, ControlType.EXAM, Grade.GOOD));

        assertFalse(budgetBook.canGetIncreasedScholarship(1));
    }

    @Test
    void testCanGetIncreasedScholarshipSuccess() throws Exception {
        StudentBook budgetBook = new StudentBook(StudyForm.BUDGET);
        budgetBook.addRecord(new AcademicRecord(1, ControlType.EXAM, Grade.EXCELLENT));
        budgetBook.addRecord(new AcademicRecord(1, ControlType.DIFF_CREDIT, Grade.EXCELLENT));

        assertTrue(budgetBook.canGetIncreasedScholarship(1));
    }
}