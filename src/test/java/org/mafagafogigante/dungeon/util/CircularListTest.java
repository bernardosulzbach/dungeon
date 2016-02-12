package org.mafagafogigante.dungeon.util;

import org.junit.Assert;
import org.junit.Test;

public class CircularListTest {

  @Test
  public void testAdd() throws Exception {
    CircularList<Integer> circularList = new CircularList<>(2);
    circularList.add(1);
    Assert.assertTrue(circularList.get(0).equals(1));
    circularList.add(2);
    Assert.assertTrue(circularList.get(0).equals(1));
    Assert.assertTrue(circularList.get(1).equals(2));
    circularList.add(3);
    Assert.assertTrue(circularList.get(0).equals(2));
    Assert.assertTrue(circularList.get(1).equals(3));
    circularList.add(4);
    Assert.assertTrue(circularList.get(0).equals(3));
    Assert.assertTrue(circularList.get(1).equals(4));
  }

  @Test
  public void testSize() throws Exception {
    final int CAPACITY = 10;
    CircularList<Integer> circularList = new CircularList<>(CAPACITY);
    Assert.assertEquals(0, circularList.size());
    for (int i = 0; i <= 2 * CAPACITY; i++) { // i is how many elements we have already added.
      Assert.assertEquals(java.lang.Math.min(i, CAPACITY), circularList.size());
      circularList.add(i);
    }
  }

  @Test
  public void testIsFull() throws Exception {
    CircularList<Integer> circularList = new CircularList<>(2);
    Assert.assertFalse(circularList.isFull());
    circularList.add(1);
    Assert.assertFalse(circularList.isFull());
    circularList.add(2);
    Assert.assertTrue(circularList.isFull());
    circularList.add(3);
    Assert.assertTrue(circularList.isFull());
  }

  @Test
  public void testIsEmpty() throws Exception {
    CircularList<Integer> circularList = new CircularList<>(1);
    Assert.assertTrue(circularList.isEmpty());
    circularList.add(1);
    Assert.assertFalse(circularList.isEmpty());
  }

  @Test
  public void testGet() throws Exception {
    final int CAPACITY = 10;
    CircularList<Integer> circularList = new CircularList<>(CAPACITY);
    for (int i = 0; i <= 2 * CAPACITY; i++) {
      circularList.add(i);
      Assert.assertTrue(circularList.get(java.lang.Math.min(i, CAPACITY - 1)).equals(i));
    }
  }

}
