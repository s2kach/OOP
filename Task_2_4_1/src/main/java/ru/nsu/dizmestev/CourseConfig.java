package ru.nsu.dizmestev;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Главный класс конфигурации курса.
 */
public class CourseConfig {
    private final Map<String, List<String>> assignments = new HashMap<>();
    private final List<Student> students = new ArrayList<>();
    private final List<Task> tasks = new ArrayList<>();
    private final List<Checkpoint> checkpoints = new ArrayList<>();
    private final Map<String, Map<String, Double>> extraPoints = new HashMap<>();

    /**
     * Добавляет контрольную точку в план курса.
     */
    public void addCheckpoint(Checkpoint cp) {
        checkpoints.add(cp);
    }

    /**
     * Выставляет дополнительные баллы студенту за конкретную задачу.
     */
    public void addExtraPoints(String github, String taskId, double points) {
        extraPoints.computeIfAbsent(github, k -> new HashMap<>()).put(taskId, points);
    }

    public double getExtra(String github, String taskId) {
        return extraPoints.getOrDefault(github, new HashMap<>()).getOrDefault(taskId, 0.0);
    }

    /**
     * Добавляет студента.
     *
     * @param student Объект студента.
     */
    public void addStudent(Student student) {
        students.add(student);
    }

    /**
     * Добавляет задачу.
     *
     * @param task Объект задачи.
     */
    public void addTask(Task task) {
        tasks.add(task);
    }

    /**
     * Назначает проверку.
     *
     * @param taskId Идентификатор задачи.
     * @param studentGithubs Список никнеймов студентов.
     */
    public void assignCheck(String taskId, List<String> studentGithubs) {
        assignments.put(taskId, studentGithubs);
    }

    /**
     * Возвращает всех студентов.
     *
     * @return Список студентов.
     */
    public List<Student> getStudents() {
        return students;
    }

    /**
     * Возвращает все назначения.
     *
     * @return Карта назначений.
     */
    public Map<String, List<String>> getAssignments() {
        return assignments;
    }

    public List<Checkpoint> getCheckpoints() {
        return checkpoints;
    }

    public List<Task> getTasks() {
        return tasks;
    }
}
