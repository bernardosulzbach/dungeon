package org.mafagafogigante.dungeon.game;

import org.mafagafogigante.dungeon.game.LocationPreset.Type;

import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class LocationPresetStoreTest {

  private LocationPresetStore makeLocalLocationPresetStore() throws Exception {
    Constructor<LocationPresetStore> constructor = LocationPresetStore.class.getDeclaredConstructor();
    constructor.setAccessible(true); // Change the accessibility of constructor for external object creation.
    LocationPresetStore store = constructor.newInstance();
    constructor.setAccessible(false); // Make the constructor no longer accessible.
    return store;
  }

  /**
   * Adds a LocationPreset to a LocationPresetStore using reflection to access its addLocationPreset private method.
   */
  private void addLocationPreset(LocationPresetStore store, LocationPreset preset) throws Exception {
    Method method = LocationPresetStore.class.getDeclaredMethod("addLocationPreset", LocationPreset.class);
    method.setAccessible(true);
    method.invoke(store, preset);
    method.setAccessible(false);
  }

  /**
   * This test also validates location preset data to some extent.
   */
  @Test
  public void testGetDefaultLocationPresetStoreShouldWork() throws Exception {
    LocationPresetStore.getDefaultLocationPresetStore(); // Triggers initialization of the data.
  }

  @Test
  public void testAddLocationPresetShouldWorkWithDifferentIds() throws Exception {
    LocationPresetStore locationPresetStore = makeLocalLocationPresetStore();
    LocationPreset blueTest = new LocationPreset(new Id("BLUE_TEST"), Type.LAND, NameFactory.newInstance("Blue Test"));
    addLocationPreset(locationPresetStore, blueTest);
    LocationPreset redTest = new LocationPreset(new Id("RED_TEST"), Type.LAND, NameFactory.newInstance("Red Test"));
    addLocationPreset(locationPresetStore, redTest);
  }

  @Test
  public void testAddLocationPresetShouldFailForRepeatedIds() throws Exception {
    LocationPresetStore locationPresetStore = makeLocalLocationPresetStore();
    LocationPreset preset = new LocationPreset(new Id("TEST"), Type.LAND, NameFactory.newInstance("Blue Test"));
    addLocationPreset(locationPresetStore, preset);
    try {
      preset = new LocationPreset(new Id("TEST"), Type.LAND, NameFactory.newInstance("Red Test"));
      addLocationPreset(locationPresetStore, preset);
      Assert.fail("expected an IllegalArgumentException");
    } catch (InvocationTargetException expected) {
      if (!(expected.getCause() instanceof IllegalArgumentException)) {
        Assert.fail("expected an IllegalArgumentException");
      }
    }
  }

}
