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

package me.sizableshrimp.adventofcode2016.util

import it.unimi.dsi.fastutil.booleans.BooleanArrayList
import it.unimi.dsi.fastutil.booleans.BooleanList
import it.unimi.dsi.fastutil.chars.CharArrayList
import it.unimi.dsi.fastutil.chars.CharList
import it.unimi.dsi.fastutil.doubles.DoubleArrayList
import it.unimi.dsi.fastutil.doubles.DoubleList
import it.unimi.dsi.fastutil.ints.IntArrayList
import it.unimi.dsi.fastutil.ints.IntList
import it.unimi.dsi.fastutil.longs.LongArrayList
import it.unimi.dsi.fastutil.longs.LongList

fun <T> Iterable<T>.splitOn(splitter: (T) -> Boolean): List<List<T>> {
    val result = ArrayList<List<T>>()
    var subList = ArrayList<T>()
    result.add(subList)

    this.forEach {
        if (splitter(it)) {
            subList = ArrayList()
            result.add(subList)
        } else {
            subList.add(it)
        }
    }

    return result
}

fun <T> Iterable<T>.splitOnElement(element: T): List<List<T>> = splitOn { element == it }

fun Iterable<String>.splitOnBlankLines(): List<List<String>> = splitOn(String::isBlank)

fun List<String>.toInts(): IntList = this.mapTo(IntArrayList(this.size), String::toInt)

fun List<String>.toLongs(): LongList = this.mapTo(LongArrayList(this.size), String::toLong)

fun List<String>.toDoubles(): DoubleList = this.mapTo(DoubleArrayList(this.size), String::toDouble)

fun List<String>.toBooleans(): BooleanList = this.mapTo(BooleanArrayList(this.size), String::toBoolean)

fun List<String>.toChars(): CharList = this.mapTo(CharArrayList(this.size)) { it[0] }