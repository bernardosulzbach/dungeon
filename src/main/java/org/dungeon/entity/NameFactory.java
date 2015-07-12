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

package org.dungeon.entity;

import org.dungeon.game.Name;
import org.dungeon.io.DLogger;

import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;
import org.jetbrains.annotations.NotNull;

public final class NameFactory {

  private NameFactory() {
    throw new AssertionError();
  }

  public static Name fromJsonObject(@NotNull JsonObject jsonObject) {
    String singular = jsonObject.get("singular").asString();
    String plural = readOrRenderPlural(jsonObject, singular);
    return Name.newInstance(singular, plural);
  }

  private static String readOrRenderPlural(@NotNull JsonObject jsonObject, String singular) {
    JsonValue value = jsonObject.get("plural");
    if (value == null) {
      return singular + 's';
    } else {
      String plural = value.asString();
      warnIfPluralIsUnnecessary(singular, plural);
      return plural;
    }
  }

  private static void warnIfPluralIsUnnecessary(@NotNull String singular, @NotNull String plural) {
    if ((singular + 's').equals(plural)) {
      DLogger.warning("Unnecessary JSON property: " + plural + " can be rendered from " + singular + ".");
    }
  }

}
