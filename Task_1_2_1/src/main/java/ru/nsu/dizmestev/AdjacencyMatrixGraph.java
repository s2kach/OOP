package ru.nsu.dizmestev;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Реализация графа через матрицу смежности.
 *
 * @param <V> Тип вершины.
 */
public class AdjacencyMatrixGraph<V extends Vertex> implements Graph<V> {

    private final List<V> vertices = new ArrayList<>();
    private final List<List<Integer>> matrix = new ArrayList<>();

    /**
     * Возвращает индекс вершины.
     *
     * @param vertex Вершина.
     * @return Индекс вершины.
     * @throws GraphException Если вершина не найдена.
     */
    private int indexOf(V vertex) throws GraphException {
        for (int i = 0; i < vertices.size(); i++) {
            if (vertices.get(i).equals(vertex)) {
                return i;
            }
        }
        throw new GraphException("Вершина не найдена: " + vertex);
    }

    @Override
    public void addVertex(V vertex) throws GraphException {
        if (vertices.contains(vertex)) {
            throw new GraphException("Вершина уже существует: " + vertex);
        }
        vertices.add(vertex);
        for (List<Integer> row : matrix) {
            row.add(0);
        }
        List<Integer> newRow = new ArrayList<>(Collections.nCopies(vertices.size(), 0));
        matrix.add(newRow);
    }

    @Override
    public void removeVertex(V vertex) throws GraphException {
        int idx = indexOf(vertex);
        vertices.remove(idx);
        matrix.remove(idx);
        for (List<Integer> row : matrix) {
            row.remove(idx);
        }
    }

    @Override
    public void addEdge(V from, V to) throws GraphException {
        int i = indexOf(from);
        int j = indexOf(to);
        if (matrix.get(i).get(j) == 1) {
            throw new GraphException("Ребро уже существует: " + from + " -> " + to);
        }
        matrix.get(i).set(j, 1);
    }

    @Override
    public void removeEdge(V from, V to) throws GraphException {
        int i = indexOf(from);
        int j = indexOf(to);
        if (matrix.get(i).get(j) == 0) {
            throw new GraphException("Ребро не найдено: " + from + " -> " + to);
        }
        matrix.get(i).set(j, 0);
    }

    @Override
    public List<V> getNeighbors(V vertex) throws GraphException {
        int idx = indexOf(vertex);
        List<V> result = new ArrayList<>();
        for (int j = 0; j < vertices.size(); j++) {
            if (matrix.get(idx).get(j) == 1) {
                result.add(vertices.get(j));
            }
        }
        return result;
    }

    @Override
    public void loadFromFile(String path) throws GraphException, IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.trim().split(" ");
                if (parts.length == 1) {
                    addVertex((V) new StringVertex(parts[0]));
                } else if (parts.length == 2) {
                    V fromVertex = (V) new StringVertex(parts[0]);
                    V toVertex = (V) new StringVertex(parts[1]);
                    if (!vertices.contains(fromVertex)) {
                        addVertex(fromVertex);
                    }
                    if (!vertices.contains(toVertex)) {
                        addVertex(toVertex);
                    }
                    addEdge(fromVertex, toVertex);
                } else {
                    throw new GraphException("Некорректная строка: " + line);
                }
            }
        } catch (IOException e) {
            throw new GraphException("Ошибка чтения файла.", e);
        }
    }

    @Override
    public List<V> getVertices() {
        return new ArrayList<>(vertices);
    }

    @Override
    public String toString() {
        return "Vertices=" + vertices + ", Matrix=" + matrix;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof AdjacencyMatrixGraph)) {
            return false;
        }
        AdjacencyMatrixGraph<?> other = (AdjacencyMatrixGraph<?>) obj;
        return vertices.equals(other.vertices) && matrix.equals(other.matrix);
    }
}