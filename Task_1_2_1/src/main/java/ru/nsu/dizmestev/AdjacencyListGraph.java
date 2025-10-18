package ru.nsu.dizmestev;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Queue;

/**
 * Реализация графа через список смежности.
 */
public class AdjacencyListGraph implements Graph {
    private final Map<String, Set<String>> adjacencyList = new HashMap<>();

    @Override
    public void addVertex(String vertex) throws GraphException {
        if (adjacencyList.containsKey(vertex)) {
            throw new GraphException("Вершина уже существует: " + vertex);
        }
        adjacencyList.put(vertex, new HashSet<>());
    }

    @Override
    public void removeVertex(String vertex) throws GraphException {
        if (!adjacencyList.containsKey(vertex)) {
            throw new GraphException("Вершина не найдена: " + vertex);
        }
        adjacencyList.remove(vertex);
        for (Set<String> neighbors : adjacencyList.values()) {
            neighbors.remove(vertex);
        }
    }

    @Override
    public void addEdge(String from, String to) throws GraphException {
        if (!adjacencyList.containsKey(from) || !adjacencyList.containsKey(to)) {
            throw new GraphException("Одна или обе вершины не существуют.");
        }
        if (!adjacencyList.get(from).add(to)) {
            throw new GraphException("Ребро уже существует: " + from + " -> " + to);
        }
    }

    @Override
    public void removeEdge(String from, String to) throws GraphException {
        if (!adjacencyList.containsKey(from) || !adjacencyList.get(from).remove(to)) {
            throw new GraphException("Ребро не найдено: " + from + " -> " + to);
        }
    }

    @Override
    public List<String> getNeighbors(String vertex) throws GraphException {
        if (!adjacencyList.containsKey(vertex)) {
            throw new GraphException("Вершина не найдена: " + vertex);
        }
        return new ArrayList<>(adjacencyList.get(vertex));
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
                    if (!adjacencyList.containsKey(parts[0])) addVertex(parts[0]);
                    if (!adjacencyList.containsKey(parts[1])) addVertex(parts[1]);
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
        for (String v : adjacencyList.keySet()) indegree.put(v, 0);
        for (Set<String> neighbors : adjacencyList.values())
            for (String n : neighbors) indegree.put(n, indegree.get(n) + 1);

        Queue<String> queue = new LinkedList<>();
        for (Map.Entry<String, Integer> e : indegree.entrySet())
            if (e.getValue() == 0) queue.add(e.getKey());

        List<String> result = new ArrayList<>();
        while (!queue.isEmpty()) {
            String v = queue.poll();
            result.add(v);
            for (String n : adjacencyList.get(v)) {
                indegree.put(n, indegree.get(n) - 1);
                if (indegree.get(n) == 0) queue.add(n);
            }
        }

        if (result.size() != adjacencyList.size()) {
            throw new GraphException("Граф содержит цикл.");
        }
        return result;
    }

    @Override
    public String toString() {
        return adjacencyList.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof AdjacencyListGraph other)) return false;
        return this.adjacencyList.equals(other.adjacencyList);
    }
}
