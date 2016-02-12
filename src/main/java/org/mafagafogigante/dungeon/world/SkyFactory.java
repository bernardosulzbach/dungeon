package org.mafagafogigante.dungeon.world;

import org.mafagafogigante.dungeon.game.World;

/**
 * The class responsible by creating skies.
 */
public class SkyFactory {

  /**
   * Makes and returns the sky of Darrowmere.
   */
  public static Sky makeDarrowmereSky(World world) {
    Sky sky = new Sky();
    TimeVisibilityRule sunRule = new TimeVisibilityRule(6, 18);
    SimpleAstronomicalBody sun = new SimpleAstronomicalBody(world, "the Sun, a large, golden, spherical body", sunRule);
    sky.addAstronomicalBody(sun);
    return sky;
  }

}
