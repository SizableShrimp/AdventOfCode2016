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

import me.sizableshrimp.adventofcode2016.helper.LineConvert
import me.sizableshrimp.adventofcode2016.templates.SeparatedDay

class Day15 : SeparatedDay() {
    private val discs = ArrayList<Disc>()
    private val discsPart2 = ArrayList<Disc>()

    override fun part1() = simulate(this.discs)

    override fun part2() = simulate(this.discsPart2)

    private fun simulate(discs: List<Disc>): Int {
        var time = 0

        while (true) {
            var i = 1
            if (discs.all { (it.startPos + time + i++) % it.positions == 0 })
                break
            time++
        }

        return time
    }

    override fun parse() {
        this.discs.clear()

        for (line in this.lines) {
            val ints = LineConvert.ints(line)
            this.discs.add(Disc(ints.getInt(3), ints.getInt(1)))
        }

        this.discsPart2.clear()
        this.discsPart2.addAll(this.discs)
        this.discsPart2.add(PART_2_DISC)
    }

    data class Disc(val startPos: Int, val positions: Int)

    companion object {
        private val PART_2_DISC = Disc(0, 11)

        @JvmStatic
        fun main(args: Array<String>) {
            Day15().run()
        }
    }
}