package com.alejokf.graphs.application.shortestpath;

import com.alejokf.graphs.domain.Node;

import java.util.Map;

/**
 * Class representing all the shortest distances on a graph starting from the node {@code source} and to all other
 * reachable nodes in the graph
 */
public class ShortestPaths {

    // The source node
    private final Node source;
    // The shortest distances to all other reachable nodes. A map whose key is the label of the target node
    private final Map<String, ShortestPath> shortestDistances;

    public ShortestPaths(Node source, Map<String, ShortestPath> shortestDistances) {
        this.source = source;
        this.shortestDistances = shortestDistances;
    }

    public Node getSource() {
        return source;
    }

    public ShortestPath getShortestDistances(String node) {
        return shortestDistances.get(node);
    }
}
