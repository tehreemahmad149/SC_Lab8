/* Copyright (c) 2015-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package graph;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * An implementation of Graph.
 * 
 * <p>PS2 instructions: you MUST use the provided rep.
 * Used below
 */
public class ConcreteEdgesGraph implements Graph<String> {
    
    private final Set<String> vertices = new HashSet<>();
    private final List<Edge> edges = new ArrayList<>();
   
    public ConcreteEdgesGraph() {
        confirmrep();
    }

    private void confirmrep() {
        assert vertices != null;
        assert edges != null;
        for (Edge edge : edges) {
            assert edge != null;
            assert edge.weight() >= 0;
            assert vertices.contains(edge.source());
            assert vertices.contains(edge.target());
      
        }
    }
    @Override 
    public boolean add(String vertex) {
    	if (vertex == null || vertices.contains(vertex)) {
            return false;
        }
        boolean added = vertices.add(vertex);
        confirmrep();
        return added;
    }
    
    @Override 
    public int set(String source, String target, int weight) {
    	//validity checks 
    	if (source == null || target == null) {
            throw new IllegalArgumentException("Source or target cannot be null.");
        }
        if (weight < 0) {
            throw new IllegalArgumentException("Weight cannot be negative.");
        }

        add(source);
        add(target);

        for (Edge edge : edges) {
        	//casting for the class below
            if (((Edge) edge).source().equals(source) && edge.target().equals(target)) {
                int oldWeight = edge.weight();
                if (weight == 0) {
                    edges.remove(edge);
                } else {
                    edges.remove(edge);
                    edges.add(new Edge(source, target, weight));
                }
                confirmrep();
                return oldWeight;
            }
        }

        if (weight > 0) {//confirmed additon
            edges.add(new Edge(source, target, weight));
        }
        confirmrep();
        return 0;
    }
    
    @Override 
    public boolean remove(String vertex) {
    	if (vertex == null || !vertices.contains(vertex)) {
            return false;
        }

        vertices.remove(vertex);
        edges.removeIf(edge -> edge.source().equals(vertex) || edge.target().equals(vertex));
        confirmrep();
        return true;
    }
    
    @Override 
    public Set<String> vertices() {
    	return Collections.unmodifiableSet(new HashSet<>(vertices));//immutable
    }
    
    @Override 
    public Map<String, Integer> sources(String target) {
    	Map<String, Integer> result = new HashMap<>();
        for (Edge edge : edges) {
            if (edge.target().equals(target)) {
                result.put(edge.source(), edge.weight());
            }
        }
        return Collections.unmodifiableMap(result);
    }
    
    @Override 
    public Map<String, Integer> targets(String source) {
    	 Map<String, Integer> result = new HashMap<>();
         for (Edge edge : edges) {
             if (edge.source().equals(source)) {
                 result.put(edge.target(), edge.weight());
             }
         }
         return Collections.unmodifiableMap(result);
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Graph:\nVertices: " + vertices + "\nEdges:\n");
        for (Edge edge : edges) {
            sb.append(edge.toString()).append("\n");
        }
        return sb.toString();
    }
    
}

/* Rep Invariant:
 source and target labels must be non-null.
 weight must be non-negative.
 
Safety from Rep Exposure:
 Fields are private and final.
 Class is immutable .
*/

/**
 * TODO specification
 * Immutable.
 * This class is internal to the rep of ConcreteEdgesGraph.
 * 
 * <p>PS2 instructions: the specification and implementation of this class is
 * up to you.
 */
    class Edge {

        private final String source;
        private final String target;
        private final int weight;
   /*

        Representation invariant:
          - source and target are non-null.
          - weight is non-negative.

        Safety from rep exposure:
        All fields are private, final, and immutable.
        */

        // Constructor
        public Edge(String source, String target, int weight) {
            if (source == null || target == null || weight < 0) {
                throw new IllegalArgumentException("Invalid edge parameters.");
            }
            this.source = source;
            this.target = target;
            this.weight = weight;
            confirmrep();
        }

        private void confirmrep() {
        	//confirmation
            assert source != null;
            assert target != null;
            assert weight >= 0;
        }
        //class method functions

        public String source() {
            return source;
        }

        public String target() {
            return target;
        }

        public int weight() {
            return weight;
        }

        @Override
        public String toString() {//use java string concatenation
            return source + " -> " + target + " (weight: " + weight + ")";
        }

        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof Edge)) return false;
            Edge other = (Edge) obj;
            return this.source.equals(other.source) &&
                   this.target.equals(other.target) &&
                   this.weight == other.weight;
        }

        @Override
        public int hashCode() {
            return source.hashCode() + target.hashCode() + Integer.hashCode(weight);
        }
    }
