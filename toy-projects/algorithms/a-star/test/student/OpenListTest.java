package student;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class OpenListTest {

    @Test
    public void testPriorityIsPreserved() throws Exception {
        OpenList<String> list = new OpenList<>();

        list.setNextCost(5);
        list.add("red");

        list.setNextCost(2);
        list.add("green");

        list.setNextCost(10);
        list.add("black");

        assertEquals("green", list.retrieveBest());
        assertEquals("red", list.retrieveBest());
        assertEquals("black", list.retrieveBest());
        assertTrue(list.isEmpty());
    }

    @Test
    public void testPriorityChanged() throws Exception {
        OpenList<String> list = new OpenList<>();

        list.setNextCost(5);
        list.add("red");

        list.setNextCost(10);
        list.add("black");

        list.update("black", 0.0);

        assertEquals("black", list.retrieveBest());
        assertEquals("red", list.retrieveBest());
        assertTrue(list.isEmpty());
    }

    @Test
    public void testPriorityChangedThenChangedBack() throws Exception {
        OpenList<String> list = new OpenList<>();

        list.setNextCost(5);
        list.add("red");

        list.setNextCost(10);
        list.add("black");

        list.update("red", 12.0);
        list.update("red", 2.0);

        assertEquals("red", list.retrieveBest());
        assertEquals("black", list.retrieveBest());
        assertTrue(list.isEmpty());
    }
}