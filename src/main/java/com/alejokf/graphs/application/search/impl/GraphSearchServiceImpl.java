package com.alejokf.graphs.application.search.impl;

import com.alejokf.graphs.application.search.GraphSearchService;
import com.alejokf.graphs.domain.Edge;
import com.alejokf.graphs.domain.Graph;
import com.alejokf.graphs.domain.Node;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.function.Predicate;

/**
 * Service with graph-search related operations, i.e. operations in which search through the graph nodes and edges
 * needs to be carried out to get information from it
 */
@Component
public class GraphSearchServiceImpl implements GraphSearchService {

    public List<List<Node>> tracesByExactHops(final Graph graph, final String start, final String end,
            final int exactHops) {
        Predicate<TraceStatus> keepTraversing = status -> status.getHops() <= exactHops;
        Predicate<TraceStatus> addTrace = status -> status.getHops() == exactHops;
        return tracesByConditionHops(graph, start, end, keepTraversing, addTrace);
    }

    public List<List<Node>> tracesByMaxHops(final Graph graph, final String start, final String end,
            final int maxHops) {
        Predicate<TraceStatus> keepTraversing = status -> status.getHops() <= maxHops;
        Predicate<TraceStatus> addTrace = status -> status.getHops() > 0 && status.getHops() <= maxHops;
        return tracesByConditionHops(graph, start, end, keepTraversing, addTrace);
    }

    public List<List<Node>> tracesByMaxLatency(final Graph graph, final String start, final String end,
            final int maxLatency) {
        Predicate<TraceStatus> keepTraversing = status -> status.getLatency() < maxLatency;
        Predicate<TraceStatus> addTrace = status -> status.getLatency() > 0 && status.getLatency() <= maxLatency;
        return tracesByConditionHops(graph, start, end, keepTraversing, addTrace);
    }

    /**
     * Returns all the traces in the {@code graph} originating in node {@code start} and ending in node {@code end}
     * that comply with the condition defined in {@code addTrace}
     * <p>
     * Every trace is represented as a list of {@link Node}, i.e. this method returns a List of Lists of {@link Node}
     * <p>
     * This a generalisation which uses two predicates: {@code keepTraversing} to check if the graph traverse should
     * continue in the current node and {@code addTrace} to check if the trace should be added to the results.
     * <p>
     * The implementation uses a DSF (Depth-Search-First) approach when traversing the graph, which navigates through
     * all nodes of the graph going through a single path first until a condition is met and back tracing in case it
     * is necessary.
     *
     * @param graph          the graph with the information
     * @param start          the starting node
     * @param end            the ending node
     * @param keepTraversing the predicate to check if the graph traverse should continue
     * @param addTrace       the predicate to check if the trace should be added to the results
     * @return the list of traces from {@code start} to {@code end} complying the condition defined in {@code addTrace}
     */
    private List<List<Node>> tracesByConditionHops(final Graph graph, final String start, final String end,
            final Predicate<TraceStatus> keepTraversing, final Predicate<TraceStatus> addTrace) {
        Node startNode = graph.getNodes().get(start);
        if (startNode == null) {
            throw new IllegalArgumentException("Start node does not exist");
        }

        List<List<Node>> traces = new ArrayList<>();

        //Stack that have the node (when navigating the Graph) and the number of hops to reach that node
        Deque<TraceStatus> deque = new ConcurrentLinkedDeque<>();
        deque.push(TraceStatus.of(startNode, List.of(startNode)));

        while (!deque.isEmpty()) {
            TraceStatus traceStatus = deque.pop();
            Node node = traceStatus.getCurrentNode();

            if (keepTraversing.test(traceStatus)) {
                if (addTrace.test(traceStatus) && node.getLabel().equals(end)) {
                    traces.add(traceStatus.getNodes());
                }
                for (Edge edge : node.getEdgesFrom()) {
                    List<Node> nodes = new ArrayList<>(traceStatus.getNodes());
                    nodes.add(edge.getHead());
                    List<Edge> edges = new ArrayList<>(traceStatus.getEdges());
                    edges.add(edge);
                    deque.push(TraceStatus.of(edge.getHead(), nodes, edges));
                }
            }
        }

        return traces;
    }

}
