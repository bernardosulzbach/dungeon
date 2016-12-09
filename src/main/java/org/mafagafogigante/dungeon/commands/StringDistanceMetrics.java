package org.mafagafogigante.dungeon.commands;

import org.apache.commons.lang3.StringUtils;

class StringDistanceMetrics {

  static int levenshteinDistance(final String a, final String b) {
    if (!CommandLimits.isWithinMaximumCommandLength(a)) {
      throw new IllegalArgumentException("input is too big.");
    }
    if (!CommandLimits.isWithinMaximumCommandLength(b)) {
      throw new IllegalArgumentException("input is too big.");
    }
    return StringUtils.getLevenshteinDistance(a, b);
  }

}
