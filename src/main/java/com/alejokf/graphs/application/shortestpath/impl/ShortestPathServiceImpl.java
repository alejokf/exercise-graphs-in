package com.alejokf.graphs.application.shortestpath.impl;

import com.alejokf.graphs.application.shortestpath.ShortestPath;
import com.alejokf.graphs.application.shortestpath.ShortestPathService;
import com.alejokf.graphs.application.shortestpath.ShortestPaths;
import com.alejokf.graphs.domain.Edge;
import com.alejokf.graphs.domain.Graph;
import com.alejokf.graphs.domain.Node;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.PriorityQueue;
import java.util.Set;

/**
 * Service to calculate graph shortest paths
 */
@Component
public class ShortestPathServiceImpl implements ShortestPathService {

    public ShortestPaths shortestPath(Graph graph, String start) {
        Node startNode = graph.getNodes().get(start);
        if (startNode == null) {
            throw new IllegalArgumentException("Start node does not exist");
        }
        return calculateShortestPaths(graph, startNode);
    }

    /**
     * Calculates all shortest paths on graph {@code graph} starting from node {@code source}
     * <p>
     * The implementation uses Dijkstra's algorithm, optimized by using a Heap data structure, which maintains the
     * non-processed nodes ordered by the smallest Dijkstra's greedy score of its incoming edges.
     * This optimization changes the algorithm running time from O(e*n) to O(e*lon(n)), with e = the number of edges
     * and n = the number of nodes.
     * <p>
     * There is variation in this case: the distance from and to the same node is not 0. Instead, a non-empty trace
     * starting and ending in the same node needs to exist to calculate the shortest distance.
     *
     * @param graph  the graph to perform the shortest paths calculation
     * @param source the source node
     * @return the shortest paths starting from node {@code source}
     */
    private ShortestPaths calculateShortestPaths(Graph graph, Node source) {
        // Set Greedy Score on edges and Heap Score on nodes for every edge starting from the source node and its head.
        source.getEdgesFrom().forEach(edge -> {
            edge.setDijkstraGreedyScore(edge.getWeight());
            edge.getHead().setHeapScore(edge.getWeight());
        });

        // Processed nodes are those whose shortest path from the source has already been calculated
        Set<Node> processedNodes = new HashSet<>();

        // Map with the calculated shortest distances. The key is the target node.
        Map<String, ShortestPath> shortestDistances = new HashMap<>();

        // Heap to maintain the non processed nodes ordered by the minimum Dijkstra greedy score of its edges coming
        // from the set of non-processed nodes
        PriorityQueue<Node> minNodesHeap = new PriorityQueue<>(Comparator.comparing(Node::getHeapScore));
        minNodesHeap.addAll(graph.getNodes().values());

        // Initially, source node is not in the set of non-processed nodes
        boolean isSourceProcessed = false;

        while (shortestDistances.size() < graph.getNodes().size()) {
            Node minNode = minNodesHeap.poll();

            // If there are no more edges whose tail is the set of non-processed nodes and the head is in the set of
            // processed nodes, we set null as the distance to the remaining non-processed nodes, meaning there is no
            // path between the source node and that node
            if (minNode == null) {
                for (Node node : graph.getNodes().values()) {
                    if (!processedNodes.contains(node)) {
                        processedNodes.add(node);
                        shortestDistances.put(node.getLabel(), null);
                    }
                }
                continue;
            }

            Optional<Edge> minEdgeOpt = findMinEdge(minNode, processedNodes, isSourceProcessed, source);
            if (minEdgeOpt.isPresent()) {
                Edge minEdge = minEdgeOpt.get();
                Node nodeFrom = minEdge.getOppositeNode(minNode);

                if (minNode.equals(source)) {
                    isSourceProcessed = true;
                }

                processedNodes.add(minNode);

                ShortestPath nodeFromShortestPath = shortestDistances.get(nodeFrom.getLabel());
                Long previousDistance = nodeFromShortestPath != null ? nodeFromShortestPath.getDistance() : 0L;
                List<Node> actualPath = nodeFromShortestPath != null ? nodeFromShortestPath.getNodes() :
                        new ArrayList<>(List.of(source));
                actualPath.add(minNode);

                shortestDistances.put(minNode.getLabel(), new ShortestPath(source, minNode,
                        previousDistance + minEdge.getWeight(), actualPath));

                // minNode has been added to the set of non-processed nodes. There are now new edges whose tail is
                // in the set of non-processed nodes and the head is in the set of processed nodes.
                // This can change the heap score of the node, which is the heap key. Therefore, we need to remove
                // those edges to the heap, recalculate Dijkstra Greedy Score for those edges, and add them to he
                // heap again.
                updateHeap(minNodesHeap, minNode, graph.getNodes(), processedNodes, shortestDistances);
            }
        }

        return new ShortestPaths(source, shortestDistances);
    }

    /**
     * Finds the edge with the minimum Dijkstra Greedy Score of the provided {@code head} node, whose tail is in the
     * set of non-processed nodes.
     * <p>
     * There is a special case to consider: The source node is not considered to be processed initially, but its
     * outgoing edges are considered when calculating the scores.
     *
     * @param head              the provided head node
     * @param processedNodes    the set of processed nodes
     * @param isSourceProcessed flag indicating if the source node has already been processed
     * @param source            the source node
     * @return the min edge with the minimum Dijkstra Greedy Score of the provided {@code head} node
     */
    private Optional<Edge> findMinEdge(final Node head, final Set<Node> processedNodes, boolean isSourceProcessed,
            Node source) {
        return head.getEdgesTo().stream()
                .filter(edge -> (!isSourceProcessed && source.equals(edge.getTail())) || processedNodes.contains(edge.getTail()))
                .min(Comparator.comparing(Edge::getDijkstraGreedyScore));
    }

    /**
     * Updates the heap score of the nodes in the Heap, having edges whose tail is in the set of non-processed nodes
     * and the head is in the set of processed nodes.
     *
     * @param minNodesHeap      the heap having the nodes ordered by minimum Dijkstra Greedy Score
     * @param movedNode         the moved node
     * @param allNodes          Map with all nodes of the graph, whose key is the node label
     * @param processedNodes    Map with the processed nodes, whose key is the node label
     * @param shortestDistances Map with the currently calculated shortest distances, whose key is the target node label
     */
    private void updateHeap(PriorityQueue<Node> minNodesHeap, Node movedNode, Map<String, Node> allNodes,
            Set<Node> processedNodes, Map<String, ShortestPath> shortestDistances) {
        movedNode.getEdgesFrom().forEach(edge -> {
            Node head = edge.getHead();

            if (allNodes.containsKey(head.getLabel()) && !processedNodes.contains(head)) {
                ShortestPath movedShortestPath = shortestDistances.get(movedNode.getLabel());
                if (movedShortestPath != null) {
                    minNodesHeap.remove(head);

                    edge.setDijkstraGreedyScore(movedShortestPath.getDistance() + edge.getWeight());
                    long newScore = Math.min(head.getHeapScore(), edge.getDijkstraGreedyScore());
                    head.setHeapScore(newScore);

                    minNodesHeap.add(head);
                }
            }
        });
    }
}
