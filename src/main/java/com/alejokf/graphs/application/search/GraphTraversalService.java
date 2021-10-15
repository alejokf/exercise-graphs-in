package com.alejokf.graphs.application.search;

import com.alejokf.graphs.domain.Graph;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

public interface GraphTraversalService {

    String NO_SUCH_TRACE = "NO SUCH TRACE";

    /**
     * Returns the average latency output of a trace within a graph. The output is the average latency as String, or
     * {@value NO_SUCH_TRACE} in case the trace does not exist.
     *
     * @param graph the graph
     * @param trace the trace within the graph
     * @return the average latency output of the trace
     */
    String traceLatencyOutput(@NotNull Graph graph, @NotNull List<String> trace);

    /**
     * Returns the average latency of a trace within a graph. If the trace does not exist in the graph, it returns an
     * Optional.
     *
     * @param graph the graph
     * @param trace the trace within the graph
     * @return the average latency of the trace
     */
    Optional<Long> traceLatency(@NotNull Graph graph, @NotNull List<String> trace);
}
