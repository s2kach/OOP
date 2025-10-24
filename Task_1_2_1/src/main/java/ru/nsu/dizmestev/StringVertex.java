package ru.nsu.dizmestev;

/**
 * Реализация вершины графа на основе строки.
 */
public class StringVertex implements Vertex {
    private final String name;

    /**
     * Создаёт вершину с указанным именем.
     *
     * @param name Имя вершины.
     */
    public StringVertex(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof StringVertex other)) {
            return false;
        }
        return name.equals(other.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}
