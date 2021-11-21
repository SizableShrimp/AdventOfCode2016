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

import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import it.unimi.dsi.fastutil.longs.LongArrayList;
import it.unimi.dsi.fastutil.longs.LongList;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class LineConvert {
    /**
     * Find all integers in a line.
     *
     * @param line The string line.
     * @return The integers found in the line.
     */
    public static IntList ints(String line) {
        Matcher m = Pattern.compile("-?\\d+").matcher(line);
        IntList result = new IntArrayList();
        while (m.find()) {
            result.add(Integer.parseInt(m.group(0)));
        }
        return result;
    }

    /**
     * Find all integers in a line.
     *
     * @param line The string line.
     * @return The integers found in the line.
     */
    public static int[] intsArray(String line) {
        return ArrayConvert.unboxInts(ints(line));
    }

    public static LongList longs(String line) {
        Matcher m = Pattern.compile("-?\\d+").matcher(line);
        LongList result = new LongArrayList();
        while (m.find()) {
            result.add(Long.parseLong(m.group(0)));
        }
        return result;
    }

    public static List<Character> chars(String s) {
        return s.chars()
                .mapToObj(i -> (char) i)
                .collect(Collectors.toList());
    }
}
