package ru.nsu.dizmestev;

/**
 * Класс студента.
 */
public class Student {
    private final String github;
    private final String name;
    private final String repoUrl;

    /**
     * Конструктор студента.
     *
     * @param github Никнейм на GitHub.
     * @param name ФИО студента.
     * @param repoUrl Ссылка на репозиторий.
     */
    public Student(String github, String name, String repoUrl) {
        this.github = github;
        this.name = name;
        this.repoUrl = repoUrl;
    }

    /**
     * Получает никнейм.
     *
     * @return GitHub никнейм.
     */
    public String getGithub() {
        return github;
    }

    /**
     * Получает имя.
     *
     * @return Имя.
     */
    public String getName() {
        return name;
    }

    /**
     * Получает ссылку на репозиторий.
     *
     * @return URL репозитория.
     */
    public String getRepoUrl() {
        return repoUrl;
    }
}
