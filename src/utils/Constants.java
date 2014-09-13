package utils;

/**
 * Constants class is a general-purpose constant storing class.
 *
 * @author Bernardo Sulzbach
 */
public class Constants {

    public static final String TITLE = "Dungeon";

    /**
     * String used to improve output readability.
     */
    public static final String MARGIN = "  ";

    public static final String CAMPAIGN_PATH = "campaign.dungeon";
    public static final String LOAD_ERROR = "Could not load a saved game.";
    public static final String SAVE_ERROR = "Could not save the game.";

    // Two 79-character long strings used to improve readability.
    public static final String LINE_1 = Utils.makeRepeatedCharacterString(79, '-');
    public static final String LINE_2 = Utils.makeRepeatedCharacterString(79, '=');

}
