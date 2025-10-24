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
import java.util.Queue;
import java.util.Set;

/**
 * Реализация графа через список смежности.
 *
 * @param <V> Тип вершины.
 */
public class AdjacencyListGraph<V extends Vertex> implements Graph<V> {
    private final Map<V, Set<V>> adjacencyList = new HashMap<>();

    /**
     * Добавляет вершину в список смежности.
     *
     * @param vertex Вершина для добавления.
     * @throws GraphException Если вершина уже существует или произошла ошибка.
     */
    @Override
    public void addVertex(V vertex) throws GraphException {
        if (adjacencyList.containsKey(vertex)) {
            throw new GraphException("Вершина уже существует: " + vertex);
        }
        adjacencyList.put(vertex, new HashSet<>());
    }

    @Override
    public void removeVertex(V vertex) throws GraphException {
        if (!adjacencyList.containsKey(vertex)) {
            throw new GraphException("Вершина не найдена: " + vertex);
        }
        adjacencyList.remove(vertex);
        for (Set<V> neighbors : adjacencyList.values()) {
            neighbors.remove(vertex);
        }
    }

    @Override
    public void addEdge(V from, V to) throws GraphException {
        if (!adjacencyList.containsKey(from) || !adjacencyList.containsKey(to)) {
            throw new GraphException("Одна или обе вершины не существуют.");
        }
        if (!adjacencyList.get(from).add(to)) {
            throw new GraphException("Ребро уже существует: " + from + " -> " + to);
        }
    }

    @Override
    public void removeEdge(V from, V to) throws GraphException {
        if (!adjacencyList.containsKey(from) || !adjacencyList.get(from).remove(to)) {
            throw new GraphException("Ребро не найдено: " + from + " -> " + to);
        }
    }

    @Override
    public List<V> getNeighbors(V vertex) throws GraphException {
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
                    addVertex((V) new StringVertex(parts[0]));
                } else if (parts.length == 2) {
                    V fromVertex = (V) new StringVertex(parts[0]);
                    V toVertex = (V) new StringVertex(parts[1]);
                    if (!adjacencyList.containsKey(fromVertex)) {
                        addVertex(fromVertex);
                    }
                    if (!adjacencyList.containsKey(toVertex)) {
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
        return new ArrayList<>(adjacencyList.keySet());
    }

    @Override
    public String toString() {
        return adjacencyList.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof AdjacencyListGraph)) {
            return false;
        }
        AdjacencyListGraph<?> other = (AdjacencyListGraph<?>) obj;
        return this.adjacencyList.equals(other.adjacencyList);
    }

    @Override
    public int hashCode() {
        return adjacencyList.hashCode();
    }
}