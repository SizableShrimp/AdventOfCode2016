/*
 * AdventOfCode2016
 * Copyright (C) 2022 SizableShrimp
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

import me.sizableshrimp.adventofcode2016.templates.Coordinate;
import me.sizableshrimp.adventofcode2016.templates.EnumState;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class Parser {
    public static final Pattern COORDINATE_PATTERN = Pattern.compile("(\\d+,\\d+)");

    /**
     * Attempts to find every coordinate in the provided line and returns them in a list, in order.
     *
     * @param line The provided line to search for coordinates.
     * @return A list of {@link Coordinate}s found in the provided line, in order.
     */
    public static List<Coordinate> parseCoordinates(String line) {
        ArrayList<Coordinate> coordinates = new ArrayList<>();
        Matcher matcher = COORDINATE_PATTERN.matcher(line);

        while (matcher.find()) {
            coordinates.add(Coordinate.parse(matcher.group(1)));
        }

        return coordinates;
    }

    /**
     * Parses each line from the input list using the provided pattern and {@link Matcher#matches()}.
     *
     * @param pattern The pattern to use for matching to each line.
     * @param lines The list of lines to parse.
     * @param consumer The consumer to accept the {@link MatchWrapper} for each line.
     * Use {@link MatchResult#group()} to get the full line.
     */
    public static void parseLines(Pattern pattern, List<String> lines, Consumer<MatchWrapper> consumer) {
        for (String line : lines) {
            consumer.accept(parseMatch(pattern, line));
        }
    }

    /**
     * Parses each line from the input list using the provided pattern and {@link Matcher#matches()}.
     *
     * @param pattern The pattern to use for matching to each line.
     * @param lines The list of lines to parse.
     * @return A stream of the {@link MatchWrapper}s for each line.
     * Use {@link MatchResult#group()} to get the full line.
     */
    public static Stream<MatchWrapper> parseLinesStream(Pattern pattern, List<String> lines) {
        return lines.stream().map(l -> parseMatch(pattern, l));
    }

    /**
     * Creates a {@link Matcher} from the provided {@link Pattern} and {@link String} input.
     * The returned value will be a {@link MatchWrapper} that wraps a {@link MatchResult}
     * with overloaded helper functions.
     *
     * @param pattern The {@link Pattern} used to match the provided input.
     * @param input The {@link String} input to be matched against the pattern.
     * @return A {@link MatchWrapper} that wraps a {@link MatchResult}
     */
    public static MatchWrapper parseMatch(Pattern pattern, String input) {
        return parseMatch(pattern.matcher(input));
    }

    public static MatchWrapper parseMatch(Matcher matcher) {
        return parseMatch(matcher, false);
    }

    private static MatchWrapper parseMatch(Matcher matcher, boolean find) {
        if (find) {
            if (!matcher.find())
                throw new IllegalStateException("Matcher did not find a match in the input string");
        } else {
            if (!matcher.matches())
                throw new IllegalStateException("Matcher does not match the full input string");
        }
        return new MatchWrapper(matcher.toMatchResult());
    }

    public static MatchWrapper findFirstMatch(Pattern pattern, String input) {
        return parseMatch(pattern.matcher(input), true);
    }

    /**
     * Parses each line from the input list using the provided pattern and {@link Matcher#find}.
     *
     * @param pattern The pattern to use for matching to each line.
     * @param lines The list of lines to parse.
     * @param consumer The consumer to use the matcher and line data returned for each line.
     */
    public static void parseLinesFind(Pattern pattern, List<String> lines, BiConsumer<Matcher, String> consumer) {
        for (String line : lines) {
            Matcher m = pattern.matcher(line);
            m.find();
            consumer.accept(m, line);
        }
    }

    public static <T extends Enum<T> & EnumState<T>> T parseEnumState(T[] enumConstants, char c) {
        for (T t : enumConstants) {
            if (t.getMappedChar() == c)
                return t;
        }
        throw new IllegalArgumentException();
    }
}
