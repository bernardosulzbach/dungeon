package org.mafagafogigante.dungeon.io;

import org.mafagafogigante.dungeon.logging.DungeonLogger;

import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.Reader;
import java.io.Serializable;
import java.util.Properties;

public class Version implements Comparable<Version>, Serializable {

  public static final long MAJOR = 7L;
  private static final long serialVersionUID = Version.MAJOR;
  private static final String PROPERTIES_NAME = "project.properties";
  private static final Version CURRENT_VERSION = readCurrentVersion();

  private final String version;

  /**
   * Constructs a new Version object from a version string.
   */
  public Version(@NotNull String version) {
    if (version.isEmpty()) {
      throw new VersionFormatException("version string cannot be empty");
    } else if (version.charAt(0) != 'v') {
      throw new VersionFormatException("version string must start with a 'v'");
    } else if (!StringUtils.containsOnly(version.substring(1), "0123456789.")) {
      throw new VersionFormatException("version string can only contain digits and periods");
    }
    this.version = version;
  }

  public static Version getCurrentVersion() {
    return CURRENT_VERSION;
  }

  private static Version readCurrentVersion() {
    String version = "";
    try (Reader input = ResourceStreamFactory.getInputStreamReader(PROPERTIES_NAME)) {
      Properties properties = new Properties();
      properties.load(input);
      version = properties.getProperty("version");
    } catch (IOException exception) {
      DungeonLogger.logSevere(exception);
    }
    return new Version(version);
  }

  private static Integer parseInteger(@NotNull String part, @NotNull String string) {
    try {
      return Integer.parseInt(part);
    } catch (NumberFormatException exception) {
      DungeonLogger.warning(String.format("Got invalid version string: %s", string));
    }
    return null;
  }

  @Override
  public String toString() {
    return version;
  }

  @Override
  public boolean equals(Object other) {
    if (this == other) {
      return true;
    }
    if (other == null || getClass() != other.getClass()) {
      return false;
    }
    return compareTo((Version) other) == 0;
  }

  @Override
  public int hashCode() {
    return version != null ? version.hashCode() : 0;
  }

  @Override
  public int compareTo(@NotNull Version version) {
    String ourString = this.version.substring(1);
    String otherString = version.version.substring(1);
    if (ourString.equals(otherString)) {
      return 0;
    }
    String[] ourParts = StringUtils.split(ourString, '.');
    String[] otherParts = StringUtils.split(otherString, '.');
    for (int i = 0; i < ourParts.length && i < otherParts.length; i++) {
      String ourPart = ourParts[i];
      String otherPart = otherParts[i];
      Integer ourInteger = parseInteger(ourPart, ourString);
      if (ourInteger == null) {
        return 0;
      }
      Integer otherInteger = parseInteger(otherPart, otherString);
      if (otherInteger == null) {
        return 0;
      }
      if (ourInteger < otherInteger) {
        return -1;
      } else if (ourInteger > otherInteger) {
        return 1;
      }
    }
    if (ourParts.length < otherParts.length) {
      return -1;
    } else if (ourParts.length == otherParts.length) {
      return 0;
    } else {
      return 1;
    }
  }

  public static class VersionFormatException extends IllegalArgumentException {

    VersionFormatException(@NotNull String string) {
      super(string);
    }

  }

}
