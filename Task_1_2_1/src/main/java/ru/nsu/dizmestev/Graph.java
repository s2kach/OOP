package ru.nsu.dizmestev;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

/**
 * Интерфейс графа.
 *
 * @param <V> Тип вершины.
 */
public interface Graph<V extends Vertex> {

    /**
     * Добавляет вершину в граф.
     *
     * @param vertex Вершина для добавления.
     * @throws GraphException Если вершина уже существует или произошла ошибка.
     */
    void addVertex(V vertex) throws GraphException;

    /**
     * Удаляет вершину из графа.
     *
     * @param vertex Вершина для удаления.
     * @throws GraphException Если вершина отсутствует или произошла ошибка.
     */
    void removeVertex(V vertex) throws GraphException;

    /**
     * Добавляет ребро между двумя вершинами.
     *
     * @param from Начальная вершина.
     * @param to   Конечная вершина.
     * @throws GraphException Если вершины отсутствуют или ребро уже существует.
     */
    void addEdge(V from, V to) throws GraphException;

    /**
     * Удаляет ребро между двумя вершинами.
     *
     * @param from Начальная вершина.
     * @param to   Конечная вершина.
     * @throws GraphException Если ребро отсутствует.
     */
    void removeEdge(V from, V to) throws GraphException;

    /**
     * Возвращает список соседей вершины.
     *
     * @param vertex Вершина, для которой ищутся соседи.
     * @return Список соседей вершины.
     * @throws GraphException Если вершина не существует.
     */
    List<V> getNeighbors(V vertex) throws GraphException;

    /**
     * Загружает граф из файла фиксированного формата (по одной вершине и ребру в строке).
     *
     * @param path Путь к файлу.
     * @throws GraphException Если произошла ошибка чтения или формат некорректен.
     * @throws IOException    Если произошла ошибка ввода-вывода.
     */
    void loadFromFile(String path) throws GraphException, IOException;

    /**
     * Возвращает список всех вершин графа.
     *
     * @return Список всех вершин.
     */
    List<V> getVertices();

    /**
     * Выполняет топологическую сортировку графа.
     *
     * @return Список вершин в топологическом порядке.
     * @throws GraphException Если граф содержит цикл.
     */
    default List<V> topologicalSort() throws GraphException {
        Map<V, Integer> indegree = new HashMap<>();
        List<V> allVertices = getVertices();

        for (V v : allVertices) {
            indegree.put(v, 0);
        }

        for (V v : allVertices) {
            for (V neighbor : getNeighbors(v)) {
                indegree.put(neighbor, indegree.get(neighbor) + 1);
            }
        }

        Queue<V> queue = new LinkedList<>();
        for (V v : allVertices) {
            if (indegree.get(v) == 0) {
                queue.add(v);
            }
        }

        List<V> result = new ArrayList<>();

        while (!queue.isEmpty()) {
            V v = queue.poll();
            result.add(v);
            for (V neighbor : getNeighbors(v)) {
                indegree.put(neighbor, indegree.get(neighbor) - 1);
                if (indegree.get(neighbor) == 0) {
                    queue.add(neighbor);
                }
            }
        }

        if (result.size() != allVertices.size()) {
            throw new GraphException("Граф содержит цикл — топологическая сортировка невозможна.");
        }

        return result;
    }


    /**
     * Преобразует граф в строковое представление.
     *
     * @return Строковое представление графа.
     */
    String toString();

    /**
     * Проверяет равенство двух графов.
     *
     * @param obj Объект для сравнения.
     * @return true, если графы равны.
     */
    boolean equals(Object obj);
}