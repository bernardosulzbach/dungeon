package org.mafagafogigante.dungeon.game;

import org.junit.jupiter.api.Test;

public class LocationPresetStoreTest {

  /**
   * This test also validates location preset data to some extent.
   */
  @Test
  public void testGetDefaultLocationPresetStoreShouldWork() throws Exception {
    LocationPresetStore.getDefaultLocationPresetStore(); // Triggers initialization of the data.
  }

}
