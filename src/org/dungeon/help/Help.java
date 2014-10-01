/* 
 * Copyright (C) 2014 Bernardo Sulzbach
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.dungeon.help;

import org.dungeon.io.IO;
import org.dungeon.io.WriteStyle;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class Help {

    private static final List<CommandHelp> COMMANDS = new ArrayList<CommandHelp>();

    // Attempts to load the help strings from a text file.
    public static void initializeCommandList() {
        // TODO: write this parser as if I was a programmer (not a monkey).
        // Damn fool.
        BufferedReader bufferedReader;
        try {
            // TODO: move this text file to a resource folder?
            InputStream inputStream = Help.class.getResourceAsStream(HelpConstants.HELP_FILE_PATH);
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            bufferedReader = new BufferedReader(inputStreamReader);

            CommandHelpBuilder commandBuilder = null;
            String line;

            while ((line = bufferedReader.readLine()) != null) {
                // Trim possible excessive spaces and convert the string to lowercase.
                line = line.trim();
                if (!line.isEmpty()) {
                    if (line.charAt(0) == '#') {
                        continue;
                    }
                    if (line.contains("command")) {
                        line = line.substring(line.indexOf('=') + 1);
                        line = line.trim().replace("\"", "");
                        if (!line.isEmpty()) {
                            if (commandBuilder != null) {
                                COMMANDS.add(commandBuilder.createCommandHelp());
                            }
                            commandBuilder = new CommandHelpBuilder();
                            commandBuilder.setName(line);
                        } else {
                            throw new IllegalHelpFormatException("Missing string after '=' operand.");
                        }
                    } else if (line.contains("info")) {
                        line = line.substring(line.indexOf('=') + 1);
                        line = line.trim().replace("\"", "");
                        if (!line.isEmpty()) {
                            if (commandBuilder != null) {
                                commandBuilder.setInfo(line);
                            }
                        } else {
                            throw new IllegalHelpFormatException("Missing string after '=' operand.");
                        }
                    } else if (line.contains("aliases")) {
                        line = line.substring(line.indexOf('=') + 1);
                        if (commandBuilder != null) {
                            if (!line.isEmpty()) {
                                commandBuilder.setAliases(parseStringArray(line));
                            } else {
                                throw new IllegalHelpFormatException("Missing string after '=' operand.");
                            }
                        }
                    } else if (line.contains("arguments")) {
                        line = line.substring(line.indexOf('=') + 1);
                        if (commandBuilder != null) {
                            if (!line.isEmpty()) {
                                commandBuilder.setArguments(parseStringArray(line));
                            } else {
                                throw new IllegalHelpFormatException("Missing string after '=' operand.");
                            }
                        }

                    }
                }
            }
            // Add our current command.
            if (commandBuilder != null) {
                COMMANDS.add(commandBuilder.createCommandHelp());
            }
        } catch (IOException ignored) {
        }
    }

    /**
     * Parses an array of strings and returns the individual strings.
     * <p/>
     * For instance, passing
     * "\"word\", \"another\", \"more three words.\""
     * to this method will result in
     * ["word", "another", "more three words"]
     */
    private static String[] parseStringArray(String line) {
        List<String> innerStrings = new ArrayList<String>();
        for (String substring : line.split("\"")) {
            substring = substring.trim();
            if (!substring.isEmpty()) {
                innerStrings.add(substring);
            }
        }
        String[] aliasArray = new String[innerStrings.size()];
        innerStrings.toArray(aliasArray);
        return aliasArray;

    }

    /**
     * Print the a help string based on the specifiers.
     */
    public static void printCommandHelp(String[] words) {
        if (words.length == 1) {
            // There are no specifiers, report the correct usage of this method.
            IO.writeString(HelpConstants.HELP_USAGE, WriteStyle.MARGIN);
        } else {
            for (CommandHelp command : COMMANDS) {
                if (command.equalsIgnoreCase(words[1])) {
                    // Output to toString method of the first command that matches the input.
                    IO.writeString(command.toString(), WriteStyle.COMMAND);
                    return;
                }
            }
            // There was no match.
            IO.writeString(String.format("No help text for '%s' could be found.", words[1]), WriteStyle.MARGIN);
        }
    }

    /**
     * Prints a list of commands based on the commands in COMMANDS.
     */
    public static void printCommandList() {
        StringBuilder builder = new StringBuilder();
        for (CommandHelp command : COMMANDS) {
            builder.append(command.toOneLineString());
            builder.append('\n');
        }
        IO.writeString(builder.toString(), WriteStyle.MARGIN);
    }

    private static class IllegalHelpFormatException extends RuntimeException {
        private IllegalHelpFormatException(String message) {
            super(message);
        }
    }
}
