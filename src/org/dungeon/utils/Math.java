package org.dungeon.utils;

import org.dungeon.io.IO;

import java.awt.*;
import java.math.BigInteger;

/**
 * A collection of mathematical utility methods.
 * <p/>
 * Created by Bernardo on 22/10/2014.
 */
public class Math {

    // Limits the fibonacci argument to 400 so its output does not span multiple lines.
    private static final int FIBONACCI_MAX = 400;

    /**
     * The public method that should be invoked using the input words.
     *
     * @param stringArgument the argument passed as a string.
     */
    public static void fibonacci(String stringArgument) {
        int intArgument;
        try {
            intArgument = Integer.parseInt(stringArgument);
        } catch (NumberFormatException ignore) {
            IO.writeString(String.format("Failed to parse " + stringArgument + ".", Color.RED));
            return;
        }
        if (0 < intArgument && intArgument <= 400) {
            IO.writeString("= " + fibonacci(intArgument));
        } else {
            IO.writeString("n must be positive and smaller than " + FIBONACCI_MAX + ".", Color.ORANGE);
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
