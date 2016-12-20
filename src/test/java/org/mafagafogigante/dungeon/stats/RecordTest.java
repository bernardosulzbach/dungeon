package org.mafagafogigante.dungeon.stats;

import org.mafagafogigante.dungeon.stats.Record.Type;

import org.junit.Assert;
import org.junit.Test;

public class RecordTest {

  @Test
  public void testUpdate() throws Exception {
    Record maximumRecord = new Record(Type.MAXIMUM);
    Assert.assertNull(maximumRecord.getValue());
    Assert.assertEquals("N/A", maximumRecord.toString());
    maximumRecord.update(1);
    Assert.assertTrue(maximumRecord.getValue().equals(1));
    Assert.assertEquals("1", maximumRecord.toString());
    maximumRecord.update(0); // An update that does not change the Record.
    Assert.assertTrue(maximumRecord.getValue().equals(1));
    Assert.assertEquals("1", maximumRecord.toString());
    maximumRecord.update(2); // An update that changes the Record.
    Assert.assertTrue(maximumRecord.getValue().equals(2));
    Assert.assertEquals("2", maximumRecord.toString());

    Record minimumRecord = new Record(Type.MINIMUM);
    Assert.assertNull(minimumRecord.getValue());
    Assert.assertEquals("N/A", minimumRecord.toString());
    minimumRecord.update(1);
    Assert.assertTrue(minimumRecord.getValue().equals(1));
    Assert.assertEquals("1", minimumRecord.toString());
    minimumRecord.update(0); // An update that changes the Record.
    Assert.assertTrue(minimumRecord.getValue().equals(0));
    Assert.assertEquals("0", minimumRecord.toString());
    minimumRecord.update(2); // An update that does not change the Record.
    Assert.assertTrue(minimumRecord.getValue().equals(0));
    Assert.assertEquals("0", minimumRecord.toString());
  }

}
