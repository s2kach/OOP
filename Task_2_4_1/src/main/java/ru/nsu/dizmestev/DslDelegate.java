package ru.nsu.dizmestev;

import groovy.lang.Binding;
import groovy.lang.Closure;
import groovy.lang.GroovyShell;
import groovy.util.DelegatingScript;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import org.codehaus.groovy.control.CompilerConfiguration;

/**
 * Делегат для обработки Groovy DSL.
 */
public class DslDelegate {
    private final CourseConfig config;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    /**
     * Конструктор.
     *
     * @param config Конфигурация курса.
     */
    public DslDelegate(CourseConfig config) {
        this.config = config;
    }

    /**
     * Импортирует другой файл конфигурации.
     *
     * @param filename Имя файла.
     * @throws CheckerException При ошибке чтения файла.
     */
    public void include(String filename) throws CheckerException {
        try {
            CompilerConfiguration compilerConfig = new CompilerConfiguration();
            compilerConfig.setScriptBaseClass(DelegatingScript.class.getName());
            GroovyShell shell = new GroovyShell(DslDelegate.class.getClassLoader(),
                    new Binding(), compilerConfig);

            DelegatingScript script;
            File externalFile = new File(filename);

            if (externalFile.exists()) {
                script = (DelegatingScript) shell.parse(externalFile);
            } else {
                var resource = getClass().getClassLoader().getResource(filename);
                if (resource == null) throw new IOException("Файл не найден: " + filename);
                script = (DelegatingScript)
                        shell.parse(new InputStreamReader(resource.openStream()));
            }

            script.setDelegate(this);
            script.run();
        } catch (Exception e) {
            throw new CheckerException("Ошибка загрузки конфига: " + filename, e);
        }
    }

    /**
     * Блок задач.
     *
     * @param closure Замыкание.
     */
    public void tasks(Closure<?> closure) {
        executeClosure(closure);
    }

    /**
     * Создает задачу.
     *
     * @param params Параметры из DSL.
     */
    public void task(Map<String, Object> params) {
        String id = (String) params.get("id");
        String name = (String) params.get("name");
        int maxPoints = (Integer) params.get("maxPoints");
        LocalDate soft = LocalDate.parse((String) params.get("softDeadline"), formatter);
        LocalDate hard = LocalDate.parse((String) params.get("hardDeadline"), formatter);
        config.addTask(new Task(id, name, maxPoints, soft, hard));
    }

    /**
     * Блок групп.
     *
     * @param closure Замыкание.
     */
    public void groups(Closure<?> closure) {
        executeClosure(closure);
    }

    /**
     * Создает группу (пока служит оберткой для студентов).
     *
     * @param params Параметры.
     * @param closure Замыкание студентов.
     */
    public void group(Map<String, Object> params, Closure<?> closure) {
        executeClosure(closure);
    }

    /**
     * Создает студента.
     *
     * @param params Параметры.
     */
    public void student(Map<String, Object> params) {
        String github = (String) params.get("github");
        String name = (String) params.get("name");
        String repoUrl = (String) params.get("repoUrl");
        config.addStudent(new Student(github, name, repoUrl));
    }

    /**
     * Блок проверок.
     *
     * @param closure Замыкание.
     */
    public void check(Closure<?> closure) {
        executeClosure(closure);
    }

    /**
     * Назначает проверку.
     *
     * @param params Параметры назначения.
     */
    @SuppressWarnings("unchecked")
    public void assign(Map<String, Object> params) {
        String taskId = (String) params.get("taskId");
        List<String> students = (List<String>) params.get("students");
        config.assignCheck(taskId, students);
    }

    /**
     * Выполняет замыкание.
     *
     * @param closure Замыкание.
     */
    private void executeClosure(Closure<?> closure) {
        closure.setDelegate(this);
        closure.setResolveStrategy(Closure.DELEGATE_FIRST);
        closure.call();
    }

    /**
     * Блок описания контрольных точек в DSL.
     *
     * @param closure Замыкание.
     */
    public void checkpoints(Closure<?> closure) {
        closure.setDelegate(this);
        closure.call();
    }

    /**
     * Добавляет КТ через DSL: checkpoint(name: "...", date: "...")
     *
     * @param params параметры.
     */
    public void checkpoint(Map<String, Object> params) {
        String name = (String) params.get("name");
        LocalDate date = LocalDate.parse((String) params.get("date"), formatter);
        config.addCheckpoint(new Checkpoint(name, date));
    }

    /**
     * Дополнительная настройка: выставление бонусных баллов.
     *
     * @param params параметры.
     */
    public void extraPoints(Map<String, Object> params) {
        String student = (String) params.get("student");
        String task = (String) params.get("task");
        double pts = ((Number) params.get("points")).doubleValue();
        config.addExtraPoints(student, task, pts);
    }
}
