package graph.scc;

import graph.common.Graph;
import java.util.*;

/**
 * Tarjan's algorithm for finding Strongly Connected Components.
 */
public class TarjanSCC {
    private final Graph g;
    private int time = 0;
    private final int[] disc, low, compIndex;
    private final boolean[] inStack;
    private final Deque<Integer> stack = new ArrayDeque<>();
    private final List<List<Integer>> components = new ArrayList<>();

    public TarjanSCC(Graph g) {
        this.g = g;
        int n = g.n;
        disc = new int[n];
        low = new int[n];
        compIndex = new int[n];
        Arrays.fill(disc, -1);
        Arrays.fill(low, -1);
        Arrays.fill(compIndex, -1);
        inStack = new boolean[n];
    }

    public List<List<Integer>> run() {
        for (int i = 0; i < g.n; i++) {
            if (disc[i] == -1) dfs(i);
        }
        return components;
    }

    private void dfs(int u) {
        disc[u] = low[u] = time++;
        stack.push(u);
        inStack[u] = true;
        for (Graph.Edge e : g.adj.get(u)) {
            int v = e.v;
            if (disc[v] == -1) {
                dfs(v);
                low[u] = Math.min(low[u], low[v]);
            } else if (inStack[v]) {
                low[u] = Math.min(low[u], disc[v]);
            }
        }
        if (low[u] == disc[u]) {
            List<Integer> comp = new ArrayList<>();
            while (true) {
                int v = stack.pop();
                inStack[v] = false;
                compIndex[v] = components.size();
                comp.add(v);
                if (v == u) break;
            }
            components.add(comp);
        }
    }

    public int[] getComponentIndex() { return compIndex; }
}
