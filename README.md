# Exercise Solution

## Requirements

- Maven 3.5
- Java 11

## How to Run it

By default, the exercises will be executed with the input file located in the "resources" folder
at `src/main/resources/com/alejokf/graphs/TestInput.csv`. It is also possible to set another input file, by specifying
the path of the file as argument. The file can be located at:

- The "resources" folder of this project/jar, or
- Any file in the file system, either with a relative or absolute path

There are two options to run it:

1. With Maven \
   This option is lighter since less dependencies are downloaded. First build the project:
    ```
    mvn clean compile
    ```
   Run it with the default input file:
    ```
    mvn exec:java -Dexec.mainClass=com.alejokf.graphs.mainapp.Exercises
    ```
   Run it with another input file:
    ```
    mvn exec:java -Dexec.mainClass=com.alejokf.graphs.mainapp.Exercises -Dexec.args="FILE_PATH"
    ```

2. With a Jar (Fat Jar) \
   First build the project:
    ```
    mvn clean package
    ```
   Run it with the default input file:
    ```
    java -jar target/excercise-graphs-in-1.0-SNAPSHOT-jar-with-dependencies.jar
    ```
   Run it with another input file:
    ```
    java -jar target/excercise-graphs-in-1.0-SNAPSHOT-jar-with-dependencies.jar FILE_PATH
    ```

## Considerations

### Graphs

A graph can be represented by an Adjacency Matrix or by an Adjacency List. The first approach has a bigger memory
consumption, so I decided to use the Adjacency List representation, which is:

- A list of nodes
- A list of edges
- edge node points to its two nodes
- each node points to edges incident on it

### Special considerations

The exercises have some special conditions (when compared to "traditional" graph exercises) that need to be considered:

1. The shortest path starting from a node and ending on a node is not 0. Instead, the distances of the non-empty traces
   starting and ending in the same node are considered.

2. Related to the point above, when searching the graph or navigating through a given trace, even if all nodes have
   already been explored, this is not a condition of termination of the search/navigation. This means a node can already
   be explored, and if it is reached again, the search/navigation of the graph is continued until a stop condition is
   met. The stop condition will vary given by the nature of the specific exercise.

### Exercises Approach

#### Points 1 to 5: traversing the Graph through a given trace

No special or known algorithm was used in this case. The approach is simple with the Adjacency List representation of
the graph: start on the first node of the given trace, and from then, navigate to the following node using the outgoing
edges of that node. We accumulate the latency as the given trace is traversed. If it is not possible to find the next
node in the trace, 'NO SUCH TRACE' is returned, as requested.

#### Point 6, 7 and 10

For these points a "Graph Search" is needed, which consists in navigating the Graph from a starting node. I decided to
use a DSF (Depth-Search-First) approach when searching the graph, which navigates through all nodes of the graph going
through a single path first until a condition is met and backtracking only when it is necessary. The algorithm was
generalized to make it reusable for three points, by using two predicates:

- one predicate to check if the navigation of the graph should continue further from the current path
- one predicate to check if the current path meets the condition asked in the specific exercise (e.g. max hops)

There is another approach to do a "Graph Search": BSF (Breath-Search-First). This other approach navigates the graph
in "layers". The first layer is only the initial node, and the next layer is the set of nodes that have not been
explored yet and that have an edge in the previous layer (already explored). Since in this case, a node can be explored
more than once (see Special Considerations above), I'm not comlpetely sure if this approach would work, so I decided to
use DSF.

#### Point 8 and 9

For these two points I used the Dijkstra's algorithm to compute the shortest path between a source node and every other
node in the graph. The algorithm runs as follows:

- Let's call N the complete set of nodes
- Let's call P the set of processed nodes
- Therefore (N-P) is the set of non-processed nodes

1. Initially, the source node is the only one in P*.
2. From all edges whose tail is in P and whose tail is in (N-P), we choose the one head node of that edge that minimizes
   the Dijkstra's Greedy Score. This score is the score of the tail node (previously computed) + the weight of the edge.
3. Add that node to N.
4. Continue until all nodes are processed.

*: This is actually one of the special conditions of this exercise. The source node is actually not considered part of
the already processed nodes, but its outgoing edges are considered as "coming out" of this set for the algorithm.

The running time of this algorithm is O(e*n), with e = number de edges, and n = number of nodes.

There is an optimization to this algorithm, implemented in this exercise, which is to use a Heap to maintain the min
score of a node in (N-P), considering all incoming edges whose tail is in (N). A Heap always maintain a min (or max)
value in the top, and its read and write operations running time is O(log(n)).

Therefore, with this improvement, the running time of the search algorithm is O(e*log(n))

### TDD

Giving the nature of the exercises, unit tests were critical for its solution. TDD was used during the implementation. 

