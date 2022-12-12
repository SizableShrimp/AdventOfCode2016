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

import me.sizableshrimp.adventofcode2016.templates.Day

class Day23 : Day() {
    private lateinit var instructions: List<Instruction>

    override fun evaluate(): Result {
        return Result.of(execute(longArrayOf(7, 0, 0, 0)), execute(longArrayOf(12, 0, 0, 0)))
    }

    private fun execute(register: LongArray): Long {
        var insnIdx = 0
        val instructions = this.instructions.map(Instruction::copy)
        val instructionCount = instructions.size

        while (true) {
            val instruction = instructions.get(insnIdx)
            insnIdx = instruction.apply(instructions, register, insnIdx)
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

    enum class Operation(val id: String, internal val function: (List<Instruction>, LongArray, Int, Boolean, Int?, Boolean, Int) -> Int?) {
        COPY("cpy", { _, register, a, aRegister, b, bRegister, _ -> if (bRegister) register[b!!] = if (aRegister) register[a] else a.toLong(); null }),
        INCREASE("inc", { _, register, a, _, _, _, _ -> register[a]++; null }),
        DECREASE("dec", { _, register, a, _, _, _, _ -> register[a]--; null }),
        JUMP("jnz", { _, register, a, aRegister, b, bRegister, insnIdx -> if ((if (aRegister) register[a] else a) != 0L) insnIdx + (if (bRegister) register[b!!].toInt() else b!!) else null }),
        TOGGLE("tgl", { instructions, register, a, aRegister, _, _, insnIdx ->
            val targetIdx = insnIdx + if (aRegister) register[a].toInt() else a
            if (targetIdx >= 0 && targetIdx < instructions.size) instructions[targetIdx].toggle()
            null
        });

        companion object {
            val ID_MAP: Map<String, Operation> = Operation.values().associateBy { it.id }
        }
    }

    data class Instruction(private var operation: Operation, private val a: Int, private val aRegister: Boolean, private val b: Int?, private val bRegister: Boolean) {
        internal fun apply(instructions: List<Instruction>, register: LongArray, insnIdx: Int): Int {
            val result = this.operation.function(instructions, register, this.a, this.aRegister, this.b, this.bRegister, insnIdx)
            return result ?: (insnIdx + 1)
        }

        internal fun toggle() {
            this.operation = when (this.operation) {
                Operation.INCREASE -> Operation.DECREASE
                Operation.DECREASE, Operation.TOGGLE -> Operation.INCREASE
                Operation.COPY -> Operation.JUMP
                Operation.JUMP -> Operation.COPY
            }
        }

        internal fun copy() = Instruction(operation, a, aRegister, b, bRegister)
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            Day23().run()
        }
    }
}