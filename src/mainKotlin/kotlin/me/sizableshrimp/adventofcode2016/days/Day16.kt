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
import java.util.BitSet

class Day16 : SeparatedDay() {
    private var initialState: Int = 0
    private var initialLength: Int = 0

    override fun part1() = simulate(272)

    override fun part2() = simulate(35651584)

    private fun simulate(targetLength: Int): String {
        var length = this.initialLength
        var data = BitSet.valueOf(longArrayOf(this.initialState.toLong()))
        while (length < targetLength) {
            val newLength = 2 * length + 1
            val newData = BitSet(newLength)
            for (i in 0 until length) {
                if (data.get(i))
                    newData.set(length + 1 + i)
            }
            for (i in 0 until length) {
                if (!data.get(length - i - 1))
                    newData.set(i)
            }
            data = newData
            length = newLength
        }

        if (data.length() > targetLength) {
            val numRemove = data.length() - targetLength
            val newData = BitSet(targetLength - numRemove)
            for (i in 0 until targetLength) {
                if (data.get(i + numRemove))
                    newData.set(i)
            }
            data = newData
        }

        var checksum: BitSet = data
        var checksumLength = targetLength
        do {
            val oldChecksum = checksum
            checksumLength /= 2
            checksum = BitSet(checksumLength)
            for (i in 0 until checksumLength) {
                if (oldChecksum.get(2 * i) == oldChecksum.get(2 * i + 1))
                    checksum.set(i)
            }
        } while (checksumLength % 2 == 0)

        return checksum.toLongArray()[0].toString(2).padStart(checksumLength, '0');
    }

    override fun parse() {
        val line = this.lines[0]
        this.initialState = line.toInt(2)
        this.initialLength = line.length
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            Day16().run()
        }
    }
}