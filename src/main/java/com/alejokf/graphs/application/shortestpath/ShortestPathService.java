package com.alejokf.graphs.application.shortestpath;

import com.alejokf.graphs.domain.Graph;

public interface ShortestPathService {

    /**
     * Finds the {@link ShortestPaths}, i.e. the shortest paths on the {@code graph} starting on node {@code start}
     *
     * @param graph the graph to perform the shortest paths calculation
     * @param start the starting node
     * @return the shortest paths starting from node {@code start}
     */
    ShortestPaths shortestPath(Graph graph, String start);
}
