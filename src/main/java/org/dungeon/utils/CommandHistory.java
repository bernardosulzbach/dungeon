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
package org.dungeon.utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * CommandHistory class that is used to keep track of all the commands issued by the player.
 * <p/>
 * Created by Bernardo Sulzbach on 23/09/2014.
 */
public class CommandHistory implements Serializable {

    private static final long serialVersionUID = 1L;

    private final List<String> commands;
    private long characterCount;

    public CommandHistory() {
        commands = new ArrayList<String>();
        characterCount = 0;
    }

    public int getCommandCount() {
        return commands.size();
    }

    public void addCommand(String command) {
        characterCount += command.length();
        commands.add(command);
    }

    public long getCharacterCount() {
        return characterCount;
    }

    public String getCommandAt(int commandIndex) {
        return commands.get(commandIndex);
    }
}
