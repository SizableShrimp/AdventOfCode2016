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

import it.unimi.dsi.fastutil.ints.Int2IntMap;
import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import lombok.Data;
import me.sizableshrimp.adventofcode2016.helper.MatchWrapper;
import me.sizableshrimp.adventofcode2016.helper.Parser;
import me.sizableshrimp.adventofcode2016.templates.Day;

import java.util.regex.Pattern;

public class Day10 extends Day {
    private static final Pattern GIVE_PATTERN = Pattern.compile("bot (\\d+) gives low to (output|bot) (\\d+) and high to (output|bot) (\\d+)");
    private static final Pattern VALUE_PATTERN = Pattern.compile("value (\\d+) goes to bot (\\d+)");
    private Int2ObjectMap<Bot> bots;
    private Int2IntMap outputs;

    public static void main(String[] args) {
        new Day10().run();
    }

    @Override
    protected Result evaluate() {
        Bot part1 = null;
        boolean processing = true;
        while (processing) {
            processing = false;
            for (Bot bot : bots.values()) {
                if (bot.values.size() == 2) {
                    processing = true;
                    if (bot.distribute())
                        part1 = bot;
                }
            }
        }
        return Result.of(part1.id, outputs.get(0) * outputs.get(1) * outputs.get(2));
    }

    private Bot getBot(int id) {
        return bots.computeIfAbsent(id, Bot::new);
    }

    @Override
    protected void parse() {
        bots = new Int2ObjectOpenHashMap<>();
        outputs = new Int2IntOpenHashMap();

        for (String line : lines) {
            if (line.startsWith("value")) {
                MatchWrapper match = Parser.parseMatch(VALUE_PATTERN, line);
                Bot bot = getBot(match.groupInt(2));
                bot.values.add(match.groupInt(1));
            } else if (line.startsWith("bot")) {
                MatchWrapper match = Parser.parseMatch(GIVE_PATTERN, line);
                Bot bot = getBot(match.groupInt(1));
                boolean lowOutput = match.group(2).equals("output");
                int lowId = match.groupInt(3);
                boolean highOutput = match.group(4).equals("output");
                int highId = match.groupInt(5);
                bot.lowDest = new Destination(lowOutput, lowId);
                bot.highDest = new Destination(highOutput, highId);
            }
        }
    }

    @Data
    private class Bot {
        final int id;
        Destination lowDest;
        Destination highDest;
        final IntList values = new IntArrayList();

        boolean distribute() {
            int low = Math.min(values.getInt(0), values.getInt(1));
            int high = Math.max(values.getInt(0), values.getInt(1));
            distribute(lowDest, low);
            distribute(highDest, high);
            values.clear();
            return low == 17 && high == 61;
        }

        void distribute(Destination dest, int value) {
            if (dest.output) {
                outputs.put(dest.id, value);
            } else {
                getBot(dest.id).values.add(value);
            }
        }
    }

    private record Destination(boolean output, int id) {}
}
