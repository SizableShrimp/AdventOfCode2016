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

import it.unimi.dsi.fastutil.ints.IntArrayList
import me.sizableshrimp.adventofcode2016.templates.SeparatedDay

class Day25 : SeparatedDay() {
    private var firstNum = 0
    private var secondNum = 0

    override fun part1(): Int {
        val magicConstant = this.firstNum * this.secondNum

        val leadingZeroBits = magicConstant.countLeadingZeroBits()
        val usedBits = 32 - leadingZeroBits
        // Based on a sample size of 2, this is always false
        val needsIncrease = magicConstant and (Int.MIN_VALUE ushr (leadingZeroBits + 1)) > 0
        // Force to be even and larger (we subtract one because 1 shl even == odd)
        var bits = usedBits + usedBits % 2 + (if (needsIncrease) 2 else 0) - 1
        var valid = 0
        while (bits > 0) {
            valid = valid or (1 shl bits)
            bits -= 2
        }

        return valid - magicConstant
    }

    override fun part2() = null // Part 2 does not exist

    override fun parse() {
        this.firstNum = this.lines[1].split(" ")[1].toInt()
        this.secondNum = this.lines[2].split(" ")[1].toInt()
    }

    // Checks if a starting number "a" would be a valid input to generate the clock signal
    // Magic constant must be added manually
    private fun valid(startingA: Int): Boolean {
        var a = startingA
        if (a % 2 != 0)
            return false

        var count = 0
        do {
            val wasEven = a % 2 == 0
            a /= 2
            val isEven = a % 2 == 0
            if (wasEven == isEven)
                return false
            count++
        } while (a > 0)

        return count % 2 == 0
    }

    // This is the simplified code, it isn't actually needed to find the answer.
    // Only two numbers in the entire input change from input to input.
    // Otherwise, to get to the clock signal, the starting "a" value
    // plus an offset of (firstNum * secondNum) is checked to be even, then odd, then even,
    // then odd after each consecutive divide by 2. The number of evens/odds must be even itself.
    // With an offset of 0, 10 would be a solution because 10 is even, 5 is odd,
    // 2 is even (5 / 2 = 2 under int floor division), and 0 is odd.
    // This can also be modeled using binary notation.
    // To be alternating even and odd, the binary notation must have always alternating 1s and 0s.
    // Because the number must start even, and the number of even-odds (or number of bits) must be even,
    // the last bit is always odd and therefore off.
    // This means any number that satisfies 0b(10)+ would work (although the magic constant must be
    // accounted for).
    private fun simulate(startingA: Int): Boolean {
        val output = IntArrayList()
        var a = startingA
        val d = a + this.firstNum * this.secondNum
        var b: Int

        while (true) {
            a = d
            do {
                b = a // a should be 0/1/0/1 (mod 2) (a must be even, odd, even, odd)
                a = b / 2 // a is halved until 0 (int floor division)
                b %= 2 // b mod 2 should be 0/1/0/1
                output.add(b)
            } while (a != 0)
        }
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            Day25().run()
        }
    }
}