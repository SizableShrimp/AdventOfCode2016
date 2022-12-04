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

import it.unimi.dsi.fastutil.ints.IntList;
import me.sizableshrimp.adventofcode2016.helper.LineConvert;
import me.sizableshrimp.adventofcode2016.templates.SeparatedDay;

import java.util.ArrayList;
import java.util.List;

public class Day03 extends SeparatedDay {
    private List<IntList> triangles;
    private List<IntList> trianglesPart2;

    public static void main(String[] args) {
        new Day03().run();
    }

    @Override
    protected Object part1() {
        return getValid(triangles);
    }

    @Override
    protected Object part2() {
        return getValid(trianglesPart2);
    }

    private int getValid(List<IntList> triangles) {
        int valid = 0;
        for (IntList triangle : triangles) {
            int a = triangle.getInt(0);
            int b = triangle.getInt(1);
            int c = triangle.getInt(2);
            if (a + b > c && a + c > b && b + c > a)
                valid++;
        }
        return valid;
    }

    @Override
    protected void parse() {
        triangles = lines.stream().map(LineConvert::ints).toList();
        trianglesPart2 = new ArrayList<>();
        for (int i = 0; i < triangles.size(); i += 3) {
            for (int j = 0; j < 3; j++) {
                trianglesPart2.add(IntList.of(triangles.get(i).getInt(j), triangles.get(i + 1).getInt(j), triangles.get(i + 2).getInt(j)));
            }
        }
    }
}
