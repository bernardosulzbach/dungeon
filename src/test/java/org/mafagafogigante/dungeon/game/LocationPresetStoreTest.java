/*
 * Copyright (C) 2015 Bernardo Sulzbach
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

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