package org.mafagafogigante.dungeon.util;

import org.mafagafogigante.dungeon.io.DungeonResource;
import org.mafagafogigante.dungeon.io.JsonObjectFactory;
import org.mafagafogigante.dungeon.io.ResourceNameResolver;

import java.util.List;

/**
 * Tutorial class that contains the game tutorial.
 */
public class Tutorial implements Writable {

  private static final String FILENAME = ResourceNameResolver.resolveName(DungeonResource.TUTORIAL);
  private static final String text = JsonObjectFactory.makeJsonObject(FILENAME).get("tutorial").asString();

  @Override
  public List<RichString> toRichStrings() {
    return new StandardRichTextBuilder().append(text).toRichText().toRichStrings();
  }

}
