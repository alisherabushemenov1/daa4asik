package graph.common;

import java.util.*;

/**
 * Simple directed graph representation with optional edge weights.
 */
public class Graph {
    public final int n;
    public final List<Edge> edges = new ArrayList<>();
    public final List<List<Edge>> adj;

    public Graph(int n) {
        this.n = n;
        adj = new ArrayList<>(n);
        for (int i = 0; i < n; i++) adj.add(new ArrayList<>());
    }

    public void addEdge(int u, int v) {
        addEdge(u, v, 1);
    }

    public void addEdge(int u, int v, int w) {
        Edge e = new Edge(u, v, w);
        edges.add(e);
        adj.get(u).add(e);
    }

    public static class Edge {
        public final int u, v;
        public final int w;
        public Edge(int u, int v, int w) { this.u = u; this.v = v; this.w = w; }
        public String toString() { return String.format("(%d->%d:%d)", u, v, w); }
    }
}
