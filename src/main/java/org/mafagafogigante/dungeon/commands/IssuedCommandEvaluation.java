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

package org.mafagafogigante.dungeon.commands;

import java.util.Collections;
import java.util.List;

public class IssuedCommandEvaluation {

  private final boolean valid;
  private final List<String> suggestions;

  public IssuedCommandEvaluation(boolean valid, List<String> suggestions) {
    this.valid = valid;
    this.suggestions = suggestions;
  }

  public boolean isValid() {
    return valid;
  }

  public List<String> getSuggestions() {
    return Collections.unmodifiableList(suggestions);
  }

  @Override
  public String toString() {
    return "IssuedCommandEvaluation{" +
        "valid=" + valid +
        ", suggestions=" + suggestions +
        '}';
  }

}
