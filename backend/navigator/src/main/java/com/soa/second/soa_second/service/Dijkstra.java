package com.soa.second.soa_second.service;
import java.util.*;

public class Dijkstra {
    public static class Edge {
        Long destination;
        Float weight;

        Edge(Long destination, Float weight) {
            this.destination = destination;
            this.weight = weight;
        }
    }

    public static float[] dijkstra(List<List<Edge>> graph, int start) {
        float[] distance = new float[graph.size()];
        Arrays.fill(distance, Integer.MAX_VALUE);
        distance[start] = 0;

        PriorityQueue<Integer> pq = new PriorityQueue<>(Comparator.comparingDouble(o -> distance[o]));
        pq.add(start);

        while (!pq.isEmpty()) {
            int node = pq.poll();

            for (Edge neighbor : graph.get(node)) {
                float newDist = distance[node] + neighbor.weight;
                if (newDist < distance[Math.toIntExact(neighbor.destination)]) {
                    distance[Math.toIntExact(neighbor.destination)] = newDist;
                    pq.add(Math.toIntExact(neighbor.destination));
                }
            }
        }

        return distance;
    }

    public static float shortestPath(List<List<Edge>> graph, int start, int end) {
        float[] distances = dijkstra(graph, start);
        if (distances[end] == Integer.MAX_VALUE) {
            return -1; // No path found
        } else {
            return distances[end];
        }
    }
}