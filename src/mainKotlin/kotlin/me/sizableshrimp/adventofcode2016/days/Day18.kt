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

class Day18 : SeparatedDay() {
    private var startingRowA = 0L
    private var startingRowB = 0L
    private var rowLength = 0

    override fun part1() = simulate(40)

    override fun part2() = simulate(400000)

    private fun simulate(rows: Int): Long {
        var prevRowA = this.startingRowA
        var prevRowB = this.startingRowB
        var totalSafe = prevRowA.countOneBits() + prevRowB.countOneBits().toLong()

        for (y in 1 until rows) {
            var curRowA = 0L
            var curRowB = 0L

            for (x in 0 until this.rowLength) {
                val leftTrap = x != 0 && isTrap(x - 1, prevRowA, prevRowB)
                val rightTrap = x != this.rowLength - 1 && isTrap(x + 1, prevRowA, prevRowB)

                if (leftTrap == rightTrap) {
                    if (x < 63) curRowA = curRowA or (1L shl x) else curRowB = curRowB or (1L shl (x - 63))
                    totalSafe++
                }
            }

            prevRowA = curRowA
            prevRowB = curRowB
        }

        return totalSafe
    }

    private fun isTrap(idx: Int, rowA: Long, rowB: Long) = (if (idx < 63) rowA else rowB) shr (if (idx < 63) idx else idx - 63) and 1L == 0L

    override fun parse() {
        this.rowLength = this.lines[0].length
        this.startingRowA = 0L
        this.startingRowB = 0L

        this.lines[0].forEachIndexed { x, c ->
            if (c == '^')
                return@forEachIndexed

            if (x < 63) {
                this.startingRowA = this.startingRowA or (1L shl x)
            } else {
                this.startingRowB = this.startingRowB or (1L shl (x - 63))
            }
        }
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            Day18().run()
        }
    }
}