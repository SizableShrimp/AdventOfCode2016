/*
 * AdventOfCode2016
 * Copyright (C) 2021 SizableShrimp
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package me.sizableshrimp.adventofcode2016.helper;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ArrayConvert {
    /**
     * Parse all lines to integers.
     * <b>NOTE: Not the same as {@link LineConvert#ints}</b>
     *
     * @return An array of parsed integers.
     */
    public static int[] ints(List<String> list) {
        return convert(list, Integer::valueOf).mapToInt(i -> i).toArray();
    }

    private static <T> Stream<T> convert(List<String> list, Function<String, T> func) {
        return list.stream()
                .map(func);
    }

    public static int[] unboxInts(List<Integer> boxed) {
        return boxed.stream().mapToInt(i -> i).toArray();
    }

    public static long[] longs(List<String> list) {
        return convert(list, Long::valueOf).mapToLong(l -> l).toArray();
    }

    public static long[] unboxLongs(List<Long> boxed) {
        return boxed.stream().mapToLong(l -> l).toArray();
    }

    public static double[] doubles(List<String> list) {
        return convert(list, Double::valueOf).mapToDouble(d -> d).toArray();
    }

    public static double[] unboxDoubles(List<Double> boxed) {
        return boxed.stream().mapToDouble(d -> d).toArray();
    }

    public static boolean[] booleans(List<String> list) {
        // No BooleanStream type :(
        List<Boolean> boxed = convert(list, Boolean::valueOf).collect(Collectors.toList());
        return unboxBooleans(boxed);
    }

    public static boolean[] unboxBooleans(List<Boolean> boxed) {
        boolean[] arr = new boolean[boxed.size()];
        for (int i = 0; i < boxed.size(); i++) {
            arr[i] = boxed.get(i);
        }
        return arr;
    }

    public static char[] chars(List<String> list) {
        // No CharStream type :(
        List<Character> boxed = list.stream()
                .flatMap(s -> LineConvert.chars(s).stream())
                .collect(Collectors.toList());
        return unboxChars(boxed);
    }

    public static char[] unboxChars(List<Character> boxed) {
        char[] arr = new char[boxed.size()];
        for (int i = 0; i < boxed.size(); i++) {
            arr[i] = boxed.get(i);
        }
        return arr;
    }

    public static boolean[][] copyOf(boolean[][] array) {
        boolean[][] copy = new boolean[array.length][];
        for (int i = 0; i < array.length; i++) {
            boolean[] arr = array[i];
            copy[i] = new boolean[arr.length];
            System.arraycopy(arr, 0, copy[i], 0, arr.length);
        }
        return copy;
    }

    public static int[][] copyOf(int[][] array) {
        int[][] copy = new int[array.length][];
        for (int i = 0; i < array.length; i++) {
            int[] arr = array[i];
            copy[i] = new int[arr.length];
            System.arraycopy(arr, 0, copy[i], 0, arr.length);
        }
        return copy;
    }

    public static long[] prefixSumsLong(List<Long> list) {
        if (list.isEmpty())
            return new long[0];

        long[] prefixSums = new long[list.size()];
        prefixSums[0] = list.get(0);

        for (int i = 1; i < prefixSums.length; i++) {
            prefixSums[i] = list.get(i) + prefixSums[i - 1];
        }

        return prefixSums;
    }

    // private static <T, U extends BaseStream<T, U>> U convert(List<String> list, Function<String, T> func, Function<Stream<T>, U> transform) {
    //     Stream<T> boxedStream = list.stream()
    //             .map(func);
    //     return transform.apply(boxedStream);
    // }
}
