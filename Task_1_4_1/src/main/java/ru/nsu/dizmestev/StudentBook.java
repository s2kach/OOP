package ru.nsu.dizmestev;

import java.util.ArrayList;
import java.util.List;
import java.util.OptionalDouble;
import java.util.stream.Collectors;

/**
 * Реализация электронной зачетной книжки студента.
 */
public class StudentBook {

    private final List<AcademicRecord> records;
    private final StudyForm studyForm;
    private final int totalDiplomaGrades;
    private Grade graduationWorkGrade;
    private final String studentId;
    private final String studentName;

    /**
     * Создает пустую зачетную книжку.
     *
     * @param studentId ID студента
     * @param studentName Имя студента
     * @param studyForm Форма обучения.
     */
    public StudentBook(String studentId, String studentName, StudyForm studyForm,
                       int totalDiplomaGrades) {
        this.totalDiplomaGrades = totalDiplomaGrades;
        this.records = new ArrayList<>();
        this.studyForm = studyForm;
        this.studentId = studentId;
        this.studentName = studentName;
    }

    /**
     * Добавляет новую оценку.
     *
     * @param record Запись об оценке.
     */
    public void addRecord(AcademicRecord record) {
        records.add(record);
    }

    /**
     * Устанавливает оценку за выпускную работу.
     *
     * @param grade Оценка за ВКР.
     */
    public void setGraduationWorkGrade(Grade grade) {
        this.graduationWorkGrade = grade;
    }

    /**
     * Вычисляет текущий средний балл.
     *
     * @return Средний балл.
     * @throws AcademicBaseException При отсутствии оценок.
     */
    public double calculateAverage() throws AcademicBaseException {
        if (records.isEmpty()) {
            throw new AcademicBaseException("Нет оценок для расчета.");
        }

        OptionalDouble average = records.stream()
                .mapToInt(record -> record.getGrade().getValue())
                .average();

        return average.orElseThrow(() -> new AcademicBaseException("Нет оценок для расчета."));
    }

    /**
     * Проверяет возможность перевода на бюджет.
     *
     * @return Возможность перевода.
     */
    public boolean canTransferToBudget() {
        if (studyForm == StudyForm.BUDGET) {
            return false;
        }

        int lastSemester = getLastSemester();

        boolean hasSatisfactoryInLastTwoSemesters = records.stream()
                .filter(record -> record.getSemester() >= lastSemester - 1
                        && record.getSemester() <= lastSemester)
                .filter(record -> record.getType() == ControlType.EXAM)
                .anyMatch(record -> record.getGrade() == Grade.SATISFACTORY);

        return !hasSatisfactoryInLastTwoSemesters;
    }

    /**
     * Проверяет потенциальную возможность получения красного диплома.
     * Исключаем все ситуации когда студент уже не может улучшить оценки в будущем.
     *
     * @return Потенциальная возможность получения.
     */
    public boolean canGetRedDiploma() {
        boolean hasSatisfactory = records.stream()
                .anyMatch(record -> record.getGrade() == Grade.SATISFACTORY);

        if (hasSatisfactory) {
            return false;
        }

        if (graduationWorkGrade != null && graduationWorkGrade != Grade.EXCELLENT) {
            return false;
        }

        List<AcademicRecord> forDiploma = records.stream()
                .filter(r -> r.getType() == ControlType.EXAM
                        || r.getType() == ControlType.DIFF_CREDIT)
                .collect(Collectors.toList());

        int done = forDiploma.size();
        int excellent = (int) forDiploma.stream()
                .filter(r -> r.getGrade() == Grade.EXCELLENT)
                .count();

        int remaining = totalDiplomaGrades - done;
        if (remaining < 0) {
            return false;
        }

        int maxExcellent = excellent + remaining;
        return maxExcellent >= 0.75 * totalDiplomaGrades;
    }

    /**
     * Проверяет возможность получения повышенной стипендии.
     *
     * @param currentSemester Текущий семестр.
     * @return Возможность получения.
     */
    public boolean canGetIncreasedScholarship(int currentSemester) {
        if (studyForm != StudyForm.BUDGET) {
            return false;
        }

        List<AcademicRecord> currentSemesterRecords = records.stream()
                .filter(record -> record.getSemester() == currentSemester)
                .collect(Collectors.toList());

        if (currentSemesterRecords.isEmpty()) {
            return false;
        }

        boolean allExcellent = currentSemesterRecords.stream()
                .allMatch(record -> record.getGrade() == Grade.EXCELLENT);

        return allExcellent;
    }

    /**
     * Возвращает номер последнего семестра.
     *
     * @return Номер семестра.
     */
    private int getLastSemester() {
        return records.stream()
                .mapToInt(AcademicRecord::getSemester)
                .max()
                .orElse(0);
    }

    /**
     * Получить ID студента.
     *
     * @return ID студента
     */
    public String getStudentId() {
        return studentId;
    }

    /**
     * Получить имя студента.
     *
     * @return имя студента.
     */
    public String getStudentName() {
        return studentName;
    }

    /**
     * Получить форму обучения.
     *
     * @return форма обучения.
     */
    public StudyForm getStudyForm() {
        return studyForm;
    }

    /**
     * Получить все записи об оценках.
     *
     * @return список записей.
     */
    public List<AcademicRecord> getRecords() {
        return new ArrayList<>(records);
    }

    /**
     * Получить оценку за выпускную работу.
     *
     * @return оценка за выпускную работу.
     */
    public Grade getGraduationWorkGrade() {
        return graduationWorkGrade;
    }
}