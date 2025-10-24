package ru.nsu.dizmestev;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Реализация графа через матрицу инцидентности.
 *
 * @param <V> Тип вершины.
 */
public class IncidenceMatrixGraph<V extends Vertex> implements Graph<V> {

    private final List<V> vertices = new ArrayList<>();
    private final List<List<Integer>> matrix = new ArrayList<>();
    private int edgeCount = 0;

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
        List<Integer> newRow = new ArrayList<>(Collections.nCopies(edgeCount, 0));
        matrix.add(newRow);
    }

    @Override
    public void removeVertex(V vertex) throws GraphException {
        int idx = indexOf(vertex);
        for (int e = edgeCount - 1; e >= 0; e--) {
            if (matrix.get(idx).get(e) != 0) {
                for (List<Integer> row : matrix) {
                    row.remove(e);
                }
                edgeCount--;
            }
        }
        vertices.remove(idx);
        matrix.remove(idx);
    }


    @Override
    public void addEdge(V from, V to) throws GraphException {
        int i = indexOf(from);
        int j = indexOf(to);

        if (from.equals(to)) {
            throw new GraphException("Нельзя добавить петлю из вершины в саму себя: " + from);
        }

        for (List<Integer> row : matrix) {
            row.add(0);
        }
        matrix.get(i).set(edgeCount, 1);
        matrix.get(j).set(edgeCount, -1);
        edgeCount++;
    }

    @Override
    public void removeEdge(V from, V to) throws GraphException {
        int i = indexOf(from);
        int j = indexOf(to);
        int removeIndex = -1;
        for (int e = 0; e < edgeCount; e++) {
            if (matrix.get(i).get(e) == 1 && matrix.get(j).get(e) == -1) {
                removeIndex = e;
                break;
            }
        }
        if (removeIndex == -1) {
            throw new GraphException("Ребро не найдено: " + from + " -> " + to);
        }
        for (List<Integer> row : matrix) {
            row.remove(removeIndex);
        }
        edgeCount--;
    }

    @Override
    public List<V> getNeighbors(V vertex) throws GraphException {
        int idx = indexOf(vertex);
        List<V> neighbors = new ArrayList<>();

        for (int e = 0; e < edgeCount; e++) {
            if (matrix.get(idx).get(e) == 1) {
                for (int k = 0; k < vertices.size(); k++) {
                    if (matrix.get(k).get(e) == -1) {
                        neighbors.add(vertices.get(k));
                    }
                }
            }
        }
        return neighbors;
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
        if (!(obj instanceof IncidenceMatrixGraph)) {
            return false;
        }
        IncidenceMatrixGraph<?> other = (IncidenceMatrixGraph<?>) obj;
        return vertices.equals(other.vertices) && matrix.equals(other.matrix);
    }
}