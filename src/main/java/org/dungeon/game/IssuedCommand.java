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

package org.dungeon.game;

import org.dungeon.utils.Utils;

import java.util.Arrays;

/**
 * IssuedCommand class that wraps a String entered by the user and provides powerful query methods.
 * <p/>
 * An IssuedCommand is made up of at least one token (word) and is not case-sensitive.
 * <p/>
 * Created by Bernardo Sulzbach on 21/11/14.
 */
public final class IssuedCommand {

    private String stringRepresentation;
    private String[] tokens;

    public IssuedCommand(String source) {
        this.tokens = Utils.split(source);
        this.stringRepresentation = Utils.join(" ", tokens);
        if (tokens.length == 0) {
            throw new IllegalArgumentException("source must contain at least one token.");
        }
    }

    public String getStringRepresentation() {
        return stringRepresentation;
    }

    public String getFirstToken() {
        return tokens[0];
    }

    public boolean firstTokenEquals(String token) {
        return tokens[0].equalsIgnoreCase(token);
    }

    /**
     * @return true if there are at least two tokens, false otherwise.
     */
    public boolean hasArguments() {
        return tokens.length > 1;
    }

    public String getFirstArgument() {
        return tokens[1];
    }

    public boolean firstArgumentEquals(String argument) {
        if (hasArguments()) {
            return tokens[1].equalsIgnoreCase(argument);
        } else {
            throw new IllegalArgumentException("this command does not have arguments.");
        }
    }

    /**
     * @return an array with all tokens but the first.
     */
    public String[] getArguments() {
        return Arrays.copyOfRange(tokens, 1, tokens.length);
    }

    public int getTokenCount() {
        return tokens.length;
    }

}
