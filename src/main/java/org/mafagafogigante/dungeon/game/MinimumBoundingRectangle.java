package org.mafagafogigante.dungeon.game;

import org.mafagafogigante.dungeon.io.Version;

import java.io.Serializable;

public class MinimumBoundingRectangle implements Serializable {

  private static final long serialVersionUID = Version.MAJOR;
  private final int width;
  private final int height;

  public MinimumBoundingRectangle(int width, int height) {
    this.width = width;
    this.height = height;
  }

  public int getWidth() {
    return width;
  }

  public int getHeight() {
    return height;
  }

}
