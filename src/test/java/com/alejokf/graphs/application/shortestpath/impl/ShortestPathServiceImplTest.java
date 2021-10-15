package com.alejokf.graphs.application.shortestpath.impl;

import com.alejokf.graphs.application.shortestpath.ShortestPath;
import com.alejokf.graphs.application.shortestpath.ShortestPaths;
import com.alejokf.graphs.domain.Graph;
import com.alejokf.graphs.domain.Node;
import com.alejokf.graphs.infrastructure.impl.CSVGraphProcessor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

class ShortestPathServiceImplTest {

    private final ShortestPathServiceImpl shortestPathServiceImpl = new ShortestPathServiceImpl();
    private Graph graph;

    @BeforeEach
    void loadGraph() throws IOException {
        CSVGraphProcessor reader = new CSVGraphProcessor();
        graph = reader.readFromFile("com/alejokf/graphs/TestInput.csv");
    }

    @Test
    void shortestPath_FromA() {
        ShortestPaths shortestPathDistances = shortestPathServiceImpl.shortestPath(graph, "A");
        ShortestPath shortestPathAC = shortestPathDistances.getShortestDistances("C");
        assertNotNull(shortestPathAC);
        assertEquals(9L, shortestPathAC.getDistance());

        assertEquals("ABC", shortestPathAC.getNodes().stream().map(Node::getLabel).collect(Collectors.joining()));
    }

    @Test
    void shortestPath_FromB() {
        ShortestPaths shortestPathDistances = shortestPathServiceImpl.shortestPath(graph, "B");
        ShortestPath shortestPathBB = shortestPathDistances.getShortestDistances("B");
        assertNotNull(shortestPathBB);
        assertEquals(9L, shortestPathBB.getDistance());
    }

    @Test
    void shortestPath_FromC() {
        ShortestPaths shortestPathDistances = shortestPathServiceImpl.shortestPath(graph, "C");

        ShortestPath shortestPathCD = shortestPathDistances.getShortestDistances("D");
        assertNotNull(shortestPathCD);
        assertEquals(8L, shortestPathCD.getDistance());

        ShortestPath shortestPathCC = shortestPathDistances.getShortestDistances("C");
        assertNotNull(shortestPathCC);
        assertEquals(9L, shortestPathCC.getDistance());

        ShortestPath shortestPathCA = shortestPathDistances.getShortestDistances("A");
        assertNull(shortestPathCA);


    }
}