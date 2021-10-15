package com.alejokf.graphs.domain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Class representing a Graph, implemented with the "Adjacency List" approach, i.e.
 * - A list of nodes
 * - A list of edges
 * - edge node points to its two nodes
 * - each node points to edges incident on it
 *
 * In this case, the list of nodes is actually implemented by a Map, to facilitate node retrieval by label
 */
public class Graph {

    private static final Logger logger = LoggerFactory.getLogger(Graph.class);

    private final Map<String, Node> nodes;
    private final List<Edge> edges;

    public Graph(Map<String, Node> nodes, List<Edge> edges) {
        Objects.requireNonNull(nodes, "nodes are required");
        Objects.requireNonNull(edges, "edges are required");
        this.nodes = nodes;
        this.edges = edges;
    }

    public Map<String, Node> getNodes() {
        return nodes;
    }

    public void cleanup() {
        nodes.values().forEach(Node::cleanup);
        edges.forEach(Edge::cleanup);
    }

    public List<Edge> getEdges() {
        return edges;
    }

    public void printGraph() {
        logger.info("Printing graph");
        for (Node node : nodes.values()) {
            StringBuilder nodeMessage = new StringBuilder();
            for (Edge edge : node.getEdgesFrom()) {
                nodeMessage.append(" ");
                nodeMessage.append(edge.getOppositeNode(node).getLabel());
                nodeMessage.append("(");
                nodeMessage.append(edge.getWeight());
                nodeMessage.append(")");
            }
            logger.info(node.getLabel() + ":" + nodeMessage.toString());
        }
    }
}
