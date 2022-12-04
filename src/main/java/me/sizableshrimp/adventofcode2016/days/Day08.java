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

import com.google.common.primitives.Booleans;
import it.unimi.dsi.fastutil.ints.IntList;
import me.sizableshrimp.adventofcode2016.helper.GridHelper;
import me.sizableshrimp.adventofcode2016.helper.LineConvert;
import me.sizableshrimp.adventofcode2016.templates.Day;

import java.util.Collections;

public class Day08 extends Day {
    public static void main(String[] args) {
        new Day08().run();
    }

    @Override
    protected Result evaluate() {
        boolean[][] grid = new boolean[6][50];

        for (String line : lines) {
            IntList ints = LineConvert.ints(line);
            if (line.startsWith("rect")) {
                int width = ints.getInt(0);
                int height = ints.getInt(1);
                for (int y = 0; y < height; y++) {
                    for (int x = 0; x < width; x++) {
                        grid[y][x] = true;
                    }
                }
            } else if (line.startsWith("rotate row")) {
                Collections.rotate(Booleans.asList(grid[ints.getInt(0)]), ints.getInt(1));
            } else if (line.startsWith("rotate column")) {
                boolean[] newColumn = new boolean[grid.length];
                int x = ints.getInt(0);
                int distance = ints.getInt(1);
                for (int y = 0; y < grid.length; y++) {
                    newColumn[(y + distance) % grid.length] = grid[y][x];
                }
                for (int y = 0; y < grid.length; y++) {
                    grid[y][x] = newColumn[y];
                }
            }
        }
        StringBuilder builder = new StringBuilder("\n");
        for (boolean[] row : grid) {
            for (boolean pixel : row) {
                builder.append(pixel ? "##" : "..");
            }
            builder.append('\n');
        }
        return Result.of(GridHelper.countOccurrences(grid, true), builder.toString());
    }

    @Override
    protected void parse() {

    }
}
