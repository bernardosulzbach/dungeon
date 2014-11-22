package org.dungeon.utils;

import org.dungeon.core.game.Command;
import org.dungeon.io.IO;

import java.awt.*;
import java.math.BigInteger;

/**
 * A collection of mathematical utility methods.
 * <p/>
 * Created by Bernardo on 22/10/2014.
 */
public class Math {

    private static final int FIBONACCI_MAX = 10000;

    /**
     * The public method that should be invoked using the input words.
     *
     * @param command the command entered by the player.
     */
    public static void fibonacci(Command command) {
        int intArgument;
        if (command.hasArguments()) {
            for (String strArgument : command.getArguments()) {
                try {
                    intArgument = Integer.parseInt(strArgument);
                } catch (NumberFormatException ignore) {
                    IO.writeString(String.format("Failed to parse " + strArgument + ".", Color.RED));
                    return;
                }
                if (0 < intArgument && intArgument <= FIBONACCI_MAX) {
                    char[] result = fibonacci(intArgument).toCharArray();
                    StringBuilder sb = new StringBuilder(result.length + 64);
                    sb.append("fibonacci").append('(').append(intArgument).append(')').append(" = ");
                    int newLineCharacters = 0;
                    for (char character : result) {
                        if ((sb.length() + 1 - newLineCharacters) % Constants.COLS == 0) {
                            sb.append("\\\n");
                            newLineCharacters++;
                        } else {
                            sb.append(character);
                        }
                    }
                    IO.writeString(sb.toString());
                } else {
                    IO.writeString("n must be positive and smaller than " + FIBONACCI_MAX + ".", Color.ORANGE);
                }
            }
        } else {
            Utils.printMissingArgumentsMessage();
        }
    }

    /**
     * Returns the n-th element of the fibonacci sequence.
     *
     * @param n the position of the element on the sequence. Must be positive.
     */
    private static String fibonacci(int n) {
        if (n < 1) {
            throw new IllegalArgumentException("n must be positive.");
        }
        BigInteger a = BigInteger.ZERO;
        BigInteger b = BigInteger.ONE;
        // Swap variable.
        BigInteger s;
        for (int i = 1; i < n; i++) {
            s = a;
            a = b;
            b = b.add(s);
        }
        return a.toString();
    }

}
