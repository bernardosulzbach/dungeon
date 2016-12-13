package org.mafagafogigante.dungeon.util;

class Mebibytes {

  private final double mebibytes;

  Mebibytes(long bytes) {
    this.mebibytes = bytes / Math.pow(2, 20);
  }

  @Override
  public String toString() {
    return String.format("%.1f MiB", mebibytes);
  }

}
