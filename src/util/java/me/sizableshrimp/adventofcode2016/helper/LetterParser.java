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

import it.unimi.dsi.fastutil.ints.Int2CharMap;
import it.unimi.dsi.fastutil.ints.Int2CharMaps;
import it.unimi.dsi.fastutil.ints.Int2CharOpenHashMap;
import me.sizableshrimp.adventofcode2016.templates.Coordinate;

import java.util.Collection;

public class LetterParser {
    private static final Int2CharMap LETTER_MAP;

    static {
        Int2CharMap letterMap = new Int2CharOpenHashMap();

        // Similar to https://fontstruct.com/fontstructions/show/1650758/4x6-font
        // Similar to https://community.arduboy.com/t/4x6-font-for-the-arduboy/5589
        // Not exact copies; there are unique ones in here from checking outputs
        // Letters D I M N O Q S T V W X have not shown up yet, but have some guesses here based on the other 2 fonts
        // Letter Y does not fit 4x6
        letterMap.put(0b01100_10010_10010_11110_10010_10010, 'A');
        letterMap.put(0b11100_10010_11100_10010_10010_11100, 'B');
        letterMap.put(0b01100_10010_10000_10000_10010_01100, 'C');
        letterMap.put(0b11100_10010_10010_10000_10010_11100, 'D'); // Guess
        letterMap.put(0b11110_10000_11100_10000_10000_11110, 'E');
        letterMap.put(0b11110_10000_11100_10000_10000_10000, 'F');
        letterMap.put(0b01100_10010_10000_10110_10010_01110, 'G');
        letterMap.put(0b10010_10010_11110_10010_10010_10010, 'H');
        letterMap.put(0b11100_01000_01000_01000_01000_11100, 'I'); // Guess
        letterMap.put(0b00110_00010_00010_00010_10010_01100, 'J');
        letterMap.put(0b10010_10100_11000_10100_10100_10010, 'K');
        letterMap.put(0b10000_10000_10000_10000_10000_11110, 'L');
        letterMap.put(0b10010_11110_11110_10010_10010_10010, 'M'); // Guess
        letterMap.put(0b10010_11010_10110_10010_10010_10010, 'N'); // Guess
        letterMap.put(0b01100_10010_10010_10010_10010_01100, 'O'); // Guess
        letterMap.put(0b11100_10010_10010_11100_10000_10000, 'P');
        letterMap.put(0b01100_10010_10010_10010_10100_01010, 'Q'); // Guess
        letterMap.put(0b11100_10010_10010_11100_10100_10010, 'R');
        letterMap.put(0b01110_10000_01100_00010_00010_11100, 'S'); // Guess
        letterMap.put(0b01110_10000_01100_00010_00010_11100, 'T'); // Guess
        letterMap.put(0b10010_10010_10010_10010_10010_01100, 'U');
        // V
        // W
        // X
        letterMap.put(0b10001_10001_01010_00100_00100_00100, 'Y');
        letterMap.put(0b11110_00010_00100_01000_10000_11110, 'Z');

        letterMap.put(0, ' ');

        LETTER_MAP = Int2CharMaps.unmodifiable(letterMap);
    }

    /**
     * @return the letters in the grid, additionally appending a display of the grid if a letter cannot be parsed.
     */
    public static String getLettersOrGrid(Collection<Coordinate> coords) {
        String letters = getLetters(coords);
        return letters.indexOf('?') == -1 ? letters : letters + "\n" + Printer.toString(coords, (contains, coord) -> contains ? "██" : "  ");
    }

    public static String getLetters(Collection<Coordinate> coords) {
        int minX = Integer.MAX_VALUE;
        int minY = Integer.MAX_VALUE;
        int maxX = Integer.MIN_VALUE;

        for (Coordinate coord : coords) {
            if (coord.x < minX)
                minX = coord.x;
            if (coord.x > maxX)
                maxX = coord.x;
            if (coord.y < minY)
                minY = coord.y;
        }

        StringBuilder builder = new StringBuilder();

        for (int x = minX; x <= maxX; x += 5) {
            builder.append(getLetter(x, minY, coords));
        }

        return builder.toString();
    }

    public static char getLetter(int xOffset, int yOffset, Collection<Coordinate> coords) {
        int id = 0;

        int height = 6;
        int width = 5;
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (coords.contains(Coordinate.of(xOffset + x, yOffset + y)))
                    id |= 1 << ((height - y) * width - x - 1);
            }
        }

        return LETTER_MAP.getOrDefault(id, '?');
    }
}
