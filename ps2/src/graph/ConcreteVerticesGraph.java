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

public class ConcreteVerticesGraph implements Graph<String> {
    
    private final List<Vertex> vertices = new ArrayList<>();
  
    
    public ConcreteVerticesGraph() {
        checkRep();
    }

    private void checkRep() {
        assert vertices != null;
        Set<String> labels = new HashSet<>();
        for (Vertex vertex : vertices) {
            assert vertex != null;
            assert vertex.getLabel() != null;
            assert !labels.contains(vertex.getLabel());
            labels.add(vertex.getLabel());
            for (int weight : vertex.getTargets().values()) {
                assert weight >= 0;
            }
        }
    }
    
    @Override public boolean add(String vertex) {
    	if (vertex == null) return false;
        for (Vertex v : vertices) {
            if (v.getLabel().equals(vertex)) {
                return false;
            }
        }
        vertices.add(new Vertex(vertex));
        checkRep();
        return true;
    }
    
    @Override public int set(String source, String target, int weight) {
    	if (source == null || target == null || weight < 0) {
            throw new IllegalArgumentException("Invalid source, target, or weight.");
        }

        add(source);
        add(target);

        Vertex sourceVertex = getVertex(source);
        int previousWeight = sourceVertex.setTarget(target, weight);

        checkRep();
        return previousWeight;
    }
    
    @Override public boolean remove(String vertex) {
    	 if (vertex == null) return false;
         Vertex toRemove = getVertex(vertex);
         if (toRemove == null) return false;

         vertices.remove(toRemove);
         for (Vertex v : vertices) {
             v.setTarget(vertex, 0);
         }

         checkRep();
         return true;
    }
    
    @Override public Set<String> vertices() {
    	Set<String> vertexLabels = new HashSet<>();
        for (Vertex v : vertices) {
            vertexLabels.add(v.getLabel());
        }
        return Collections.unmodifiableSet(vertexLabels);
    }
    
    @Override public Map<String, Integer> sources(String target) {
    	Map<String, Integer> result = new HashMap<>();
        for (Vertex v : vertices) {
            Integer weight = v.getTargets().get(target);
            if (weight != null && weight > 0) {
                result.put(v.getLabel(), weight);
            }
        }
        return Collections.unmodifiableMap(result);
    }
    
    @Override public Map<String, Integer> targets(String source) {
    	 Vertex vertex = getVertex(source);
         if (vertex == null) {
             return Collections.emptyMap();
         }
         return Collections.unmodifiableMap(new HashMap<>(vertex.getTargets()));
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Graph:\n");
        for (Vertex v : vertices) {
            sb.append(v.toString()).append("\n");
        }
        return sb.toString();
    }
    //get vertex label
    private Vertex getVertex(String label) {
        for (Vertex v : vertices) {
            if (v.getLabel().equals(label)) {
                return v;
            }
        }
        return null;
    }
}
class Vertex {
	 private final String label;
	    private final Map<String, Integer> targets = new HashMap<>();
    
	    public Vertex(String label) {
	        if (label == null) {
	            throw new IllegalArgumentException("Label cannot be null.");
	        }
	        this.label = label;
	        checkRep();
	    }

	    // Check representation invariant
	    private void checkRep() {
	        assert label != null;
	        for (Map.Entry<String, Integer> entry : targets.entrySet()) {
	            assert entry.getKey() != null;
	            assert entry.getValue() != null && entry.getValue() >= 0;
	        }
	    }

	    public String getLabel() {
	        return label;
	    }

	    public Map<String, Integer> getTargets() {
	        return Collections.unmodifiableMap(targets);
	    }

	    public int setTarget(String target, int weight) {
	        if (weight < 0) {
	            throw new IllegalArgumentException("Weight cannot be negative.");
	        }

	        Integer previousWeight = targets.get(target);
	        if (weight == 0) {
	            targets.remove(target);
	        } else {
	            targets.put(target, weight);
	        }
	        checkRep();
	        return previousWeight == null ? 0 : previousWeight;
	    }

	    @Override
	    public String toString() {
	        return label + " -> " + targets.toString();
	    }
    
}
