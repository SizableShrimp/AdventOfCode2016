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
import me.sizableshrimp.adventofcode2016.helper.Parser;
import me.sizableshrimp.adventofcode2016.templates.Day;

import java.util.Comparator;
import java.util.List;
import java.util.regex.Pattern;

public class Day04 extends Day {
    private List<Room> rooms;

    public static void main(String[] args) {
        new Day04().run();
    }

    @Override
    protected Result evaluate() {
        List<Room> valid = this.rooms.stream().filter(Room::isValid).toList();
        int sum = valid.stream().reduce(0, (c, r) -> c + r.id(), Integer::sum);
        int northPoleStorageId = valid.stream().filter(r -> r.shift().equals("northpole object storage")).findFirst().get().id();
        return Result.of(sum, northPoleStorageId);
    }

    @Override
    protected void parse() {
        rooms = Parser.parseLinesStream(Pattern.compile("([a-z\\-]+)-(\\d+)\\[([a-z]+)]"), lines)
                .map(mw -> new Room(mw.group(1), mw.groupInt(2), mw.group(3)))
                .toList();
    }

    private record Room(String name, int id, String checksum) {
        boolean isValid() {
            Char2IntMap charMap = new Char2IntOpenHashMap();
            for (char c : name.toCharArray()) {
                if (c >= 'a' && c <= 'z')
                    charMap.compute(c, (k, v) -> v == null ? 1 : v + 1);
            }

            String calculatedChecksum = charMap.char2IntEntrySet().stream()
                    .sorted(Comparator.comparingInt(Char2IntMap.Entry::getIntValue).reversed().thenComparing(Char2IntMap.Entry::getCharKey))
                    .limit(5)
                    .map(Char2IntMap.Entry::getCharKey)
                    .reduce(new StringBuilder(), StringBuilder::append, StringBuilder::append)
                    .toString();
            return this.checksum.equals(calculatedChecksum);
        }

        String shift() {
            StringBuilder sb = new StringBuilder();
            for (char c : name.toCharArray()) {
                if (c == '-') {
                    sb.append(' ');
                } else {
                    char newLetter = (char) (((c - 'a') + id) % 26 + 'a');
                    sb.append(newLetter);
                }
            }
            return sb.toString();
        }
    }
}
