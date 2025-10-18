package ru.nsu.dizmestev;

import java.io.IOException;
import java.util.List;

/**
 * Интерфейс графа.
 */
public interface Graph {

    /**
     * Добавляет вершину в граф.
     *
     * @param vertex Вершина для добавления.
     * @throws GraphException Если вершина уже существует или произошла ошибка.
     */
    void addVertex(String vertex) throws GraphException;

    /**
     * Удаляет вершину из графа.
     *
     * @param vertex Вершина для удаления.
     * @throws GraphException Если вершина отсутствует или произошла ошибка.
     */
    void removeVertex(String vertex) throws GraphException;

    /**
     * Добавляет ребро между двумя вершинами.
     *
     * @param from Начальная вершина.
     * @param to   Конечная вершина.
     * @throws GraphException Если вершины отсутствуют или ребро уже существует.
     */
    void addEdge(String from, String to) throws GraphException;

    /**
     * Удаляет ребро между двумя вершинами.
     *
     * @param from Начальная вершина.
     * @param to   Конечная вершина.
     * @throws GraphException Если ребро отсутствует.
     */
    void removeEdge(String from, String to) throws GraphException;

    /**
     * Возвращает список соседей вершины.
     *
     * @param vertex Вершина, для которой ищутся соседи.
     * @return Список соседей вершины.
     * @throws GraphException Если вершина не существует.
     */
    List<String> getNeighbors(String vertex) throws GraphException;

    /**
     * Загружает граф из файла фиксированного формата (по одной вершине и ребру в строке).
     *
     * @param path Путь к файлу.
     * @throws GraphException Если произошла ошибка чтения или формат некорректен.
     */
    void loadFromFile(String path) throws GraphException, IOException;

    /**
     * Выполняет топологическую сортировку графа.
     *
     * @return Список вершин в топологическом порядке.
     * @throws GraphException Если граф содержит цикл.
     */
    List<String> topologicalSort() throws GraphException;

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
