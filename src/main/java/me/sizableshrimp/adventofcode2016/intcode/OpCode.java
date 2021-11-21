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

import lombok.AllArgsConstructor;
import lombok.experimental.Delegate;

import java.util.Arrays;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

@AllArgsConstructor
public enum OpCode {
    ACCUMULATE("acc", (ic, op) -> {
        ic.adjustAccumulator(op.getArgs()[0]);
        return null;
    }),
    JUMP("jmp", (ic, op) -> op.getArgs()[0]),
    NOP("nop", (ic, op) -> null);

    private static final Map<String, OpCode> codes = Arrays.stream(values())
            .collect(Collectors.toMap(oc -> oc.code, Function.identity()));
    final String code;
    @Delegate
    BiFunction<Intcode, Instruction, Integer> operation;

    public static OpCode getOpCode(String code) {
        OpCode opcode = codes.get(code);
        if (opcode != null)
            return opcode;
        throw new IllegalArgumentException();
    }
}
