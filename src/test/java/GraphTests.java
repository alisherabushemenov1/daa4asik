import static org.junit.Assert.*;
import org.junit.Test;
import graph.common.Graph;
import graph.scc.TarjanSCC;
import graph.scc.Condensation;
import graph.topo.TopologicalSort;
import graph.dagsp.DAGSP;

import java.util.*;

public class GraphTests {
    @Test
    public void testSCCSimple() {
        Graph g = new Graph(4);
        g.addEdge(0,1); g.addEdge(1,2); g.addEdge(2,0);
        g.addEdge(2,3);
        TarjanSCC t = new TarjanSCC(g);
        java.util.List<java.util.List<Integer>> comps = t.run();
        assertTrue(comps.size() >= 2);
    }

    @Test
    public void testTopoDAG() {
        Graph g = new Graph(3);
        g.addEdge(0,1); g.addEdge(1,2);
        java.util.List<Integer> ord = TopologicalSort.kahnOrder(g);
        assertEquals(3, ord.size());
        assertTrue(ord.indexOf(0) < ord.indexOf(1));
    }

    @Test
    public void testDAGSP() {
        Graph g = new Graph(4);
        g.addEdge(0,1,3); g.addEdge(1,2,4); g.addEdge(0,2,10); g.addEdge(2,3,1);
        long[] sp = DAGSP.shortestPaths(g,0);
        assertEquals(7, sp[2]);
        long[] lp = DAGSP.longestPaths(g,0);
        assertEquals(11, lp[3]);
    }
}
