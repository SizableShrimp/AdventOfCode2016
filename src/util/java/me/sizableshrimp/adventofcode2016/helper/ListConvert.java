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

import it.unimi.dsi.fastutil.booleans.BooleanArrayList;
import it.unimi.dsi.fastutil.booleans.BooleanList;
import it.unimi.dsi.fastutil.chars.CharArrayList;
import it.unimi.dsi.fastutil.chars.CharList;
import it.unimi.dsi.fastutil.doubles.DoubleArrayList;
import it.unimi.dsi.fastutil.doubles.DoubleList;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import it.unimi.dsi.fastutil.longs.LongArrayList;
import it.unimi.dsi.fastutil.longs.LongList;

import java.util.List;

public class ListConvert {
    /**
     * Parse all lines to integers using {@link Integer#parseInt(String)}.
     * <b>NOTE: Not the same as {@link LineConvert#ints}</b>
     *
     * @return A list of parsed integers.
     */
    public static IntList ints(List<String> list) {
        IntList l = new IntArrayList();
        for (String s : list) {
            l.add(Integer.parseInt(s));
        }
        return l;
    }

    public static LongList longs(List<String> list) {
        LongList l = new LongArrayList();
        for (String s : list) {
            l.add(Long.parseLong(s));
        }
        return l;
    }

    public static DoubleList doubles(List<String> list) {
        DoubleList l = new DoubleArrayList();
        for (String s : list) {
            l.add(Double.parseDouble(s));
        }
        return l;
    }

    public static BooleanList booleans(List<String> list) {
        BooleanList l = new BooleanArrayList();
        for (String s : list) {
            l.add(Boolean.parseBoolean(s));
        }
        return l;
    }

    public static CharList chars(List<String> list) {
        CharArrayList l = new CharArrayList();
        for (String s : list) {
            for (char c : s.toCharArray()) {
                l.add(c);
            }
        }
        return l;
    }
}
