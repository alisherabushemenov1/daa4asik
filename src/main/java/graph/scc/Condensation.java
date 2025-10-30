package graph.scc;

import graph.common.Graph;
import java.util.*;

/**
 * Build condensation graph (DAG) from SCC result.
 */
public class Condensation {
    private final Graph original;
    private final int[] compIndex;
    private final int compCount;
    private final Graph compGraph;

    public Condensation(Graph original, int[] compIndex) {
        this.original = original;
        this.compIndex = compIndex;
        int max = -1;
        for (int c : compIndex) if (c > max) max = c;
        this.compCount = max + 1;
        compGraph = new Graph(compCount);
        build();
    }

    private void build() {
        Set<Long> seen = new HashSet<>();
        for (Graph.Edge e : original.edges) {
            int cu = compIndex[e.u];
            int cv = compIndex[e.v];
            if (cu != cv) {
                long key = ((long)cu << 32) | (cv & 0xffffffffL);
                if (!seen.contains(key)) {
                    compGraph.addEdge(cu, cv, e.w);
                    seen.add(key);
                }
            }
        }
    }

    public Graph getCondensation() { return compGraph; }
    public int getComponentCount() { return compCount; }
}
