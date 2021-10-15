package com.alejokf.graphs.application.search.impl;

import com.alejokf.graphs.application.search.GraphTraversalService;
import com.alejokf.graphs.domain.Edge;
import com.alejokf.graphs.domain.Graph;
import com.alejokf.graphs.domain.Node;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Service to get information of known traces from the graph
 */
@Component
public class GraphTraversalServiceImpl implements GraphTraversalService {

    public String traceLatencyOutput(@NotNull Graph graph, @NotNull List<String> trace) {
        return traceLatency(graph, trace).map(Object::toString).orElse(GraphTraversalService.NO_SUCH_TRACE);
    }

    /**
     * This implementation uses a recursive approach to traverse all the nodes in the {@code trace}.
     * <p>
     * It uses a FIFO queue to store the trace, to avoid keeping the index of the current trace node during the
     * recursion.
     *
     * @param graph the graph
     * @param trace the trace within the graph
     * @return the average latency of the trace
     */
    public Optional<Long> traceLatency(@NotNull Graph graph, @NotNull List<String> trace) {
        if (CollectionUtils.isEmpty(trace)) {
            throw new IllegalArgumentException("Trace must be non-empty");
        }
        //FIFO queue with the nodes in the trace
        Queue<String> queue = new ConcurrentLinkedQueue<>(trace);
        Node startNode = graph.getNodes().get(queue.poll());
        return startNode != null ? traceLatency(startNode, queue, 0L) : Optional.empty();

    }

    /**
     * Traverses through the graph recursively, from the {@code currentNode} an through all nodes in the {@code queue},
     * accumulating the latency in the process.
     * <p>
     * The recursion is implemented in a tail-recursive way, to avoid a stack overflow.
     * <p>
     * It returns the accumulated latency after all nodes in the queue have been processed, or Optional in case the
     * trace does not exist.
     *
     * @param currentNode the current node where the traversing is happening
     * @param queue       the queue containing the rest of the elements to traverse
     * @param latency     the accumulated latency
     * @return the accumulated latency or Optional if the trace doesn't exist
     */
    private Optional<Long> traceLatency(@NotNull Node currentNode, @NotNull Queue<String> queue, Long latency) {
        String nextNodeLabel = queue.poll();
        if (nextNodeLabel == null) {
            return Optional.of(latency);
        }

        for (Edge edge : currentNode.getEdgesFrom()) {
            Node nextNode = edge.getOppositeNode(currentNode);
            if (StringUtils.equals(nextNodeLabel, nextNode.getLabel())) {
                return traceLatency(nextNode, queue, latency + edge.getWeight());
            }
        }
        // This means the trace was not found in the graph
        return Optional.empty();
    }
}
