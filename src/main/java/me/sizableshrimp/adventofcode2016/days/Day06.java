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

import it.unimi.dsi.fastutil.chars.Char2IntMap;
import it.unimi.dsi.fastutil.chars.Char2IntOpenHashMap;
import me.sizableshrimp.adventofcode2016.templates.Day;

import java.util.Comparator;
import java.util.stream.Stream;

public class Day06 extends Day {
    public static void main(String[] args) {
        new Day06().run();
    }

    @Override
    protected Result evaluate() {
        return Result.of(getMessage(false), getMessage(true));
    }

    private String getMessage(boolean min) {
        int width = lines.get(0).length();
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < width; i++) {
            Char2IntOpenHashMap counts = new Char2IntOpenHashMap();
            for (String line : lines) {
                counts.addTo(line.charAt(i), 1);
            }
            Stream<Char2IntMap.Entry> stream = counts.char2IntEntrySet().stream();
            Comparator<Char2IntMap.Entry> comparator = Comparator.comparingInt(Char2IntMap.Entry::getIntValue);
            char c = (min ? stream.min(comparator) : stream.max(comparator)).orElseThrow().getCharKey();
            builder.append(c);
        }
        return builder.toString();
    }

    @Override
    protected void parse() {

    }
}
