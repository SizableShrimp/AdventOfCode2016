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

class Day21 : Day() {
    override fun evaluate(): Result {
        return Result.of(simulate("abcdefgh"), simulate("fbgdceah", reversed = true))
    }

    private fun simulate(input: String, reversed: Boolean = false): String {
        val password = StringBuilder(input)
        val range = if (reversed) this.lines.size - 1 downTo 0 else this.lines.indices

        for (lineIdx in range) {
            val line = this.lines[lineIdx]
            val split = line.split(" ")
            when (split[0]) {
                "swap" -> {
                    if (split[1] == "letter") {
                        val first = split[if (reversed) 5 else 2][0]
                        val second = split[if (reversed) 2 else 5][0]

                        for (i in password.indices) {
                            val char = password[i]

                            if (char == first) {
                                password[i] = second
                            } else if (char == second) {
                                password[i] = first
                            }
                        }
                    } else {
                        val first = split[if (reversed) 5 else 2].toInt()
                        val second = split[if (reversed) 2 else 5].toInt()
                        val a = password[first]
                        val b = password[second]
                        password[first] = b
                        password[second] = a
                    }
                }

                "rotate" -> {
                    val rotateCount = if (split[1] == "based") {
                        val letter = split[6][0]
                        val idx = password.indexOf(letter)
                        if (reversed) -REVERSE_ROTATE_LETTER_MAP[idx] else 1 + idx + if (idx >= 4) 1 else 0
                    } else {
                        val left = split[1][0] == 'l'
                        val num = split[2].toInt()
                        if (left xor reversed) -num else num
                    }

                    if (rotateCount == 0) continue

                    val oldPassword = password.toString()
                    val length = password.length
                    for (i in 0 until length) {
                        password[Math.floorMod(i + rotateCount, length)] = oldPassword[i]
                    }
                }

                "reverse" -> {
                    val first = split[2].toInt()
                    val second = split[4].toInt()
                    val diff = second - first
                    val oldPassword = password.toString()

                    for (i in 0..diff) {
                        password[first + i] = oldPassword[second - i]
                    }
                }

                "move" -> {
                    val first = split[if (reversed) 5 else 2].toInt()
                    val second = split[if (reversed) 2 else 5].toInt()
                    val letter = password[first]

                    password.deleteCharAt(first)
                    password.insert(second, letter)
                }
            }
        }

        return password.toString()
    }

    companion object {
        /**
         * Maps the current index of the letter to the rightward shift it took
         * to get there according to the "rotate based on position of letter X" rule
         */
        private val REVERSE_ROTATE_LETTER_MAP = intArrayOf(/* 9 but it's equivalent to 1 */ 1, 1, 6, 2, 7, 3, /* 8 but it's equivalent to 0 */ 0, 4)

        @JvmStatic
        fun main(args: Array<String>) {
            Day21().run()
        }
    }
}