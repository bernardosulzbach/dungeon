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
import org.dungeon.utils.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public final class Help {

    private static final List<CommandHelp> COMMANDS = new ArrayList<CommandHelp>();
    private static final List<AspectHelp> ASPECTS = new ArrayList<AspectHelp>();
    private static boolean initialized;

    /**
     * Method that must be called in order to initialize the help text available in-game.
     */
    public static void initialize() {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        initCommandHelp(classLoader);
        initAspectHelp(classLoader);
        initialized = true;
    }

    public static boolean isInitialized() {
        return initialized;
    }

    // Attempts to load the help strings from a text file.
    private static void initCommandHelp(ClassLoader classLoader) {
        BufferedReader bufferedReader;
        try {
            InputStream inputStream = classLoader.getResourceAsStream(HelpConstants.COMMAND_TXT_PATH);
            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

            CommandHelpBuilder commandBuilder = null;
            String line;

            while ((line = bufferedReader.readLine()) != null) {
                // Trim possible excessive spaces and convert the string to lowercase.
                line = line.trim();
                if (StringUtils.isNotBlankString(line)) {
                    if (line.charAt(0) == '#') {
                        continue;
                    }
                    if (line.startsWith("COMMAND:")) {
                        line = line.substring(line.indexOf(':') + 1);
                        line = line.trim().replace("\"", "");
                        if (!line.isEmpty()) {
                            if (commandBuilder != null) {
                                COMMANDS.add(commandBuilder.createCommandHelp());
                            }
                            commandBuilder = new CommandHelpBuilder();
                            commandBuilder.setName(line);
                        } else {
                            throw new IllegalHelpFormatException("Missing string after ':' operand.");
                        }
                    } else if (line.startsWith("INFO:")) {
                        line = line.substring(line.indexOf(':') + 1);
                        line = line.trim().replace("\"", "");
                        if (!line.isEmpty()) {
                            if (commandBuilder != null) {
                                commandBuilder.setInfo(line);
                            }
                        } else {
                            throw new IllegalHelpFormatException("Missing string after ':' operand.");
                        }
                    } else if (line.startsWith("ALIASES:")) {
                        line = line.substring(line.indexOf(':') + 1);
                        if (commandBuilder != null) {
                            if (!line.isEmpty()) {
                                commandBuilder.setAliases(parseStringArray(line));
                            } else {
                                throw new IllegalHelpFormatException("Missing string after ':' operand.");
                            }
                        }
                    } else if (line.startsWith("ARGUMENTS:")) {
                        line = line.substring(line.indexOf(':') + 1);
                        if (commandBuilder != null) {
                            if (!line.isEmpty()) {
                                commandBuilder.setArguments(parseStringArray(line));
                            } else {
                                throw new IllegalHelpFormatException("Missing string after ':' operand.");
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

    private static void initAspectHelp(ClassLoader classLoader) {
        BufferedReader bufferedReader;
        try {
            InputStream inputStream = classLoader.getResourceAsStream(HelpConstants.ASPECT_TXT_PATH);
            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

            AspectHelpBuilder aspectHelpBuilder = null;
            String line;

            // TODO: move this to GameData
            // TODO: benchmark what is faster String.split()[1] or String.substring(String.indexOf(char) + 1)
            while ((line = bufferedReader.readLine()) != null) {
                // Trim excessive spaces.
                line = line.trim();
                if (StringUtils.isNotBlankString(line)) {
                    if (line.charAt(0) == '#') {
                        continue;
                    }
                    if (line.startsWith("ASPECT:")) {
                        line = line.substring(line.indexOf(':') + 1);
                        line = line.trim().replace("\"", "");
                        if (!line.isEmpty()) {
                            if (aspectHelpBuilder != null) {
                                ASPECTS.add(aspectHelpBuilder.createAspectHelp());
                            }
                            aspectHelpBuilder = new AspectHelpBuilder();
                            aspectHelpBuilder.setName(line);
                        } else {
                            throw new IllegalHelpFormatException("Missing string after ':' operand.");
                        }
                    } else if (line.startsWith("INFO:")) {
                        line = line.substring(line.indexOf(':') + 1);
                        line = line.trim().replace("\"", "");
                        if (!line.isEmpty()) {
                            if (aspectHelpBuilder != null) {
                                aspectHelpBuilder.setInfo(line);
                            }
                        } else {
                            throw new IllegalHelpFormatException("Missing string after ':' operand.");
                        }
                    } else if (line.startsWith("ALIASES:")) {
                        line = line.substring(line.indexOf(':') + 1);
                        if (aspectHelpBuilder != null) {
                            if (!line.isEmpty()) {
                                aspectHelpBuilder.setAliases(parseStringArray(line));
                            } else {
                                throw new IllegalHelpFormatException("Missing string after ':' operand.");
                            }
                        }
                    }
                }
            }
            if (aspectHelpBuilder != null) {
                ASPECTS.add(aspectHelpBuilder.createAspectHelp());
            }
        } catch (IOException ignored) {
        }
    }

    /**
     * Parses an array of strings and returns the individual strings.
     * <p/>
     * For instance, passing ""word\", \"another\", \"more three words.\"" to this method will result in ["word",
     * "another", "more three words"]
     */
    private static String[] parseStringArray(String line) {
        List<String> innerStrings = new ArrayList<String>();
        StringBuilder builder = new StringBuilder();
        boolean insideString = false;
        for (int i = 0; i < line.length(); i++) {
            if (line.charAt(i) == '"') {
                insideString = !insideString;
                // Just finished a string.
                if (!insideString) {
                    innerStrings.add(builder.toString());
                    builder.setLength(0);
                }
            } else {
                if (insideString) {
                    builder.append(line.charAt(i));
                }
            }
        }
        String[] aliasArray = new String[innerStrings.size()];
        innerStrings.toArray(aliasArray);
        return aliasArray;

    }

    public static void printHelp(String[] words) {
        if (!initialized) {
            IO.writeString(HelpConstants.NOT_INITIALIZED);
        } else if (words.length == 1) {
            // There are no specifiers, report the correct usage of this method.
            IO.writeString(HelpConstants.HELP_USAGE);
        } else {
            if (!printCommandHelp(words)) {
                if (!printAspectHelp(words)) {
                    // There was no match.
                    IO.writeString(String.format("No help text for '%s' could be found.", words[1]));
                }
            }
        }
    }

    /**
     * Prints the help string for a given game aspect.
     */
    private static boolean printAspectHelp(String[] words) {
        for (AspectHelp aspectHelp : ASPECTS) {
            if (aspectHelp.equalsIgnoreCase(words[1])) {
                // Output to toString method of the first command that matches the input.
                IO.writeString(aspectHelp.toString());
                return true;
            }
        }
        return false;
    }

    /**
     * Prints the help string for a command based on the specifiers.
     */
    private static boolean printCommandHelp(String[] words) {
        for (CommandHelp commandHelp : COMMANDS) {
            if (commandHelp.equalsIgnoreCase(words[1])) {
                // Output to toString method of the first command that matches the input.
                IO.writeString(commandHelp.toString());
                return true;
            }
        }
        return false;
    }

    /**
     * Prints a list of all the commands in COMMANDS.
     */
    public static void printCommandList() {
        StringBuilder builder = new StringBuilder();
        for (CommandHelp commandHelp : COMMANDS) {
            builder.append(commandHelp.toOneLineString());
            builder.append('\n');
        }
        IO.writeString(builder.toString());
    }

    /**
     * Prints a list of commands based on the commands in COMMANDS.
     * Only commands whose first characters match (not case sensitively) those in start will be listed.
     */
    public static void printCommandList(String start) {
        StringBuilder builder = new StringBuilder();
        for (CommandHelp commandHelp : COMMANDS) {
            String[] nameAndAliases = commandHelp.getAllAliases();
            for (String alias : nameAndAliases) {
                if (start.length() <= alias.length()) {
                    if (alias.substring(0, start.length()).equalsIgnoreCase(start)) {
                        builder.append(commandHelp.toOneLineString(alias)).append('\n');
                        break;
                    }
                }
            }
        }
        if (builder.length() == 0) {
            builder.append("No command starts with '").append(start).append("'.");
        }
        IO.writeString(builder.toString());
    }

    // TODO: consider also providing the number of the line of the text file that caused the error.
    private static class IllegalHelpFormatException extends RuntimeException {

        private IllegalHelpFormatException(String message) {
            super(message);
        }
    }
}
