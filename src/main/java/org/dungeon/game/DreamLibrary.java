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

package org.dungeon.game;

import org.dungeon.io.DLogger;
import org.dungeon.io.ResourceReader;
import org.dungeon.util.AutomaticShuffledRange;

import java.util.ArrayList;

/**
 * The Dream Library.
 * <p/>
 * Created by Bernardo on 29/01/2015.
 */
public class DreamLibrary extends Library {

  private final ArrayList<String> dreams = new ArrayList<String>();
  private AutomaticShuffledRange automaticShuffledRange;

  public String getNextDream() {
    // TODO: make it isUninitialized in the superclass.
    if (!isInitialized()) {
      initialize();
    }
    return dreams.get(automaticShuffledRange.getNext());
  }

  @Override
  void load() {
    ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
    ResourceReader reader = new ResourceReader(classLoader.getResourceAsStream("dreams.txt"), "dreams.txt");
    while (reader.readNextElement()) {
      dreams.add(reader.getValue("DREAM"));
    }
    reader.close();
    dreams.trimToSize();
    automaticShuffledRange = new AutomaticShuffledRange(0, dreams.size());
    DLogger.info("Loaded " + dreams.size() + " dreams.");
  }

}
