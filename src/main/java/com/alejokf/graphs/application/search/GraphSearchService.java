package com.alejokf.graphs.application.search;

import com.alejokf.graphs.domain.Graph;
import com.alejokf.graphs.domain.Node;

import java.util.List;

public interface GraphSearchService {

    /**
     * Returns all the traces in the {@code graph} originating in node {@code start} and ending in node {@code end}
     * with exactly {@code exactHops} hops.
     * <p>
     * Every trace is represented as a list of {@link Node}, i.e. this method returns a List of Lists of {@link Node}
     *
     * @param graph     the graph with the information
     * @param start     the starting node
     * @param end       the ending node
     * @param exactHops the exact number of hops
     * @return the list of traces from {@code start} to {@code end} with exactly {@code exactHops} hops.
     */
    List<List<Node>> tracesByExactHops(final Graph graph, final String start, final String end, final int exactHops);

    /**
     * Returns all the traces in the {@code graph} originating in node {@code start} and ending in node {@code end}
     * with a maximum of {@code maxHops} hops.
     * <p>
     * Every trace is represented as a list of {@link Node}, i.e. this method returns a List of Lists of {@link Node}
     *
     * @param graph   the graph with the information
     * @param start   the starting node
     * @param end     the ending node
     * @param maxHops the exact number of hops
     * @return the list of traces from {@code start} to {@code end} with a maximum of {@code maxHops} hops.
     */
    List<List<Node>> tracesByMaxHops(final Graph graph, final String start, final String end, final int maxHops);

    /**
     * Returns all the traces in the {@code graph} originating in node {@code start} and ending in node {@code end}
     * with a latency of less than {@code maxLatency}.
     * <p>
     * Every trace is represented as a list of {@link Node}, i.e. this method returns a List of Lists of {@link Node}
     *
     * @param graph      the graph with the information
     * @param start      the starting node
     * @param end        the ending node
     * @param maxLatency the exact number of hops
     * @return the list of traces from {@code start} to {@code end} with a latency of less than {@code maxLatency}.
     */
    List<List<Node>> tracesByMaxLatency(final Graph graph, final String start, final String end, final int maxLatency);
}
