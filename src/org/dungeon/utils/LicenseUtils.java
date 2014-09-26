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

import org.dungeon.io.IO;
import org.dungeon.io.WriteStyle;

/**
 * Utility methods related to this project licensing.
 * <p/>
 * Change log
 * Created by Bernardo on 19/09/2014.
 */
public class LicenseUtils {

    public static final String LICENSE_STRING = "    Copyright (C) 2014 Bernardo Sulzbach\n\n" +
            "  This program is free software: you can redistribute it and/or modify\n" +
            "  it under the terms of the GNU General Public License as published by\n" +
            "  the Free Software Foundation, either version 3 of the License, or\n" +
            "  (at your option) any later version.\n\n" +
            "  This program is distributed in the hope that it will be useful,\n" +
            "  but WITHOUT ANY WARRANTY; without even the implied warranty of\n" +
            "  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the\n" +
            "  GNU General Public License for more details.";

    public static void printLicense() {
        IO.writeString(LICENSE_STRING, WriteStyle.MARGIN);
    }

}
