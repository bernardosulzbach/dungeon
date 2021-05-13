package org.mafagafogigante.dungeon.stats;

import org.mafagafogigante.dungeon.stats.Record.Type;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class RecordTest {

  @Test
  public void testUpdate() throws Exception {
    Record maximumRecord = new Record(Type.MAXIMUM);
    Assertions.assertNull(maximumRecord.getValue());
    Assertions.assertEquals("N/A", maximumRecord.toString());
    maximumRecord.update(1);
    Assertions.assertEquals(1, (int) maximumRecord.getValue());
    Assertions.assertEquals("1", maximumRecord.toString());
    maximumRecord.update(0); // An update that does not change the Record.
    Assertions.assertEquals(1, (int) maximumRecord.getValue());
    Assertions.assertEquals("1", maximumRecord.toString());
    maximumRecord.update(2); // An update that changes the Record.
    Assertions.assertEquals(2, (int) maximumRecord.getValue());
    Assertions.assertEquals("2", maximumRecord.toString());

    Record minimumRecord = new Record(Type.MINIMUM);
    Assertions.assertNull(minimumRecord.getValue());
    Assertions.assertEquals("N/A", minimumRecord.toString());
    minimumRecord.update(1);
    Assertions.assertEquals(1, (int) minimumRecord.getValue());
    Assertions.assertEquals("1", minimumRecord.toString());
    minimumRecord.update(0); // An update that changes the Record.
    Assertions.assertEquals(0, (int) minimumRecord.getValue());
    Assertions.assertEquals("0", minimumRecord.toString());
    minimumRecord.update(2); // An update that does not change the Record.
    Assertions.assertEquals(0, (int) minimumRecord.getValue());
    Assertions.assertEquals("0", minimumRecord.toString());
  }

}
