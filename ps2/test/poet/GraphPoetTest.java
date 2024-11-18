/* Copyright (c) 2015-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package poet;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

/**
 * Tests for GraphPoet.
 */
public class GraphPoetTest {
    
    // Testing strategy
    //   TODO
    
    @Test(expected=AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; // make sure assertions are enabled with VM argument: -ea
    }
    @Test
    public void testPoemWithBridgeWords() throws IOException {
        // using the existing corpus in ps2
        File corpus = new File("mugar-omni-theater2.txt");
        GraphPoet poet = new GraphPoet(corpus);
        
        String input = "Test the system.";
        String result = poet.poem(input);
        //check addition of bridge word
        assertEquals("Test of the system.", result);
    }
    
    @Test
    public void testPoemNoBridgeWords() throws IOException {
      
        File corpus = new File("emptyBridgeCorpus.txt"); // Corpus with no bridge words i.e. it is empty in this scenario
        GraphPoet poet = new GraphPoet(corpus);
        String input = "Hello world.";
        String result = poet.poem(input);
        //no change will be observed as no bridge words
        assertEquals("Hello world.", result);
    }

    @Test
    public void testEmptyInput() throws IOException {
        // edge case with empty string
        File corpus = new File("mugar-omni-theater2.txt");
        GraphPoet poet = new GraphPoet(corpus);
        String input = "";
        String result = poet.poem(input);
        assertEquals("", result);
    }

    @Test
    public void testEmptyCorpus() throws IOException {
        File corpus = new File("emptyCorpus.txt"); // An empty file
        GraphPoet poet = new GraphPoet(corpus);
        String input = "Hello world.";
        String result = poet.poem(input);
        assertEquals("Hello world.", result);
    }

    @Test
    public void testCaseInsensitivity() throws IOException {
    	//case insensitive when generating poems but case preserving in poem display
        File corpus = new File("testCorpusCaseInsensitive.txt");
        GraphPoet poet = new GraphPoet(corpus);
        String input = "hello WORLD";
        String result = poet.poem(input);
        assertEquals("hello, WORLD", result); 
    }

    @Test
    public void testMultipleBridgeWords() throws IOException {
        File corpus = new File("mugar-omni-theater2.txt");//has multiple bridge words
        GraphPoet poet = new GraphPoet(corpus);
        String input = "Is a graph with words.";
        String result = poet.poem(input);
        assertEquals("This Is a raph with words.", result);
    }

    @Test
    public void testPunctuationPreservation() throws IOException {
        File corpus = new File("testCorpusPunctuation.txt");
        GraphPoet poet = new GraphPoet(corpus);
        String input = "Hello, world.";
        String result = poet.poem(input);
        assertEquals("Hello, poetic world.", result);
    }
    
    
}
