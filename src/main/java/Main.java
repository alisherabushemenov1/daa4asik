import graph.common.Graph;
import graph.scc.TarjanSCC;
import graph.scc.Condensation;
import graph.topo.TopologicalSort;
import graph.dagsp.DAGSP;
import utils.SimpleMetrics;
import utils.DatasetGenerator;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.*;

/**
 * Fixed and more robust version of Main.
 * Place this file in src/main/java/app/Main.java
 */
public class Main {

    /**
     * Reads a JSON description of a graph in a simple known format:
     * {
     *   "n": 5,
     *   "edges": [ { "u": 0, "v": 1, "w": 2 }, { "u": 1, "v": 2 } ]
     * }
     *
     * The optional field "w" is supported (if missing -> default weight = 1).
     */
    public static Graph readJson(String filename) throws IOException {
        String s = new String(Files.readAllBytes(Paths.get(filename)));
        // read n
        int n = 0;
        Matcher mn = Pattern.compile("\"n\"\\s*:\\s*(\\d+)").matcher(s);
        if (mn.find()) {
            n = Integer.parseInt(mn.group(1));
        } else {
            throw new IOException("Cannot find field \"n\" in " + filename);
        }
        Graph g = new Graph(n);

        // parse edges elements — allow optional "w"
        // example: { "u": 0, "v": 1, "w": 2 } or { "u": 1, "v": 2 }
        Matcher me = Pattern.compile("\\{[^}]*\"u\"\\s*:\\s*(\\d+)[^}]*\"v\"\\s*:\\s*(\\d+)(?:[^}]*\"w\"\\s*:\\s*(\\d+))?[^}]*\\}")
                .matcher(s);
        while (me.find()) {
            int u = Integer.parseInt(me.group(1));
            int v = Integer.parseInt(me.group(2));
            int w = 1;
            if (me.group(3) != null) {
                w = Integer.parseInt(me.group(3));
            }
            if (u < 0 || u >= n || v < 0 || v >= n) {
                System.err.printf("Warning: edge (%d->%d) ignored — индекс вне диапазона (n=%d)%n", u, v, n);
                continue;
            }
            g.addEdge(u, v, w);
        }

        return g;
    }

    private static String distToString(long d) {
        if (d == DAGSP.INF) return "INF";
        return String.valueOf(d);
    }

    private static void printLongArray(long[] arr) {
        if (arr == null) return;
        System.out.print("[");
        for (int i = 0; i < arr.length; i++) {
            System.out.print(distToString(arr[i]));
            if (i + 1 < arr.length) System.out.print(", ");
        }
        System.out.println("]");
    }

    public static void main(String[] args) {
        String file = (args.length > 0) ? args[0] : "data/tasks.json";
        try {
            // Attempt to generate example datasets (if not present)
            try {
                DatasetGenerator.generateAll("data");
            } catch (Exception ignored) { /* generation is non-critical */ }

            Graph g = readJson(file);

            SimpleMetrics metrics = new SimpleMetrics();
            metrics.start();

            TarjanSCC tarjan = new TarjanSCC(g);
            List<List<Integer>> comps = tarjan.run();
            metrics.increment("scc_runs");
            metrics.stop();

            System.out.println("==== SCC ====");
            System.out.println("Found components: " + comps.size());
            for (int i = 0; i < comps.size(); i++) {
                System.out.printf("C%d: %s%n", i, comps.get(i).toString());
            }

            // Build the condensation graph
            Condensation cond = new Condensation(g, tarjan.getComponentIndex());
            Graph cg = cond.getCondensation();
            System.out.println();
            System.out.println("==== Condensation (component DAG) ====");
            System.out.println("Component count: " + cond.getComponentCount());
            System.out.println("Condensation graph nodes: " + cg.n + ", edges: " + cg.edges.size());

            // Topological order of the component graph
            List<Integer> order = TopologicalSort.kahnOrder(cg);
            if (order.isEmpty()) {
                System.out.println("Warning: condensation graph is not a DAG (topo-order empty).");
            } else {
                System.out.println("Topological order of components: " + order);
            }

            // Run DAG-SP if there is at least one vertex in the condensation graph
            if (cg.n > 0 && !order.isEmpty()) {
                int src = 0;
                System.out.println();
                System.out.println("==== DAG Shortest / Longest from " + src + " ====");
                try {
                    long[] sp = DAGSP.shortestPaths(cg, src);
                    System.out.print("Shortest distances: ");
                    printLongArray(sp);

                    long[] lp = DAGSP.longestPaths(cg, src);
                    System.out.print("Longest distances:  ");
                    printLongArray(lp);

                    // example of reconstructing paths for all vertices
                    for (int v = 0; v < cg.n; v++) {
                        List<Integer> path = DAGSP.reconstructPath(src, v, cg, false);
                        if (path.isEmpty()) {
                            // try the longest variant
                            path = DAGSP.reconstructPath(src, v, cg, true);
                        }
                        System.out.printf("Path %d <- %d : %s%n", src, v, path);
                    }
                } catch (IllegalArgumentException ex) {
                    System.out.println("DAG-SP error: " + ex.getMessage());
                }
            }

            System.out.println();
            System.out.println("==== Metrics ====");
            System.out.println("Elapsed (ns): " + metrics.elapsedNano());
            System.out.println("scc_runs: " + metrics.getCount("scc_runs"));

        } catch (IOException e) {
            System.err.println("I/O error: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Unhandled error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
