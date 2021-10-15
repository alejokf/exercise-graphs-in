package com.alejokf.graphs.application.search.impl;

import com.alejokf.graphs.domain.Graph;
import com.alejokf.graphs.infrastructure.impl.CSVGraphProcessor;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class GraphTraversalServiceImplTest {

    private final GraphTraversalServiceImpl graphTraversalServiceImpl = new GraphTraversalServiceImpl();
    private Graph graph;

    @BeforeAll
    void loadGraph() throws IOException {
        CSVGraphProcessor reader = new CSVGraphProcessor();
        graph = reader.readFromFile("com/alejokf/graphs/TestInput.csv");
    }

    @Test
    void traceLatency_TestInput_ABC() {
        Optional<Long> traceLatency = graphTraversalServiceImpl.traceLatency(graph, List.of("A", "B", "C"));
        assertTrue(traceLatency.isPresent());
        assertEquals(9, traceLatency.get());
    }

    @Test
    void traceLatency_TestInput_AD() {
        Optional<Long> traceLatency = graphTraversalServiceImpl.traceLatency(graph, List.of("A", "D"));
        assertTrue(traceLatency.isPresent());
        assertEquals(5, traceLatency.get());
    }

    @Test
    void traceLatency_TestInput_ADC() {
        Optional<Long> traceLatency = graphTraversalServiceImpl.traceLatency(graph, List.of("A", "D", "C"));
        assertTrue(traceLatency.isPresent());
        assertEquals(13, traceLatency.get());
    }

    @Test
    void traceLatency_TestInput_AEBCD() {
        Optional<Long> traceLatency = graphTraversalServiceImpl.traceLatency(graph, List.of("A", "E", "B", "C", "D"));
        assertTrue(traceLatency.isPresent());
        assertEquals(22, traceLatency.get());
    }

    @Test
    void traceLatency_TestInput_AED() {
        Optional<Long> traceLatency = graphTraversalServiceImpl.traceLatency(graph, List.of("A", "E", "D"));
        assertFalse(traceLatency.isPresent());
    }

    @Test
    void traceLatencyOutput_ExistingTrace() {
        String traceLatencyOutput = graphTraversalServiceImpl.traceLatencyOutput(graph, List.of("A", "B", "C"));
        assertEquals("9", traceLatencyOutput);
    }

    @Test
    void traceLatencyOutput_NonExistingTrace() {
        String traceLatencyOutput = graphTraversalServiceImpl.traceLatencyOutput(graph, List.of("A", "E", "D"));
        assertEquals("NO SUCH TRACE", traceLatencyOutput);
    }
}