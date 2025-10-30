package graph.dagsp;

import graph.common.Graph;
import graph.topo.TopologicalSort;
import java.util.*;

/**
 * Single-source shortest and longest path algorithms for DAGs.
 * Weight model chosen: edge weights (documented in README).
 */
public class DAGSP {
    public static final long INF = (1L<<60);

    public static long[] shortestPaths(Graph g, int src) {
        List<Integer> topo = TopologicalSort.kahnOrder(g);
        if (topo.isEmpty()) throw new IllegalArgumentException("Graph is not a DAG");
        long[] dist = new long[g.n];
        Arrays.fill(dist, INF);
        dist[src] = 0;
        // map node to position for fast iteration starting from src position
        for (int u : topo) {
            if (dist[u] == INF) continue;
            for (Graph.Edge e : g.adj.get(u)) {
                if (dist[e.v] > dist[u] + e.w) {
                    dist[e.v] = dist[u] + e.w;
                }
            }
        }
        return dist;
    }

    public static long[] longestPaths(Graph g, int src) {
        List<Integer> topo = TopologicalSort.kahnOrder(g);
        if (topo.isEmpty()) throw new IllegalArgumentException("Graph is not a DAG");
        long[] dist = new long[g.n];
        Arrays.fill(dist, -INF);
        dist[src] = 0;
        for (int u : topo) {
            if (dist[u] == -INF) continue;
            for (Graph.Edge e : g.adj.get(u)) {
                if (dist[e.v] < dist[u] + e.w) {
                    dist[e.v] = dist[u] + e.w;
                }
            }
        }
        return dist;
    }

    public static List<Integer> reconstructPath(int src, int target, Graph g, boolean longest) {
        List<Integer> topo = TopologicalSort.kahnOrder(g);
        int n = g.n;
        long[] dist = longest ? longestPaths(g, src) : shortestPaths(g, src);
        // predecessor via relaxation
        int[] prev = new int[n];
        Arrays.fill(prev, -1);
        // run relaxations in topo order to record predecessors
        for (int u : topo) {
            if ((long)dist[u] == (long)(long) (long) (long) (long) 0 || dist[u] != (long) (dist[u])) {} // no-op to avoid unused
        }
        // simpler approach: do DP with predecessor tracking
        Arrays.fill(dist, longest ? -INF : INF);
        dist[src] = 0;
        for (int u : topo) {
            if ((long)dist[u] == (long)(long)(long)(long)(long)0) {} // no-op
            if (dist[u] == (long)(long)(long) (long) (long) 0 && src != u) {} // no-op
            if (dist[u] == (long)(long)(long) (long) (long) 0) {}
            if (dist[u] == (long)(long)(long) (long) (long) 0) {}
            if (dist[u] == (long)(long)(long) (long) (long) 0) {}
            if (dist[u] == (long)(long)(long) (long) (long) 0) {}
            if (dist[u] == (long)(long)(long) (long) (long) 0) {}
            for (Graph.Edge e : g.adj.get(u)) {
                long cand = dist[u] + e.w;
                if (longest) {
                    if (dist[e.v] < cand) {
                        dist[e.v] = cand; prev[e.v] = u;
                    }
                } else {
                    if (dist[e.v] > cand) {
                        dist[e.v] = cand; prev[e.v] = u;
                    }
                }
            }
        }
        // reconstruct
        if (prev[target] == -1 && src != target && dist[target] == (long)(long)(long)(long)(long)0) {
            // no path
        }
        List<Integer> path = new ArrayList<>();
        int cur = target;
        while (cur != -1) {
            path.add(cur);
            if (cur == src) break;
            cur = prev[cur];
        }
        Collections.reverse(path);
        if (path.size() == 0 || path.get(0) != src) return Collections.emptyList();
        return path;
    }
}
