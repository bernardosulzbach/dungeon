package org.dungeon.math;

import org.dungeon.io.IO;
import org.dungeon.utils.Constants;

import java.awt.*;
import java.math.BigInteger;

/**
 * A simple Fibonacci generator.
 * <p/>
 * Created by Bernardo on 22/10/2014.
 */
public class Fibonacci {

    /**
     * The public method that should be invoked using the input words.
     */
    public static void evaluate(String[] inputWords) {
        if (inputWords.length >= 2) {
            int n;
            try {
                n = Integer.parseInt(inputWords[1]);
            } catch (NumberFormatException ignore) {
                IO.writeString(String.format("Failed to parse \"%s\".", inputWords[1]), Color.RED);
                return;
            }
            if (0 < n && n <= 400) {
                IO.writeString("= " + fibonacci(n));
            } else {
                IO.writeString("n must be positive and smaller than 400.", Color.ORANGE);
            }
        } else {
            IO.writeString(Constants.INVALID_INPUT);
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
        BigInteger s;                          // Swap BigInteger.
        for (int i = 1; i < n; i++) {
            s = a;
            a = b;
            b = b.add(s);
        }
        return a.toString();
    }

}
