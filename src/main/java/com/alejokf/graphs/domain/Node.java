package com.alejokf.graphs.domain;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Class representing a node in the graph
 */
public class Node {

    // The label of the node, which should be unique in the graph
    private final String label;
    // The edges starting from this node, i.e. whose tail is this node
    private final Set<Edge> edgesFrom = new HashSet<>();
    // The edges pointing to this node, i.e. whose head is this node
    private final Set<Edge> edgesTo = new HashSet<>();

    // This is the key used to seep up the Dijkstra shortest path algorithm
    private Long heapScore = Long.MAX_VALUE;

    public Node(String label) {
        Objects.requireNonNull(label, "label is required");
        this.label = label;
    }

    public void addEdgeTo(Edge edge) {
        edgesTo.add(edge);
    }

    public void addEdgeFrom(Edge edge) {
        edgesFrom.add(edge);
    }

    public void cleanup() {
        heapScore = Long.MAX_VALUE;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Node node = (Node) o;
        return Objects.equals(label, node.label);
    }

    @Override
    public int hashCode() {
        return Objects.hash(label);
    }

    @Override
    public String toString() {
        return label;
    }

    public String getLabel() {
        return label;
    }

    public Set<Edge> getEdgesTo() {
        return edgesTo;
    }

    public Set<Edge> getEdgesFrom() {
        return edgesFrom;
    }

    public Long getHeapScore() {
        return heapScore;
    }

    public void setHeapScore(Long heapScore) {
        this.heapScore = heapScore;
    }
}
