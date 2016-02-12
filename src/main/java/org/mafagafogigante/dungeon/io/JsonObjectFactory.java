package org.mafagafogigante.dungeon.io;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonObject;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;

public class JsonObjectFactory {

  private static final ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
  private static final String JSON_EXTENSION = ".json";

  /**
   * Makes a new JsonObject from the resource file pointed to by the specified filename.
   *
   * @param filename the name of the JSON file, must end with .json, not null
   * @return a JsonObject that reads from the specified file
   * @throws IllegalFilenameExtensionException if the provided filename does not end with .json
   */
  public static JsonObject makeJsonObject(@NotNull String filename) {
    if (!filename.endsWith(JSON_EXTENSION)) {
      throw new IllegalFilenameExtensionException("filename must end with " + JSON_EXTENSION + ".");
    }
    // Using a BufferedReader here does not improve performance as the library is already buffered.
    Reader reader = new InputStreamReader(classLoader.getResourceAsStream(filename), Charset.forName("UTF-8"));
    try {
      return Json.parse(reader).asObject();
    } catch (IOException fatal) {
      throw new RuntimeException(fatal);
    }
  }

  public static class IllegalFilenameExtensionException extends IllegalArgumentException {

    public IllegalFilenameExtensionException(@NotNull String string) {
      super(string);
    }

  }

}
