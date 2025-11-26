package ru.nsu.dizmestev;

import java.util.ArrayList;
import java.util.List;

/**
 * Реализация электронной зачетной книжки студента.
 */
public class StudentBook {

    private final List<AcademicRecord> records;
    private StudyForm studyForm;
    private Grade graduationWorkGrade;

    /**
     * Создает пустую зачетную книжку.
     *
     * @param studyForm Форма обучения.
     */
    public StudentBook(StudyForm studyForm) {
        this.records = new ArrayList<AcademicRecord>();
        this.studyForm = studyForm;
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
     * @throws EmptyRecordException При отсутствии оценок.
     */
    public double calculateAverage() throws EmptyRecordException {
        if (records.isEmpty()) {
            throw new EmptyRecordException("Нет оценок для расчета.");
        }
        int sum = 0;
        for (AcademicRecord r : records) {
            sum += r.getGrade().getValue();
        }
        return (double) sum / records.size();
    }

    /**
     * Проверяет возможность перевода на бюджет.
     *
     * @return Возможность перевода.
     * @throws AcademicBaseException При ошибке анализа.
     */
    public boolean canTransferToBudget() throws AcademicBaseException {
        try {
            if (studyForm == StudyForm.BUDGET) {
                return false;
            }

            int lastSemester = getLastSemester();
            for (int semester = lastSemester; semester > lastSemester - 2; semester--) {
                for (AcademicRecord r : records) {
                    if (r.getSemester() == semester && r.getType() == ControlType.EXAM) {
                        if (r.getGrade() == Grade.SATISFACTORY) {
                            return false;
                        }
                    }
                }
            }
            return true;
        } catch (Exception e) {
            throw new AcademicBaseException("Ошибка проверки перевода.", e);
        }
    }

    /**
     * Проверяет возможность получения красного диплома.
     *
     * @return Возможность получения.
     * @throws AcademicBaseException При ошибке анализа.
     */
    public boolean canGetRedDiploma() throws AcademicBaseException {
        try {
            if (records.isEmpty()) {
                throw new EmptyRecordException("Нет оценок для анализа диплома.");
            }

            int lastSemester = getLastSemester();
            List<AcademicRecord> diplomaRecords = new ArrayList<>();
            for (AcademicRecord r : records) {
                if (r.getSemester() == lastSemester) {
                    diplomaRecords.add(r);
                }
            }

            if (diplomaRecords.isEmpty()) {
                throw new EmptyRecordException("Нет оценок за последний семестр.");
            }

            int excellent = 0;
            for (AcademicRecord r : diplomaRecords) {
                if (r.getGrade() == Grade.SATISFACTORY) {
                    return false;
                }
                if (r.getGrade() == Grade.EXCELLENT) {
                    excellent++;
                }
            }

            double percent = (double) excellent / diplomaRecords.size();
            return graduationWorkGrade == Grade.EXCELLENT && percent >= 0.75;
        } catch (Exception e) {
            throw new DiplomaCheckException("Ошибка проверки диплома.", e);
        }
    }

    /**
     * Проверяет возможность получения повышенной стипендии.
     *
     * @param currentSemester Текущий семестр.
     * @return Возможность получения.
     * @throws AcademicBaseException При ошибке анализа.
     */
    public boolean canGetIncreasedScholarship(int currentSemester) throws AcademicBaseException {
        try {
            if (studyForm != StudyForm.BUDGET) {
                return false;
            }

            boolean hasRecordsInSemester = false;
            for (AcademicRecord r : records) {
                if (r.getSemester() == currentSemester) {
                    hasRecordsInSemester = true;
                    if (r.getGrade() != Grade.EXCELLENT) {
                        return false;
                    }
                }
            }

            return hasRecordsInSemester;
        } catch (Exception e) {
            throw new AcademicBaseException("Ошибка проверки стипендии.", e);
        }
    }

    /**
     * Возвращает номер последнего семестра.
     *
     * @return Номер семестра.
     */
    private int getLastSemester() {
        int max = 0;
        for (AcademicRecord r : records) {
            if (r.getSemester() > max) {
                max = r.getSemester();
            }
        }
        return max;
    }
}
