package org.dungeon.utils;

import org.dungeon.io.IO;

/**
 * Poetry class that stores poems.
 * <p/>
 * Created by Bernardo on 22/10/2014.
 */
public class Poetry {

    private static final String THE_ROAD_NOT_TAKEN = "The road not taken\n" +
            "Two roads diverged in a yellow wood,\n" +
            "And sorry I could not travel both\n" +
            "And be one traveler, long I stood\n" +
            "And looked down one as far as I could\n" +
            "To where it bent in the undergrowth;\n" +
            "Then took the other, as just as fair,\n" +
            "And having perhaps the better claim,\n" +
            "Because it was grassy and wanted wear\n" +
            "Though as for that the passing there\n" +
            "Had worn them really about the same,\n" +
            "And both that morning equally lay\n" +
            "In leaves no step had trodden black.\n" +
            "Oh, I kept the first for another day!\n" +
            "Yet knowing how way leads onto way,\n" +
            "I doubted if I should ever come back.\n" +
            "I shall be telling this with a sigh\n" +
            "Somewhere ages and ages hence;\n" +
            "Two roads diverged in a wood, and I\n" +
            "I took the one less traveled by,\n" +
            "And that has made all the difference.\n" +
            "ROBERT FROST, 1916";

    public static void printRandomPoem() {
        IO.writeString(THE_ROAD_NOT_TAKEN);
    }

}
