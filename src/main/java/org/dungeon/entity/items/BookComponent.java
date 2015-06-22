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

package org.dungeon.entity.items;

import org.dungeon.game.ID;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;

/**
 * The BookComponent class.
 */
public class BookComponent implements Serializable {

  private static final int SECONDS_PER_CHARACTER = 1;
  private final ID skillID;
  private final String text;

  /**
   * Creates a new BookComponent from a skill ID and a string of text.
   *
   * @param skillID the ID of a skill, nullable
   * @param text    a string of text, not null
   */
  public BookComponent(@Nullable ID skillID, @NotNull String text) {
    this.skillID = skillID;
    this.text = text;
  }

  /**
   * Returns whether or not this book teaches a skill.
   *
   * @return true if this book teaches a skill
   */
  public boolean isDidactic() {
    return skillID != null;
  }

  /**
   * Returns the ID of the skill this books teaches or null if this book does not teach any skill.
   */
  public ID getSkillID() {
    return skillID;
  }

  /**
   * Returns the readable text of the book.
   */
  public String getText() {
    return text;
  }

  /**
   * Returns how much time the Hero takes to read this book.
   * This is a positive integer proportional to the length of the text.
   *
   * @return a positive integer
   */
  public int getTimeToRead() {
    return getText().length() * SECONDS_PER_CHARACTER;
  }

  @Override
  public String toString() {
    String representation = String.format("This book teaches %s.", isDidactic() ? skillID : "nothing");
    representation += " " + "Text: " + text;
    return representation;
  }

}
