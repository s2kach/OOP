package ru.nsu.dizmestev;

/**
 * Хранит информацию об одной итоговой оценке.
 */
public class AcademicRecord {

    private final int semester;
    private final ControlType type;
    private final Grade grade;

    /**
     * Создает запись об оценке.
     *
     * @param semester Номер семестра.
     * @param type Тип контроля.
     * @param grade Оценка.
     */
    public AcademicRecord(int semester, ControlType type, Grade grade) {
        this.semester = semester;
        this.type = type;
        this.grade = grade;
    }

    /**
     * Возвращает номер семестра.
     *
     * @return Номер семестра.
     */
    public int getSemester() {
        return semester;
    }

    /**
     * Возвращает тип контроля.
     *
     * @return Тип контроля.
     */
    public ControlType getType() {
        return type;
    }

    /**
     * Возвращает оценку.
     *
     * @return Оценка.
     */
    public Grade getGrade() {
        return grade;
    }
}