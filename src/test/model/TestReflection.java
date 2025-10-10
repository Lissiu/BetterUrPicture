package model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.util.ArrayList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TestReflection {
    Reflection testReflection;
    ArrayList<ProblemType> problems;
    ArrayList<String> comments;

    @BeforeEach
    void runBefore() {
        testReflection = new Reflection();
        problems = new ArrayList<ProblemType>();
        comments = new ArrayList<>();

    }

    @Test
    void tesrConstructor() {
        assertEquals(problems, testReflection.getProblems());
        assertEquals(comments, testReflection.getComments());
    }

    @Test
    void testAddOneComment() {
        testReflection.addComment("good");
        assertEquals("good", testReflection.getComments().get(0));
        assertEquals(1, testReflection.getComments().size());

    }

    @Test
    void testAddMultipleComments() {
        testReflection.addComment("good");
        testReflection.addComment("bad");
        assertEquals("good", testReflection.getComments().get(0));
        assertEquals("bad", testReflection.getComments().get(1));
        assertEquals(2, testReflection.getComments().size());

    }

    @Test
    void testRemoveCommentNull() {
        testReflection.addComment("good");
        testReflection.removeComment("bad");
        assertEquals(1, testReflection.getComments().size());
        assertEquals("good", testReflection.getComments().get(0));
    }

    @Test
    void testRemoveComment() {
        testReflection.addComment("good");
        testReflection.addComment("bad");
        testReflection.removeComment("bad");
        assertEquals(1, testReflection.getComments().size());
        assertEquals("good", testReflection.getComments().get(0));
    }

    @Test
    void testRemoveCommentOrder() {
        testReflection.addComment("good");
        testReflection.addComment("bad");
        testReflection.removeComment("good");
        assertEquals(1, testReflection.getComments().size());
        assertEquals("bad", testReflection.getComments().get(0));
    }

    @Test
    void testRemoveCommentTwice() {
        testReflection.addComment("good");
        testReflection.addComment("bad");
        testReflection.addComment("Cool");
        testReflection.removeComment("good");
        assertEquals(2, testReflection.getComments().size());
        assertEquals("bad", testReflection.getComments().get(0));
        assertEquals("Cool", testReflection.getComments().get(1));
        testReflection.removeComment("Cool");
        assertEquals(1, testReflection.getComments().size());
        assertEquals("bad", testReflection.getComments().get(0));
    }

    @Test
    void testRemoveCommentDuplicate() {
        testReflection.addComment("good");
        testReflection.addComment("bad");
        testReflection.addComment("Cool");
        assertEquals(3, testReflection.getComments().size());
        testReflection.removeComment("good");
        assertEquals(2, testReflection.getComments().size());
        testReflection.removeComment("good");
        assertEquals(2, testReflection.getComments().size());
        testReflection.removeComment("bad");
        assertEquals(1, testReflection.getComments().size());
    }

    @Test
    void testRemoveCommentBoundary() {
        testReflection.addComment("good");
        testReflection.addComment("bad");
        testReflection.addComment("Cool");
        testReflection.removeComment("good");
        testReflection.removeComment("bad");
        testReflection.removeComment("Cool");
        assertEquals(0, testReflection.getComments().size());
    }


    @Test
    void testAddProblemType() {
        testReflection.addProblemType(ProblemType.COLOR);
        assertEquals(ProblemType.COLOR, testReflection.getProblems().get(0));
        assertEquals(1, testReflection.getProblems().size());
    }

    @Test
    void testAddMultipleProblemType() {
        testReflection.addProblemType(ProblemType.COLOR);
        testReflection.addProblemType(ProblemType.COMPOSITION);
        assertEquals(ProblemType.COLOR, testReflection.getProblems().get(0));
        assertEquals(ProblemType.COMPOSITION, testReflection.getProblems().get(1));
        assertEquals(2, testReflection.getProblems().size());
    }




}
