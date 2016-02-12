package org.mafagafogigante.dungeon.util.library;

public final class Libraries {

  private static final AutomaticShuffledStringLibrary dreamLibrary = new AutomaticShuffledStringLibrary("dreams.json");
  private static final AutomaticShuffledStringLibrary hintLibrary = new AutomaticShuffledStringLibrary("hints.json");
  private static final PoetryLibrary poetryLibrary = new PoetryLibrary();

  private Libraries() {
    throw new AssertionError();
  }

  public static AutomaticShuffledStringLibrary getDreamLibrary() {
    return dreamLibrary;
  }

  public static AutomaticShuffledStringLibrary getHintLibrary() {
    return hintLibrary;
  }

  public static PoetryLibrary getPoetryLibrary() {
    return poetryLibrary;
  }

}
