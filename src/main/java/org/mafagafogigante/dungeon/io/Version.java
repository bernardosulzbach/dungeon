package org.mafagafogigante.dungeon.io;

import org.mafagafogigante.dungeon.logging.DungeonLogger;

import java.io.IOException;
import java.io.Reader;
import java.util.Properties;

public class Version {

  public static final long MAJOR = 7L;
  private static final String PROPERTIES_NAME = "project.properties";
  private final String version;

  /**
   * Constructs a Version object with the current game version.
   */
  public Version() {
    String version = "";
    try (Reader input = ResourceStreamFactory.getInputStreamReader(PROPERTIES_NAME)) {
      Properties properties = new Properties();
      properties.load(input);
      version = properties.getProperty("version");
    } catch (IOException exception) {
      DungeonLogger.logSevere(exception);
    }
    this.version = version;
  }

  @Override
  public String toString() {
    return version;
  }

}
