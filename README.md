Assignment 4 Report — Smart City 
Introduction

For the practical scheduling scenario, standard algorithms for directed graphs were implemented:
Tarjan’s algorithm for detecting strongly connected components (SCC), condensation graph construction, Kahn’s algorithm for topological sorting, and dynamic programming for shortest and longest paths in a DAG.

Weight Model Choice

The edge weight model was selected — weights are assigned to edges.
This naturally represents transition costs or execution times between tasks and fits standard DAG shortest/longest path algorithms.

Implementation Overview

Key points:

TarjanSCC (package graph.scc) returns both the list of components and an array compIndex for vertex-to-component mapping.

Condensation builds the compact graph of components and removes duplicate edges.

TopologicalSort.kahnOrder implements Kahn’s algorithm with in-degree counting.

DAGSP implements both shortestPaths and longestPaths based on the topological order, as well as path reconstruction.

Metrics:

Execution time is measured using System.nanoTime().

The SimpleMetrics utility collects operation counters (e.g., number of DFS calls, relaxations).

Datasets

The /data directory contains 9 datasets:

Category	Example	Description
Small	small-1.json (n=6)	Includes one small cycle
Small	small-2.json (n=7)	Pure DAG
Small	small-3.json (n=8)	1–2 small SCCs
Medium	medium-*	Mixed graphs, several SCCs
Large	large-*	20–40 nodes for performance testing

Each JSON file contains the following fields:
directed, n, edges (u, v, w), source, and weight_model.

Results and Example Analysis

Example: running on data/small-1.json

Step	Output
SCC	4 components (sizes: 3, 1, 1, 1)
Condensation	n = 4, m = 3
Topological order	[0, 2, 3, 1]
Shortest path from 0	computed successfully
Longest (critical) path	path reconstructed, length X

Observations:

Nested SCCs increase condensation graph size; graph density affects the number of DFS calls.

For sparse graphs, Tarjan’s algorithm performs faster than multi-pass approaches.

DAG shortest/longest path algorithms maintain linear time complexity due to the topological order.

Conclusions and Recommendations

For dependency analysis involving cycles, first compute SCCs (Tarjan) and then operate on the condensed DAG.

For time or cost planning, the edge-weight model is the most practical choice and integrates well with topological-order DP.

For large graphs, profiling based on the number of edges (m) is recommended, and sparse representations should be maintained.
