package ru.nsu.dizmestev;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * Реализация графа через матрицу инцидентности.
 */
public class IncidenceMatrixGraph implements Graph {

    private final List<String> vertices = new ArrayList<>();
    private final List<List<Integer>> matrix = new ArrayList<>();
    private int edgeCount = 0;

    /**
     * Возвращает индекс вершины.
     *
     * @param vertex Имя вершины.
     * @return Индекс вершины.
     * @throws GraphException Если вершина не найдена.
     */
    private int indexOf(String vertex) throws GraphException {
        int idx = vertices.indexOf(vertex);
        if (idx == -1) throw new GraphException("Вершина не найдена: " + vertex);
        return idx;
    }

    @Override
    public void addVertex(String vertex) throws GraphException {
        if (vertices.contains(vertex)) throw new GraphException("Вершина уже существует: " + vertex);
        vertices.add(vertex);
        List<Integer> newRow = new ArrayList<>(Collections.nCopies(edgeCount, 0));
        matrix.add(newRow);
    }

    @Override
    public void removeVertex(String vertex) throws GraphException {
        int idx = indexOf(vertex);
        vertices.remove(idx);
        matrix.remove(idx);
    }

    @Override
    public void addEdge(String from, String to) throws GraphException {
        int i = indexOf(from);
        int j = indexOf(to);
        for (List<Integer> row : matrix) row.add(0);
        matrix.get(i).set(edgeCount, 1);
        matrix.get(j).set(edgeCount, -1);
        edgeCount++;
    }

    @Override
    public void removeEdge(String from, String to) throws GraphException {
        int i = indexOf(from);
        int j = indexOf(to);
        int removeIndex = -1;
        for (int e = 0; e < edgeCount; e++) {
            if (matrix.get(i).get(e) == 1 && matrix.get(j).get(e) == -1) {
                removeIndex = e;
                break;
            }
        }
        if (removeIndex == -1) throw new GraphException("Ребро не найдено: " + from + " -> " + to);
        for (List<Integer> row : matrix) row.remove(removeIndex);
        edgeCount--;
    }

    @Override
    public List<String> getNeighbors(String vertex) throws GraphException {
        int idx = indexOf(vertex);
        List<String> neighbors = new ArrayList<>();
        for (int e = 0; e < edgeCount; e++) {
            if (matrix.get(idx).get(e) == 1) {
                for (int k = 0; k < vertices.size(); k++) {
                    if (matrix.get(k).get(e) == -1) neighbors.add(vertices.get(k));
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
                    addVertex(parts[0]);
                } else if (parts.length == 2) {
                    if (!vertices.contains(parts[0])) addVertex(parts[0]);
                    if (!vertices.contains(parts[1])) addVertex(parts[1]);
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
        AdjacencyListGraph temp = new AdjacencyListGraph();
        for (String v : vertices) temp.addVertex(v);
        for (int e = 0; e < edgeCount; e++) {
            String from = null, to = null;
            for (int i = 0; i < vertices.size(); i++) {
                if (matrix.get(i).get(e) == 1) from = vertices.get(i);
                if (matrix.get(i).get(e) == -1) to = vertices.get(i);
            }
            if (from != null && to != null) temp.addEdge(from, to);
        }
        return temp.topologicalSort();
    }

    @Override
    public String toString() {
        return "Vertices=" + vertices + ", Matrix=" + matrix;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof IncidenceMatrixGraph other)) return false;
        return vertices.equals(other.vertices) && matrix.equals(other.matrix);
    }
}
