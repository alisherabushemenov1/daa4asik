package utils;

import graph.common.Graph;
import java.io.*;
import java.util.*;

/**
 * Small helper to generate dataset JSON files in project /data folder.
 * Produces 9 datasets: 3 small, 3 medium, 3 large.
 */
public class DatasetGenerator {
    public static void generateAll(String path) throws IOException {
        // small
        generate(path + "/small-1.json", 6, new int[][]{{0,1,1},{1,2,1},{2,0,1},{3,4,1}});
        generate(path + "/small-2.json", 7, new int[][]{{0,1,1},{1,2,1},{2,3,1}});
        generate(path + "/small-3.json", 8, new int[][]{{0,1,1},{1,0,1},{2,3,1},{3,4,1}});
        // medium
        generate(path + "/medium-1.json", 12, new int[][]{{0,1,2},{1,2,2},{2,3,2},{3,1,2},{4,5,1},{5,6,1},{6,4,1}});
        generate(path + "/medium-2.json", 15, new int[][]{{0,1,1},{1,2,1},{2,3,1},{3,4,1},{4,5,1}});
        generate(path + "/medium-3.json", 18, new int[][]{{0,1,1},{1,2,1},{2,0,1},{3,4,1},{4,5,1}});
        // large
        generate(path + "/large-1.json", 25, randomEdges(25, 40));
        generate(path + "/large-2.json", 30, randomEdges(30, 60));
        generate(path + "/large-3.json", 40, randomEdges(40, 80));
    }

    private static void generate(String filename, int n, int[][] edges) throws IOException {
        Map<String,Object> obj = new LinkedHashMap<>();
        obj.put("directed", true);
        obj.put("n", n);
        List<Map<String,Integer>> arr = new ArrayList<>();
        for (int[] e : edges) {
            Map<String,Integer> m = new LinkedHashMap<>();
            m.put("u", e[0]);
            m.put("v", e[1]);
            m.put("w", e.length>2?e[2]:1);
            arr.add(m);
        }
        obj.put("edges", arr);
        obj.put("source", 0);
        obj.put("weight_model", "edge");
        try (Writer w = new FileWriter(filename)) {
            w.write(toJson(obj));
        }
    }

    private static int[][] randomEdges(int n, int m) {
        Random rnd = new Random(123);
        int[][] edges = new int[m][3];
        for (int i = 0; i < m; i++) {
            edges[i][0] = rnd.nextInt(n);
            edges[i][1] = rnd.nextInt(n);
            edges[i][2] = 1 + rnd.nextInt(10);
        }
        return edges;
    }

    private static String toJson(Map<String,Object> obj) {
        StringBuilder sb = new StringBuilder();
        sb.append("{\n");
        sb.append("  \"directed\": ").append(obj.get("directed")).append(",\n");
        sb.append("  \"n\": ").append(obj.get("n")).append(",\n");
        sb.append("  \"edges\": [\n");
        List<?> arr = (List<?>)obj.get("edges");
        for (int i = 0; i < arr.size(); i++) {
            Map<?,?> m = (Map<?,?>)arr.get(i);
            sb.append("    { \"u\": ").append(m.get("u")).append(", \"v\": ").append(m.get("v")).append(", \"w\": ").append(m.get("w")).append(" }");
            if (i+1 < arr.size()) sb.append(",");
            sb.append("\n");
        }
        sb.append("  ],\n");
        sb.append("  \"source\": ").append(obj.get("source")).append(",\n");
        sb.append("  \"weight_model\": \"").append(obj.get("weight_model")).append("\"\n");
        sb.append("}\n");
        return sb.toString();
    }
}
