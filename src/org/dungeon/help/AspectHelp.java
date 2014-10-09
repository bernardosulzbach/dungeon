package org.dungeon.help;

/**
 * AspectHelp class that defines a structure for holding help text about a feature or aspect of the game.
 * <p/>
 * Created by Bernardo on 08/10/2014.
 */
// TODO: this class is really similar to CommandHelp, consider creating a common superclass or a common class.
public class AspectHelp {

    // Never set any of the field to null. Use a blank string ("") instead.
    /**
     * The aspects most common name.
     */
    private final String name;
    /**
     * Full description about this feature.
     */
    private final String info;
    /**
     * The array of aliases for the aspect.
     */
    private final String[] aliases;

    public AspectHelp(String name, String info, String[] aliases) {
        this.name = name;
        this.info = info;
        this.aliases = aliases;
    }

    /**
     * Verifies if any of the command aliases matches a string.
     */
    protected boolean equalsIgnoreCase(String word) {
        if (name.equalsIgnoreCase(word)) {
            return true;
        }
        for (String alias : aliases) {
            if (alias.equalsIgnoreCase(word)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        // Name
        // Should never be an empty string.
        builder.append(name);

        // Info
        builder.append('\n');
        if (info.isEmpty()) {
            builder.append(HelpConstants.NO_INFO);
        } else {
            builder.append("Info: ").append(info);
        }

        // Aliases
        builder.append('\n');
        if (!aliases[0].isEmpty()) {
            builder.append("Also referred to as: ");
            for (int i = 0; i < aliases.length; i++) {
                // If it is not the first usage example, append a comma.
                if (i != 0) {
                    builder.append(", ");
                }
                builder.append(aliases[i]);
            }
        }

        return builder.toString();
    }
}
