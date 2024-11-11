/* Copyright (c) 2015-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package graph;

import static org.junit.Assert.*;


import java.util.Collections;

import org.junit.Test;

/**
 * Tests for instance methods of Graph.
 * 
 * <p>PS2 instructions: you MUST NOT add constructors, fields, or non-@Test
 * methods to this class, or change the spec of {@link #emptyInstance()}.
 * Your tests MUST only obtain Graph instances by calling emptyInstance().
 * Your tests MUST NOT refer to specific concrete implementations.
 */
public abstract class GraphInstanceTest {
    
    // Testing strategy
    //   TODO
    
    /**
     * Overridden by implementation-specific test classes.
     * 
     * @return a new empty graph of the particular implementation being tested
     */
    public abstract Graph<String> emptyInstance();
    
    @Test(expected=AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; // make sure assertions are enabled with VM argument: -ea
    }

    @Test
    public void testInitialVerticesEmpty() {
        // TODO you may use, change, or remove this test
        assertEquals("expected new graph to have no vertices",
                Collections.emptySet(), emptyInstance().vertices());
    }
    @Test
    public void testAddVertex() {//check if vertex added
        Graph<String> graph = emptyInstance();
        assertTrue("Vertex A should be added", graph.add("A"));
        assertFalse("Adding the same vertex should return false", graph.add("A"));
    }

    @Test
    public void testAddEdge() {//check if edge added between 2 vertices
        Graph<String> graph = emptyInstance();
        graph.add("A");
        graph.add("B");
        assertEquals("Edge weight should be set", 5, graph.set("A", "B", 5));
        assertEquals("Edge weight should be updated", 5, graph.set("A", "B", 10));//check if edge value updated
    }
    
    @Test
    public void testRemoveValidVertex() {//removing existing vertex
        Graph<String> graph = emptyInstance();
        graph.add("A");
        graph.add("B");
        graph.set("A", "B", 10);
        
        assertTrue("Vertex A should be removed", graph.remove("A"));
        assertFalse("Vertex A should no longer exist", graph.vertices().contains("A"));
        assertTrue("Vertex B should still exist", graph.vertices().contains("B"));//do not delete edge connected vertex as well
    }

    @Test
    public void testRemoveNonExistentVertex() {///removing a vertex that did not exist
    	Graph<String> graph =emptyInstance();
        graph.add("A");
        
        assertFalse("Removing non-existent vertex should return false", graph.remove("B"));
    }

    @Test
    public void testRemoveEdgeWhenVertexRemoved() {//remove edge but do not remove the vertex associated on the other end
    	Graph<String> graph = emptyInstance();
        graph.add("A");
        graph.add("B");
        graph.set("A", "B", 5);
        
        assertTrue("Vertex A should be removed", graph.remove("A"));
        assertEquals("Edge from A to B should be removed", 0, graph.set("A", "B", 0));
        assertTrue("Vertex B should still exist", graph.vertices().contains("B"));
    }

    @Test
    public void testRemoveEdgeExplicitly() {
    	Graph<String> graph = emptyInstance();
        graph.add("P");
        graph.add("Q");
        graph.set("P", "Q", 30);
        
        assertEquals("Edge from P to Q should exist with weight 30", 30, graph.set("P", "Q", 0));
        assertEquals("Removing the edge should return previous weight", 0, graph.set("P", "Q", 0));
    }
    
}
