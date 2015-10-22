/*
 * Copyright (C) 2014 Bernardo Sulzbach
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

package org.mafagafogigante.dungeon.entity.items;

import org.mafagafogigante.dungeon.game.Id;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;

/**
 * The BookComponent class.
 */
public class BookComponent implements Serializable {

  private static final int SECONDS_PER_CHARACTER = 1;
  private final Id spellId;
  private final String text;

  /**
   * Creates a new BookComponent from a spell Id and a string of text.
   *
   * @param spellId the Id of a spell, nullable
   * @param text a string of text, not null
   */
  public BookComponent(@Nullable Id spellId, @NotNull String text) {
    this.spellId = spellId;
    this.text = text;
  }

  /**
   * Returns whether or not this book teaches a spell.
   *
   * @return true if this book teaches a spell
   */
  public boolean isDidactic() {
    return spellId != null;
  }

  /**
   * Returns the Id of the spell this books teaches or null if this book does not teach any spell.
   */
  public Id getSpellId() {
    return spellId;
  }

  /**
   * Returns the readable text of the book.
   */
  public String getText() {
    return text;
  }

  /**
   * Returns how much time the Hero takes to read this book. This is a positive integer proportional to the length of
   * the text.
   *
   * @return a positive integer
   */
  public int getTimeToRead() {
    return getText().length() * SECONDS_PER_CHARACTER;
  }

  @Override
  public String toString() {
    String representation = String.format("This book teaches %s.", isDidactic() ? spellId : "nothing");
    representation += " " + "Text: " + text;
    return representation;
  }

}
