package graph.topo;

import graph.common.Graph;
import java.util.*;

/**
 * Kahn's algorithm for topological ordering of a DAG.
 */
public class TopologicalSort {
    public static List<Integer> kahnOrder(Graph g) {
        int n = g.n;
        int[] indeg = new int[n];
        for (Graph.Edge e : g.edges) indeg[e.v]++;
        Deque<Integer> q = new ArrayDeque<>();
        for (int i = 0; i < n; i++) if (indeg[i] == 0) q.add(i);
        List<Integer> order = new ArrayList<>();
        while (!q.isEmpty()) {
            int u = q.removeFirst();
            order.add(u);
            for (Graph.Edge e : g.adj.get(u)) {
                indeg[e.v]--;
                if (indeg[e.v] == 0) q.add(e.v);
            }
        }
        if (order.size() != n) {
            // not a DAG
            return Collections.emptyList();
        }
        return order;
    }
}
