package com.alejokf.graphs.mainapp;

import com.alejokf.graphs.application.search.GraphSearchService;
import com.alejokf.graphs.application.search.GraphTraversalService;
import com.alejokf.graphs.application.shortestpath.ShortestPaths;
import com.alejokf.graphs.application.shortestpath.ShortestPathService;
import com.alejokf.graphs.config.DIConfig;
import com.alejokf.graphs.domain.Graph;
import com.alejokf.graphs.domain.Node;
import com.alejokf.graphs.infrastructure.GraphProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.io.IOException;
import java.util.List;

/**
 *
 */
public class Exercises {

    private static final Logger logger = LoggerFactory.getLogger(Exercises.class);
    private static final String DEFAULT_INPUT = "com/alejokf/graphs/TestInput.csv";

    public static void main(String[] args) {
        String inputGraphPath = args.length > 0 ? args[0] : DEFAULT_INPUT;
        logger.info("Using graph input at " + inputGraphPath);

        Exercises exercises = new Exercises();
        exercises.runExercises(inputGraphPath);
    }

    public void runExercises(String inputGraphPath) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(DIConfig.class);
        GraphProcessor csvProcessor = context.getBean(GraphProcessor.class);
        Graph graph = null;
        try {
            graph = csvProcessor.readFromFile(inputGraphPath);
        } catch (IOException e) {
            logger.error("Could not read CSV file " + inputGraphPath, e);
        }

        if (graph != null) {
            GraphTraversalService graphTraversalService = context.getBean(GraphTraversalService.class);
            GraphSearchService graphSearchService = context.getBean(GraphSearchService.class);
            ShortestPathService shortestPathService = context.getBean(ShortestPathService.class);

            String ex1 = graphTraversalService.traceLatencyOutput(graph, List.of("A", "B", "C"));
            logger.info("1. " + ex1);

            String ex2 = graphTraversalService.traceLatencyOutput(graph, List.of("A", "D"));
            logger.info("2. " + ex2);

            String ex3 = graphTraversalService.traceLatencyOutput(graph, List.of("A", "D", "C"));
            logger.info("3. " + ex3);

            String ex4 = graphTraversalService.traceLatencyOutput(graph, List.of("A", "E", "B", "C", "D"));
            logger.info("4. " + ex4);

            String ex5 = graphTraversalService.traceLatencyOutput(graph, List.of("A", "E", "D"));
            logger.info("5. " + ex5);

            List<List<Node>> ex6 = graphSearchService.tracesByMaxHops(graph, "C", "C", 3);
            logger.info("6. " + ex6.size());

            List<List<Node>> ex7 = graphSearchService.tracesByExactHops(graph, "A", "C", 4);
            logger.info("7. " + ex7.size());

            ShortestPaths ex8 = shortestPathService.shortestPath(graph, "A");
            logger.info("8. " + ex8.getShortestDistances("C").getDistance());
            graph.cleanup();

            ShortestPaths ex9 = shortestPathService.shortestPath(graph, "B");
            logger.info("9. " + ex9.getShortestDistances("B").getDistance());
            graph.cleanup();

            List<List<Node>> ex10 = graphSearchService.tracesByMaxLatency(graph, "C", "C", 30);
            logger.info("10. " + ex10.size());
        }
    }
}
