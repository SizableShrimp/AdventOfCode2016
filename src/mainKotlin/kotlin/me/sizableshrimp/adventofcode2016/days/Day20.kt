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

class Day20 : Day() {
    private lateinit var ranges: List<LongRange>

    override fun evaluate(): Result {
        var totalAllowedIps = 0
        var firstAllowedIp = 0L
        var currentIp = 0L

        val max = UInt.MAX_VALUE.toLong()

        while (true) {
            val range = this.ranges.find { it.contains(currentIp) }

            if (range == null) {
                if (firstAllowedIp == 0L)
                    firstAllowedIp = currentIp
                currentIp++
                totalAllowedIps++
            } else {
                val last = range.last
                if (last == max)
                    break

                currentIp = last + 1L
            }
        }

        return Result.of(firstAllowedIp, totalAllowedIps)
    }

    override fun parse() {
        val ranges = ArrayList<LongRange>(this.lines.size)
        this.ranges = ranges

        for (line in this.lines) {
            val delimiter = line.indexOf('-')
            ranges.add(LongRange(line.substring(0, delimiter).toLong(), line.substring(delimiter + 1).toLong()))
        }
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            Day20().run()
        }
    }
}