package ru.nsu.dizmestev;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

/**
 * Реализация графа через матрицу смежности.
 */
public class AdjacencyMatrixGraph implements Graph {

    private final List<String> vertices = new ArrayList<>();
    private final List<List<Integer>> matrix = new ArrayList<>();

    /**
     * Возвращает индекс вершины.
     *
     * @param vertex Имя вершины.
     * @return Индекс вершины.
     * @throws GraphException Если вершина не найдена.
     */
    private int indexOf(String vertex) throws GraphException {
        int idx = vertices.indexOf(vertex);
        if (idx == -1) {
            throw new GraphException("Вершина не найдена: " + vertex);
        }
        return idx;
    }

    @Override
    public void addVertex(String vertex) throws GraphException {
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
    public void removeVertex(String vertex) throws GraphException {
        int idx = indexOf(vertex);
        vertices.remove(idx);
        matrix.remove(idx);
        for (List<Integer> row : matrix) {
            row.remove(idx);
        }
    }

    @Override
    public void addEdge(String from, String to) throws GraphException {
        int i = indexOf(from);
        int j = indexOf(to);
        if (matrix.get(i).get(j) == 1) {
            throw new GraphException("Ребро уже существует: " + from + " -> " + to);
        }
        matrix.get(i).set(j, 1);
    }

    @Override
    public void removeEdge(String from, String to) throws GraphException {
        int i = indexOf(from);
        int j = indexOf(to);
        if (matrix.get(i).get(j) == 0) {
            throw new GraphException("Ребро не найдено: " + from + " -> " + to);
        }
        matrix.get(i).set(j, 0);
    }

    @Override
    public List<String> getNeighbors(String vertex) throws GraphException {
        int idx = indexOf(vertex);
        List<String> result = new ArrayList<>();
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
                    addVertex(parts[0]);
                } else if (parts.length == 2) {
                    if (!vertices.contains(parts[0])) {
                        addVertex(parts[0]);
                    }
                    if (!vertices.contains(parts[1])) {
                        addVertex(parts[1]);
                    }
                    addEdge(parts[0], parts[1]);
                } else {
                    throw new GraphException("Некорректная строка: " + line);
                }
            }
        } catch (IOException e) {
            throw new GraphException("Ошибка чтения файла.", e);
        }
    }

    @Override
    public List<String> topologicalSort() throws GraphException {
        Map<String, Integer> indegree = new HashMap<>();
        for (String v : vertices) {
            indegree.put(v, 0);
        }
        for (int i = 0; i < vertices.size(); i++) {
            for (int j = 0; j < vertices.size(); j++) {
                if (matrix.get(i).get(j) == 1) {
                    indegree.put(vertices.get(j), indegree.get(vertices.get(j)) + 1);
                }
            }
        }

        Queue<String> queue = new LinkedList<>();
        for (String v : vertices) {
            if (indegree.get(v) == 0) {
                queue.add(v);
            }
        }

        List<String> sorted = new ArrayList<>();
        while (!queue.isEmpty()) {
            String v = queue.poll();
            sorted.add(v);
            int i = vertices.indexOf(v);
            for (int j = 0; j < vertices.size(); j++) {
                if (matrix.get(i).get(j) == 1) {
                    indegree.put(vertices.get(j), indegree.get(vertices.get(j)) - 1);
                    if (indegree.get(vertices.get(j)) == 0) {
                        queue.add(vertices.get(j));
                    }
                }
            }
        }

        if (sorted.size() != vertices.size()) {
            throw new GraphException("Граф содержит цикл.");
        }
        return sorted;
    }

    @Override
    public String toString() {
        return "Vertices=" + vertices + ", Matrix=" + matrix;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof AdjacencyMatrixGraph other)) {
            return false;
        }
        return vertices.equals(other.vertices) && matrix.equals(other.matrix);
    }
}