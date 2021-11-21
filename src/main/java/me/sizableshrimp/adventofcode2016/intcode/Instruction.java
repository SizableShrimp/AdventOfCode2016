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

package me.sizableshrimp.adventofcode2016.intcode;

import lombok.Value;
import me.sizableshrimp.adventofcode2016.helper.MatchWrapper;
import me.sizableshrimp.adventofcode2016.helper.Parser;

import java.util.ArrayList;
import java.util.List;

@Value
public class Instruction {
    int index;
    OpCode code;
    int[] args;

    public static List<Instruction> parseLines(List<String> lines) {
        List<Instruction> instructions = new ArrayList<>();

        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);
            instructions.add(Instruction.parse(i, line));
        }

        return instructions;
    }

    public static Instruction parse(int index, String line) {
        MatchWrapper match = Parser.parseMatch("(\\w+?) (.+?)", line);

        OpCode code = OpCode.getOpCode(match.group(1));

        String[] split = match.group(2).split(" ");
        int[] args = new int[split.length];
        for (int i = 0; i < split.length; i++) {
            String arg = split[i];
            args[i] = Integer.parseInt(arg);
        }

        return new Instruction(index, code, args);
    }

    /**
     * Returns a copy of the current {@link Instruction} with a modified {@link OpCode}.
     *
     * @param newCode The new {@link OpCode} to use.
     * @return A copy of the current instance with a modified {@link OpCode}.
     */
    public Instruction changeCode(OpCode newCode) {
        return new Instruction(index, newCode, args);
    }
}
