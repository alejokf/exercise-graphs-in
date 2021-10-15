package com.alejokf.graphs.application.search.impl;

import com.alejokf.graphs.domain.Graph;
import com.alejokf.graphs.domain.Node;
import com.alejokf.graphs.infrastructure.impl.CSVGraphProcessor;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class GraphSearchServiceTest {

    private final GraphSearchServiceImpl graphSearchServiceImpl = new GraphSearchServiceImpl();
    private Graph graph;

    @BeforeAll
    void loadGraph() throws IOException {
        CSVGraphProcessor reader = new CSVGraphProcessor();
        graph = reader.readFromFile("com/alejokf/graphs/TestInput.csv");
    }

    @Test
    void tracesByMaxHops_CC3() {
        List<List<Node>> traces = graphSearchServiceImpl.tracesByMaxHops(graph, "C", "C", 3);
        assertEquals(2, traces.size());
    }

    @Test
    void tracesByMaxHops_CC6() {
        List<List<Node>> traces = graphSearchServiceImpl.tracesByMaxHops(graph, "C", "C", 6);
        assertEquals(10, traces.size());
        // some cases
        List<List<String>> tracesLabels = traces.stream().
                map(nodes -> nodes.stream().map(Node::getLabel).collect(Collectors.toList()))
                .collect(Collectors.toList());
        assertTrue(tracesLabels.contains(List.of("C", "D", "C", "D", "E", "B", "C")));
        assertTrue(tracesLabels.contains(List.of("C", "D", "C", "D", "C", "D", "C")));
        assertTrue(tracesLabels.contains(List.of("C", "E", "B", "C", "E", "B", "C")));
    }

    @Test
    void tracesByExactHops_AC4() {
        List<List<Node>> traces = graphSearchServiceImpl.tracesByExactHops(graph, "A", "C", 4);
        assertEquals(3, traces.size());
    }

    @Test
    void tracesByExactHops_AC6() {
        List<List<Node>> traces = graphSearchServiceImpl.tracesByExactHops(graph, "A", "C", 6);
        assertEquals(6, traces.size());
        // some cases
        List<List<String>> tracesLabels = traces.stream().
                map(nodes -> nodes.stream().map(Node::getLabel).collect(Collectors.toList()))
                .collect(Collectors.toList());
        assertTrue(tracesLabels.contains(List.of("A", "B", "C", "D", "E", "B", "C")));
        assertTrue(tracesLabels.contains(List.of("A", "B", "C", "D", "C", "D", "C")));
    }

    @Test
    void tracesByLatency_CC_30() {
        List<List<Node>> traces = graphSearchServiceImpl.tracesByMaxLatency(graph, "C", "C", 30);
        assertEquals(7, traces.size());
    }
}