/* Copyright (c) 2015-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package poet;


import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.Map;

import graph.Graph;
/**
 * A graph-based poetry generator.
 * 
 * <p>GraphPoet is initialized with a corpus of text, which it uses to derive a
 * word affinity graph.
 * Vertices in the graph are words. Words are defined as non-empty
 * case-insensitive strings of non-space non-newline characters. They are
 * delimited in the corpus by spaces, newlines, or the ends of the file.
 * Edges in the graph count adjacencies: the number of times "w1" is followed by
 * "w2" in the corpus is the weight of the edge from w1 to w2.
 * 
 * <p>For example, given this corpus:
 * <pre>    Hello, HELLO, hello, goodbye!    </pre>
 * <p>the graph would contain two edges:
 * <ul><li> ("hello,") -> ("hello,")   with weight 2
 *     <li> ("hello,") -> ("goodbye!") with weight 1 </ul>
 * <p>where the vertices represent case-insensitive {@code "hello,"} and
 * {@code "goodbye!"}.
 * 
 * <p>Given an input string, GraphPoet generates a poem by attempting to
 * insert a bridge word between every adjacent pair of words in the input.
 * The bridge word between input words "w1" and "w2" will be some "b" such that
 * w1 -> b -> w2 is a two-edge-long path with maximum-weight weight among all
 * the two-edge-long paths from w1 to w2 in the affinity graph.
 * If there are no such paths, no bridge word is inserted.
 * In the output poem, input words retain their original case, while bridge
 * words are lower case. The whitespace between every word in the poem is a
 * single space.
 * 
 * <p>For example, given this corpus:
 * <pre>    This is a test of the Mugar Omni Theater sound system.  </pre>
 * <p>on this input:
 * <pre>    Test the system.    </pre>
 * <p>the output poem would be:
 * <pre>    Test of the system.    </pre>
 * 
 * <p>PS2 instructions: this is a required ADT class, and you MUST NOT weaken
 * the required specifications. However, you MAY strengthen the specifications
 * and you MAY add additional methods.
 * You MUST use Graph in your rep, but otherwise the implementation of this
 * class is up to you.
 */
public class GraphPoet {
    
    private final Graph<String> graph = Graph.empty();
    
    // Abstraction function:
    //   The graph represents a word affinity graph, where vertices are unique words from the corpus, 
    //   and edges represent adjacency counts between words (edge weights).
    // Representation invariant:
    //   - All vertices in the graph are non-empty, non-null strings.
    //   - Edge weights are positive integers.
    // Safety from rep exposure:
    //   - The graph field is private and final, and its references are not exposed.
    
    /**
     * Create a new poet with the graph from the given corpus.
     *
     * @param corpus text file from which to derive the poet's affinity graph
     * @throws IOException if the corpus file cannot be found or read
     */
    public GraphPoet(File corpus) throws IOException {
        List<String> lines = Files.readAllLines(corpus.toPath());
        String content = String.join(" ", lines);//concatenate all lines in a single string as pre-condition of buildGraoh()
        buildGraph(content);
        checkRep();
    }
    
    private void buildGraph(String content) {
        String[] words = content.toLowerCase().split("\\s+"); // separate words to build corpus
        for (int i = 0; i < words.length - 1; i++) {
        	//adjacent words
            String word1 = words[i];
            String word2 = words[i + 1];
            graph.add(word1);
            graph.add(word2);
            int currentWeight = graph.set(word1, word2, 0);
            graph.set(word1, word2, currentWeight + 1);
        }
    }
    
    private void checkRep() {
        for (String vertex : graph.vertices()) {
            assert vertex != null && !vertex.isEmpty();
            for (Map.Entry<String, Integer> edge : graph.targets(vertex).entrySet()) {
                assert edge.getValue() > 0;
            }
        }
    }
    
    /**
     * Generate a poem.
     * 
     * @param input string from which to create the poem
     * @return poem (as described above)
     */
    public String poem(String input) {
        String[] words = input.split("\\s+");
        StringBuilder poem = new StringBuilder();//mutable string object to allow bridge word addition
        
        for (int i = 0; i < words.length - 1; i++) {
            String word1 = words[i];
            String word2 = words[i + 1];
            String bridge = findBridgeWord(word1.toLowerCase(), word2.toLowerCase());
            poem.append(word1).append(" ");
            if (bridge != null) {
                poem.append(bridge).append(" ");
            }
        }
        poem.append(words[words.length - 1]); // add adjacent word to complete the string
        
        return poem.toString();
    }
    private String findBridgeWord(String word1, String word2) {
        String bestBridge = null;
        int maxWeight = 0;
        
        Map<String, Integer> targets = graph.targets(word1);// Get all direct neighbors of word1 along with their weights
        for (Map.Entry<String, Integer> entry : targets.entrySet()) {
            String candidate = entry.getKey();
            int weight1 = entry.getValue();
         // Check if there is an edge from the candidate to word2
            int weight2 = graph.targets(candidate).getOrDefault(word2, 0);
            int totalWeight = weight1 + weight2;
            
            if (weight2 > 0 && totalWeight > maxWeight) {//altering best bridge word found
                maxWeight = totalWeight;
                bestBridge = candidate;
            }
        }
        return bestBridge;
    }
    
    // TODO toString()
    @Override
    public String toString() {//helper function
        return "GraphPoet: " + graph.toString();
    }
    
}
