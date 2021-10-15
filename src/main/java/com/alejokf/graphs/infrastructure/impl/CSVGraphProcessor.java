package com.alejokf.graphs.infrastructure.impl;

import com.alejokf.graphs.domain.Edge;
import com.alejokf.graphs.domain.Graph;
import com.alejokf.graphs.domain.Node;
import com.alejokf.graphs.infrastructure.GraphProcessor;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Component;

import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Service to process a graph in a CSV file
 */
@Component
public class CSVGraphProcessor implements GraphProcessor {

    /**
     * {@inheritDoc}
     * Reads a graph from the specified {@code filePath} in CSV format
     */
    public Graph readFromFile(String filePath) throws IOException {
        Map<String, Node> nodes = new HashMap<>();
        List<Edge> edges = new ArrayList<>();

        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(filePath);
        Reader in = inputStream != null ? new InputStreamReader(inputStream) : new FileReader(filePath);
        Iterable<CSVRecord> records = CSVFormat.DEFAULT.parse(in);
        for (CSVRecord record : records) {
            String tailLabel = record.get(0);
            String headLabel = record.get(1);
            Long weight = Long.parseLong(record.get(2));

            Node tail = nodes.get(tailLabel);
            if (tail == null) {
                tail = new Node(tailLabel);
                nodes.put(tailLabel, tail);
            }

            Node head = nodes.get(headLabel);
            if (head == null) {
                head = new Node(headLabel);
                nodes.put(headLabel, head);
            }

            Edge edge = new Edge(tail, head, weight);
            head.addEdgeTo(edge);
            tail.addEdgeFrom(edge);

            edges.add(edge);
        }

        return new Graph(nodes, edges);
    }
}
