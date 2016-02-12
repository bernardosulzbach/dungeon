package org.mafagafogigante.dungeon.world;

import org.mafagafogigante.dungeon.game.World;

/**
 * A rule that describes whether or not something is visible.
 */
public interface VisibilityRule {

  boolean isRespected(World world);

}
