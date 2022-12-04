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

import it.unimi.dsi.fastutil.chars.Char2CharFunction;
import me.sizableshrimp.adventofcode2016.helper.GridHelper;
import me.sizableshrimp.adventofcode2016.templates.Coordinate;
import me.sizableshrimp.adventofcode2016.templates.Direction;
import me.sizableshrimp.adventofcode2016.templates.SeparatedDay;

import java.util.Arrays;
import java.util.List;

public class Day02 extends SeparatedDay {
    private static final char[][] PART_TWO_KEYPAD = GridHelper.convertChar(Arrays.asList(
            """
                      1\040\040
                     234\040
                    56789
                     ABC\040
                      D\040\040""".split("\n")
    ), Char2CharFunction.identity());
    private List<List<Direction>> instructions;

    public static void main(String[] args) {
        new Day02().run();
    }

    private boolean inBounds(Coordinate coord, boolean part2) {
        if (part2) {
            // int space = Math.abs(2 - coord.y);
            // return coord.y >= 0 && coord.y <= 4 && coord.x >= space && coord.x <= 4 - space;
            return coord.isValid(PART_TWO_KEYPAD) && PART_TWO_KEYPAD[coord.y][coord.x] != ' ';
        } else {
            return coord.x >= 0 && coord.x <= 2 && coord.y >= 0 && coord.y <= 2;
        }
    }

    @Override
    protected Object part1() {
        Coordinate coord = Coordinate.of(1, 1);
        int code = 0;
        for (List<Direction> list : instructions) {
            for (Direction dir : list) {
                Coordinate newCoord = coord.resolve(dir);
                if (inBounds(newCoord, false))
                    coord = newCoord;
            }
            code = code * 10 + getNum(coord);
        }
        return code;
    }

    @Override
    protected Object part2() {
        Coordinate coord = Coordinate.of(0, 3);
        StringBuilder code = new StringBuilder();
        for (List<Direction> list : instructions) {
            for (Direction dir : list) {
                Coordinate newCoord = coord.resolve(dir);
                if (inBounds(newCoord, true))
                    coord = newCoord;
            }
            code.append(PART_TWO_KEYPAD[coord.y][coord.x]);
        }
        return code.toString();
    }

    private int getNum(Coordinate coord) {
        return coord.x + coord.y * 3 + 1;
    }

    @Override
    protected void parse() {
        instructions = lines.stream().map(s -> s.chars().mapToObj(c -> Direction.getCardinalDirection((char) c)).toList()).toList();
    }
}
