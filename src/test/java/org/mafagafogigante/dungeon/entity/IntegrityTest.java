package org.mafagafogigante.dungeon.entity;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class IntegrityTest {

  @Test
  public void testIsMaximum() throws Exception {
    Assertions.assertTrue(new Integrity(2, 2).isMaximum());
    Assertions.assertFalse(new Integrity(2, 1).isMaximum());
    Assertions.assertFalse(new Integrity(2, 0).isMaximum());
  }

  @Test
  public void testIsZero() throws Exception {
    Assertions.assertTrue(new Integrity(2, 0).isZero());
    Assertions.assertFalse(new Integrity(2, 1).isZero());
    Assertions.assertFalse(new Integrity(2, 2).isZero());
  }

  @Test
  public void incrementByShouldWorkWithPositiveValues() throws Exception {
    Integrity integrity = new Integrity(1, 0);
    Assertions.assertEquals(0, integrity.getCurrent());
    integrity.incrementBy(1);
    Assertions.assertEquals(1, integrity.getCurrent());
    integrity.incrementBy(1);
    Assertions.assertEquals(1, integrity.getCurrent());
  }

  @Test
  public void incrementByShouldWorkWithNegativeValues() throws Exception {
    Integrity integrity = new Integrity(1, 1);
    Assertions.assertEquals(1, integrity.getCurrent());
    integrity.incrementBy(-1);
    Assertions.assertEquals(0, integrity.getCurrent());
    integrity.incrementBy(-1);
    Assertions.assertEquals(0, integrity.getCurrent());
  }

  @Test
  public void decrementByShouldWorkWithPositiveValues() throws Exception {
    Integrity integrity = new Integrity(1, 1);
    Assertions.assertEquals(1, integrity.getCurrent());
    integrity.decrementBy(1);
    Assertions.assertEquals(0, integrity.getCurrent());
    integrity.decrementBy(1);
    Assertions.assertEquals(0, integrity.getCurrent());
  }

  @Test
  public void decrementByShouldWorkWithNegativeValues() throws Exception {
    Integrity integrity = new Integrity(1, 0);
    Assertions.assertEquals(0, integrity.getCurrent());
    integrity.decrementBy(-1);
    Assertions.assertEquals(1, integrity.getCurrent());
    integrity.decrementBy(-1);
    Assertions.assertEquals(1, integrity.getCurrent());
  }

}
