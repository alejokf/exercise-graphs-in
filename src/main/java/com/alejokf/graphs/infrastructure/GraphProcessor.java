package com.alejokf.graphs.infrastructure;

import com.alejokf.graphs.domain.Graph;

import java.io.IOException;

public interface GraphProcessor {

    /**
     * Reads a graph from the specified {@code filePath}.
     *
     * The path can point to a file in the resources folder of this project/jar or to a file in the file system
     *
     * @param filePath the path of the file containing the graph information
     * @return a Graph representing the graph in the file
     * @throws IOException in case an IO error occurs when processing the file
     */
    Graph readFromFile(String filePath) throws IOException;
}
