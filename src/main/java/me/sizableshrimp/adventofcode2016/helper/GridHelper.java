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

import it.unimi.dsi.fastutil.chars.Char2CharFunction;
import me.sizableshrimp.adventofcode2016.templates.Coordinate;
import me.sizableshrimp.adventofcode2016.templates.EnumState;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.LongFunction;
import java.util.function.Predicate;

public class GridHelper {
    /**
     * @param generator A {@link BiFunction} that provides two numbers in the form (y, x) and should output a 2d array of type {@link T}.
     * @param lines The list of lines to convert.
     * @param func The function to transform a specific character into type {@link T}
     * @param <T> The array type to be converted to from the list of lines.
     * @return A 2d-array holding objects of type {@link T} converted from {@code func}
     * where the outer array holds the y-coordinate and the inner array holds the x-coordinate.
     */
    public static <T> T[][] convert(BiFunction<Integer, Integer, T[][]> generator, List<String> lines, Function<Character, T> func) {
        T[][] grid = generator.apply(lines.size(), lines.get(0).length());
        return convert(grid, lines, func);
    }

    public static <T> T[][] convert(T[][] grid, List<String> lines, Function<Character, T> func) {
        convert(lines, (y, x, c) -> grid[y][x] = func.apply(c));
        return grid;
    }

    private static void convert(List<String> lines, GridConsumer consumer) {
        for (int y = 0; y < lines.size(); y++) {
            String line = lines.get(y);
            char[] chars = line.toCharArray();
            for (int x = 0; x < chars.length; x++) {
                consumer.accept(y, x, chars[x]);
            }
        }
    }

    @SuppressWarnings("unchecked")
    public static <T extends Enum<T> & EnumState<T>> T[][] convert(BiFunction<Integer, Integer, T[][]> generator, List<String> lines) {
        T[][] grid = generator.apply(lines.size(), lines.get(0).length());
        T[] enumConstants = ((Class<T>) grid.getClass().getComponentType().getComponentType()).getEnumConstants();
        return convert(grid, lines, c -> Parser.parseEnumState(enumConstants, c));
    }

    public static <T> T[][] reflectY(BiFunction<Integer, Integer, T[][]> generator, T[][] grid) {
        int yLength = grid.length;
        int xLength = grid[0].length;
        T[][] reflected = generator.apply(yLength, xLength);
        for (int y = 0; y < yLength; y++) {
            reflected[y] = Arrays.copyOf(grid[yLength - y - 1], xLength);
        }
        return reflected;
    }

    public static <T> T[][] reflectX(BiFunction<Integer, Integer, T[][]> generator, T[][] grid) {
        int yLength = grid.length;
        int xLength = grid[0].length;
        T[][] reflected = generator.apply(yLength, xLength);
        for (int y = 0; y < yLength; y++) {
            for (int x = 0; x < xLength; x++) {
                reflected[y][x] = grid[y][xLength - x - 1];
            }
        }
        return reflected;
    }

    public static <T> T[][] rotate(BiFunction<Integer, Integer, T[][]> generator, T[][] grid, int degrees) {
        if (degrees < 0)
            degrees = 360 + degrees;
        degrees %= 360;

        int yLength = grid.length;
        int xLength = grid[0].length;
        for (int i = 0; i < degrees; i += 90) {
            T[][] rotated = generator.apply(yLength, xLength);
            for (int y = 0; y < yLength; y++) {
                for (int x = 0; x < xLength; x++) {
                    rotated[y][x] = grid[xLength - x - 1][y];
                }
            }
            grid = rotated;
        }
        return grid;
    }

    public static boolean[][] convertBool(List<String> lines, Predicate<Character> pred) {
        return convertBool(new boolean[lines.size()][lines.get(0).length()], lines, pred);
    }

    public static boolean[][] convertBool(boolean[][] grid, List<String> lines, Predicate<Character> pred) {
        convert(lines, (y, x, c) -> grid[y][x] = pred.test(c));
        return grid;
    }

    public static int[][] convertInt(List<String> lines, IntFunction<Character> func) {
        return convertInt(new int[lines.size()][lines.get(0).length()], lines, func);
    }

    public static int[][] convertInt(int[][] grid, List<String> lines, IntFunction<Character> func) {
        convert(lines, (y, x, c) -> grid[y][x] = func.apply(c));
        return grid;
    }

    public static long[][] convertLong(List<String> lines, LongFunction<Character> func) {
        return convertLong(new long[lines.size()][lines.get(0).length()], lines, func);
    }

    public static long[][] convertLong(long[][] grid, List<String> lines, LongFunction<Character> func) {
        convert(lines, (y, x, c) -> grid[y][x] = func.apply(c));
        return grid;
    }

    public static char[][] convertChar(List<String> lines, Char2CharFunction func) {
        return convertChar(new char[lines.size()][lines.get(0).length()], lines, func);
    }

    public static char[][] convertChar(char[][] grid, List<String> lines, Char2CharFunction func) {
        convert(lines, (y, x, c) -> grid[y][x] = func.apply(c));
        return grid;
    }

    public static <T> void print(T[][] grid) {
        for (T[] ts : grid) {
            for (T t : ts) {
                System.out.print(t);
            }
            System.out.println();
        }
    }

    public static <T extends Enum<T> & EnumState<T>> void print(T[][] grid) {
        for (T[] ts : grid) {
            for (T t : ts) {
                System.out.print(t.getMappedChar());
            }
            System.out.println();
        }
    }

    public static void print(int[][] grid) {
        for (int[] ts : grid) {
            for (int t : ts) {
                System.out.print(t);
            }
            System.out.println();
        }
    }

    public static void print(long[][] grid) {
        for (long[] ts : grid) {
            for (long t : ts) {
                System.out.print(t);
            }
            System.out.println();
        }
    }

    public static void print(boolean[][] grid) {
        for (boolean[] ts : grid) {
            for (boolean t : ts) {
                System.out.print(t);
            }
            System.out.println();
        }
    }

    public static void print(char[][] grid) {
        for (char[] ts : grid) {
            for (char t : ts) {
                System.out.print(t);
            }
            System.out.println();
        }
    }

    public static <T> int countOccurrences(T[][] grid, T target) {
        int result = 0;
        for (T[] row : grid) {
            for (T t : row) {
                if (target.equals(t))
                    result++;
            }
        }
        return result;
    }

    public static int countOccurrences(int[][] grid, int target) {
        int result = 0;
        for (int[] row : grid) {
            for (int i : row) {
                if (i == target)
                    result++;
            }
        }
        return result;
    }

    public static <T> boolean isValid(T[][] grid, Coordinate coord) {
        return isValid(grid, coord.x, coord.y);
    }

    public static <T> boolean isValid(T[][] grid, int x, int y) {
        return extracted(x, y, grid[0].length, grid.length);
    }

    public static boolean isValid(int[][] grid, Coordinate coord) {
        return isValid(grid, coord.x, coord.y);
    }

    public static boolean isValid(int[][] grid, int x, int y) {
        return extracted(x, y, grid[0].length, grid.length);
    }

    public static boolean isValid(long[][] grid, Coordinate coord) {
        return isValid(grid, coord.x, coord.y);
    }

    public static boolean isValid(long[][] grid, int x, int y) {
        return extracted(x, y, grid[0].length, grid.length);
    }

    public static boolean isValid(boolean[][] grid, Coordinate coord) {
        return isValid(grid, coord.x, coord.y);
    }

    public static boolean isValid(boolean[][] grid, int x, int y) {
        return extracted(x, y, grid[0].length, grid.length);
    }

    public static boolean isValid(char[][] grid, Coordinate coord) {
        return isValid(grid, coord.x, coord.y);
    }

    public static boolean isValid(char[][] grid, int x, int y) {
        return extracted(x, y, grid[0].length, grid.length);
    }

    private static boolean extracted(int x, int y, int width, int height) {
        return y >= 0 && y < height && x >= 0 && x < width;
    }

    public static boolean allFalse(boolean[][] grid) {
        return countOccurrences(grid, false) == (grid.length * grid[0].length);
    }

    public static int countOccurrences(boolean[][] grid, boolean target) {
        int result = 0;
        for (boolean[] row : grid) {
            for (boolean b : row) {
                if (b == target)
                    result++;
            }
        }
        return result;
    }

    /**
     * Copy {@code base} into {@code copy}.
     */
    public static <T> T[][] copy(T[][] base, T[][] copy) {
        for (int i = 0; i < base.length; i++) {
            T[] ts = base[i];
            copy[i] = Arrays.copyOf(ts, ts.length);
        }
        return copy;
    }

    @SuppressWarnings("unchecked")
    public static <T> T[][] copy(T[][] base) {
        T[][] copy = (T[][]) Array.newInstance(base.getClass().getComponentType(), base.length);
        for (int i = 0; i < base.length; i++) {
            T[] ts = base[i];
            copy[i] = Arrays.copyOf(ts, ts.length);
        }
        return copy;
    }

    public static <T> boolean equals(T[][] a, T[][] b) {
        return Arrays.deepEquals(a, b);
    }

    public static boolean equals(boolean[][] a, boolean[][] b) {
        for (int i = 0; i < a.length; i++) {
            for (int j = 0; j < a[0].length; j++) {
                if (a[i][j] != b[i][j])
                    return false;
            }
        }
        return true;
    }

    @FunctionalInterface
    public interface GridConsumer {
        void accept(int y, int x, char c);
    }
}
