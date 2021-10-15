package com.alejokf.graphs.application.search.impl;

import com.alejokf.graphs.domain.Edge;
import com.alejokf.graphs.domain.Node;

import java.util.ArrayList;
import java.util.List;

/**
 * Class representing the trace status when navigating the graph
 */
public class TraceStatus {

    // Current node of the trace
    private final Node currentNode;
    // The nodes added to the trace so far
    private final List<Node> nodes;
    // The edges added to the trace so far
    private final List<Edge> edges;

    public static TraceStatus of(Node currentNode, List<Node> trace) {
        return new TraceStatus(currentNode, trace);
    }

    public static TraceStatus of(Node currentNode, List<Node> trace, List<Edge> edges) {
        return new TraceStatus(currentNode, trace, edges);
    }

    public TraceStatus(Node currentNode, List<Node> nodes) {
        this(currentNode, nodes, new ArrayList<>());
    }

    public TraceStatus(Node currentNode, List<Node> nodes, List<Edge> edges) {
        this.currentNode = currentNode;
        this.nodes = nodes;
        this.edges = edges;
    }

    public Node getCurrentNode() {
        return currentNode;
    }

    public int getHops() {
        return nodes.size() - 1;
    }

    public long getLatency() {
        return edges.stream().map(Edge::getWeight).reduce(0L, Long::sum);
    }

    public List<Node> getNodes() {
        return nodes;
    }

    public List<Edge> getEdges() {
        return edges;
    }
}
