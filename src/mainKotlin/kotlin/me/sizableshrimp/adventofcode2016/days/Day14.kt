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

import com.google.common.hash.Hashing
import it.unimi.dsi.fastutil.ints.Int2ObjectFunction
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap
import me.sizableshrimp.adventofcode2016.templates.SeparatedDay

class Day14 : SeparatedDay() {
    private val salt = this.lines[0]

    override fun part1() = simulate(1)

    override fun part2() = simulate(2017)

    private fun simulate(numHashes: Int): Int {
        val hashes = Int2ObjectOpenHashMap<String>()
        val hashKey: (Int) -> String = { id ->
            hashes.computeIfAbsent(id, Int2ObjectFunction {
                var value = this.salt + it

                for (i in 0 until numHashes) {
                    @Suppress("DEPRECATION")
                    value = Hashing.md5().hashString(value, Charsets.UTF_8).toString()
                }

                value
            })
        }
        var foundKeys = 0
        var index = -1
        while (foundKeys != 64) {
            index++
            val hash = hashKey(index)
            val windowed: List<String> = hash.windowed(3)
            val validChar = windowed.find { it.toSet().size == 1 }?.get(0) ?: continue
            run loop@{
                val target = validChar.toString().repeat(5)
                for (i in 1..1000) {
                    val subHash = hashKey(index + i)
                    if (subHash.contains(target)) {
                        foundKeys++
                        return@loop
                    }
                }
            }
        }
        return index
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            Day14().run()
        }
    }
}