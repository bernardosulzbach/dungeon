package org.mafagafogigante.dungeon.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class CircularListTest {

  private static final int CIRCULAR_LIST_CAPACITY = 16;

  @Test
  public void testAdd() throws Exception {
    CircularList<Integer> circularList = new CircularList<>(2);
    circularList.add(1);
    Assertions.assertEquals(1, (int) circularList.get(0));
    circularList.add(2);
    Assertions.assertEquals(1, (int) circularList.get(0));
    Assertions.assertEquals(2, (int) circularList.get(1));
    circularList.add(3);
    Assertions.assertEquals(2, (int) circularList.get(0));
    Assertions.assertEquals(3, (int) circularList.get(1));
    circularList.add(4);
    Assertions.assertEquals(3, (int) circularList.get(0));
    Assertions.assertEquals(4, (int) circularList.get(1));
  }

  @Test
  public void testSize() throws Exception {
    CircularList<Integer> circularList = new CircularList<>(CIRCULAR_LIST_CAPACITY);
    Assertions.assertEquals(0, circularList.size());
    for (int i = 0; i <= 2 * CIRCULAR_LIST_CAPACITY; i++) { // i is how many elements we have already added.
      Assertions.assertEquals(Math.min(i, CIRCULAR_LIST_CAPACITY), circularList.size());
      circularList.add(i);
    }
  }

  @Test
  public void testIsFull() throws Exception {
    CircularList<Integer> circularList = new CircularList<>(2);
    Assertions.assertFalse(circularList.isFull());
    circularList.add(1);
    Assertions.assertFalse(circularList.isFull());
    circularList.add(2);
    Assertions.assertTrue(circularList.isFull());
    circularList.add(3);
    Assertions.assertTrue(circularList.isFull());
  }

  @Test
  public void testIsEmpty() throws Exception {
    CircularList<Integer> circularList = new CircularList<>(1);
    Assertions.assertTrue(circularList.isEmpty());
    circularList.add(1);
    Assertions.assertFalse(circularList.isEmpty());
  }

  @Test
  public void testGet() throws Exception {
    CircularList<Integer> circularList = new CircularList<>(CIRCULAR_LIST_CAPACITY);
    for (int i = 0; i <= 2 * CIRCULAR_LIST_CAPACITY; i++) {
      circularList.add(i);
      Assertions.assertEquals((int) circularList.get(Math.min(i, CIRCULAR_LIST_CAPACITY - 1)), i);
    }
  }

}
