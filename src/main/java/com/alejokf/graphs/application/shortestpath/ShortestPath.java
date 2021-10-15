package com.alejokf.graphs.application.shortestpath;

import com.alejokf.graphs.domain.Node;

import java.util.List;

/**
 * Class representing a shortest distance between a {@code source} node and a {@code target} node
 */
public class ShortestPath {

    // The source node
    private final Node source;
    // The target node
    private final Node target;
    // The distance of the shortest pat
    private final Long distance;
    // All nodes in the shortest path
    private final List<Node> nodes;

    public ShortestPath(Node source, Node target, Long distance, List<Node> nodes) {
        this.source = source;
        this.target = target;
        this.distance = distance;
        this.nodes = nodes;
    }

    public Node getSource() {
        return source;
    }

    public Node getTarget() {
        return target;
    }

    public Long getDistance() {
        return distance;
    }

    public List<Node> getNodes() {
        return nodes;
    }
}
