package com.alejokf.graphs.domain;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * Class representing an edge in the graph
 */
public class Edge {

    private final Node tail;
    private final Node head;
    private final Long weight;
    private Long dijkstraGreedyScore = Long.MAX_VALUE;

    public Edge(Node tail, Node head, Long weight) {
        Objects.requireNonNull(tail, "tail is required");
        Objects.requireNonNull(head, "head is required");
        Objects.requireNonNull(weight, "weight is required");
        this.tail = tail;
        this.head = head;
        this.weight = weight;
    }

    @NotNull
    public Node getOppositeNode(Node node) {
        if (tail.equals(node)) {
            return head;
        } else if (head.equals(node)) {
            return tail;
        } else {
            throw new IllegalArgumentException("Node " + node.getLabel());
        }
    }

    public void cleanup() {
        dijkstraGreedyScore = Long.MAX_VALUE;
    }

    @Override
    public String toString() {
        return tail + " --(" + weight + ")--> " + head;
    }

    public Node getTail() {
        return tail;
    }

    public Node getHead() {
        return head;
    }

    public Long getWeight() {
        return weight;
    }

    public Long getDijkstraGreedyScore() {
        return dijkstraGreedyScore;
    }

    public void setDijkstraGreedyScore(Long dijkstraGreedyScore) {
        this.dijkstraGreedyScore = dijkstraGreedyScore;
    }
}
