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

package me.sizableshrimp.adventofcode2016.days;

import me.sizableshrimp.adventofcode2016.templates.SeparatedDay;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day09 extends SeparatedDay {
    private static final Pattern MARKER_PATTERN = Pattern.compile("\\((\\d+)x(\\d+)\\)");
    private String input;

    public static void main(String[] args) {
        new Day09().run();
    }

    @Override
    protected Object part1() {
        return getLength(input, 1, false);
    }

    @Override
    protected Object part2() {
        return getLength(input, 1, true);
    }

    private long getLength(String message, long count, boolean expandInner) {
        Matcher matcher = MARKER_PATTERN.matcher(message);
        int next = 0;
        long result = 0;
        while (matcher.find(next)) {
            int subLength = Integer.parseInt(matcher.group(1));
            int subCount = Integer.parseInt(matcher.group(2));
            result += expandInner
                    ? getLength(message.substring(matcher.end(), matcher.end() + subLength), subCount, true)
                    : subCount * (long) subLength;
            if (next != matcher.start()) {
                // If there was extra in-between that wasn't expanded, add it here now with implicit count=1
                result += matcher.start() - next;
            }
            next = matcher.end() + subLength;
        }
        return (next == 0 ? message.length() : result) * count;
    }

    @Override
    protected void parse() {
        input = lines.get(0);
    }
}
