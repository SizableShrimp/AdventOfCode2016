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

package me.sizableshrimp.adventofcode2016.days

import me.sizableshrimp.adventofcode2016.templates.SeparatedDay

class Day12 : SeparatedDay() {
    private lateinit var instructions: List<Instruction>

    override fun part1() = execute(LongArray(4))

    override fun part2() = execute(longArrayOf(0, 0, 1, 0))

    private fun execute(register: LongArray): Long {
        var insnIdx = 0
        val instructionCount = this.instructions.size

        while (true) {
            val instruction = this.instructions.get(insnIdx)
            insnIdx = instruction.apply(register, insnIdx)
            if (insnIdx >= instructionCount)
                return register[0]
        }
    }

    override fun parse() {
        this.instructions = this.lines.map {
            val split = it.split(' ')
            val a = split[1].toIntOrNull()
            val hasB = split.size >= 3
            val b = if (hasB) split[2].toIntOrNull() else null
            Instruction(Operation.ID_MAP[split[0]]!!, a ?: (split[1][0] - 'a'), a == null, if (!hasB) null else b ?: (split[2][0] - 'a'), b == null)
        }
    }

    enum class Operation(val id: String, internal val function: (LongArray, Int, Boolean, Int?, Boolean, Int) -> Int?) {
        Copy("cpy", { register, a, aRegister, b, _, _ -> register[b!!] = if (aRegister) register[a] else a.toLong(); null }),
        Increase("inc", { register, a, _, _, _, _ -> register[a]++; null }),
        Decrease("dec", { register, a, _, _, _, _ -> register[a]--; null }),
        Jump("jnz", { register, a, _, b, _, insnIdx -> if (register[a] != 0L) insnIdx + b!!.toInt() else null });

        companion object {
            val ID_MAP: Map<String, Operation> = Operation.values().associateBy { it.id }
        }
    }

    data class Instruction(private val operation: Operation, private val a: Int, private val aRegister: Boolean, private val b: Int?, private val bRegister: Boolean) {
        internal fun apply(register: LongArray, insnIdx: Int): Int {
            val result = this.operation.function(register, this.a, this.aRegister, this.b, this.bRegister, insnIdx)
            return result ?: (insnIdx + 1)
        }
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            Day12().run()
        }
    }
}